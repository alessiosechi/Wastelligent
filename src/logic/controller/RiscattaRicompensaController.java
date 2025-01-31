package logic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import logic.beans.RicompensaBean;
import logic.beans.RiscattoBean;
import logic.beans.SegnalazioneBean;
import logic.exceptions.CodiceRiscattoNonTrovatoException;
import logic.exceptions.ConnessioneAPIException;
import logic.exceptions.DailyRedemptionLimitException;
import logic.exceptions.GestioneRiscattoException;
import logic.exceptions.InsufficientPointsException;
import logic.model.dao.CoordinateDao;
import logic.model.dao.DaoFactory;
import logic.model.dao.RicompensaDao;
import logic.model.dao.RiscattoDao;
import logic.model.dao.SegnalazioneDao;
import logic.model.dao.UtenteBaseDao;
import logic.model.domain.Ricompensa;
import logic.model.domain.Riscatto;
import logic.model.domain.Segnalazione;
import logic.model.domain.UtenteBase;
import logic.model.domain.Coordinate;
import logic.model.domain.LoggedUser;
import logic.observer.Observer;
import logic.utils.DateUtils;

public class RiscattaRicompensaController {

	private static volatile RiscattaRicompensaController instance;
	private static final Logger logger = Logger.getLogger(RiscattaRicompensaController.class.getName());
	private UtenteBase utente = null;

	private RicompensaDao ricompensaDao;
	private UtenteBaseDao utenteBaseDao;
	private SegnalazioneDao segnalazioneDao;
	private RiscattoDao riscattoDao;
	private CoordinateDao coordinateDao;

	private RiscattaRicompensaController() {
		try {
			ricompensaDao = DaoFactory.getDao(RicompensaDao.class);
			utenteBaseDao = DaoFactory.getDao(UtenteBaseDao.class);
			segnalazioneDao = DaoFactory.getDao(SegnalazioneDao.class);
			riscattoDao = DaoFactory.getDao(RiscattoDao.class);
			coordinateDao = DaoFactory.getDao(CoordinateDao.class);

		} catch (Exception e) {
			logger.severe("Errore durante l'inizializzazione: " + e.getMessage());
		}
	}

	public static RiscattaRicompensaController getInstance() {
		RiscattaRicompensaController result = instance;

		if (instance == null) {
			synchronized (RiscattaRicompensaController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RiscattaRicompensaController();
				}
			}
		}
		return result;
	}
	
    public void caricaUtente() {
        LoggedUser utenteLoggato = LoggedUser.getInstance();
        int punti = utenteBaseDao.estraiPunti(utenteLoggato.getIdUtente());
        List<Segnalazione> segnalazioni = segnalazioneDao.getSegnalazioniRiscontrateByUtente(utenteLoggato.getIdUtente());
        List<Riscatto> riscatti = riscattoDao.getRiscattiByUtente(utenteLoggato.getIdUtente());

        utente = new UtenteBase(utenteLoggato.getIdUtente(), utenteLoggato.getUsername(), segnalazioni, riscatti, punti);
    }

	public void registraOsservatoreRiscatti(Observer observer) {
		utente.registraOsservatore(observer);
	}

	public List<RiscattoBean> getRiscatti() {
		return convertRiscattoListToBeanList(utente.getRiscatti());
	}

	public List<RicompensaBean> ottieniRicompenseAPI() throws ConnessioneAPIException {
		try {
			List<Ricompensa> ricompenseAPI = ricompensaDao.getRicompense();
			return convertRicompensaListToBeanList(ricompenseAPI);
		} catch (ConnessioneAPIException e) {
			// rilancio l'eccezione con un messaggio più generico
			throw new ConnessioneAPIException("Si è verificato un errore durante la connessione al server.", e);
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	public int ottieniPuntiUtente() {
		return utente.getPunti();
	}

	public List<RiscattoBean> ottieniRiscattiUtente() {
		try {
			return convertRiscattoListToBeanList(utente.getRiscatti());
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	public boolean riscatta(RicompensaBean ricompensaBean)
			throws DailyRedemptionLimitException, InsufficientPointsException, GestioneRiscattoException {

		try {
			verificaLimiteGiornaliero();
			int puntiRiscatto = calcolaPuntiNecessari(ricompensaBean.getValore());
			verificaPuntiSufficienti(puntiRiscatto);
			String codiceRiscatto = ottieniCodiceRiscatto(ricompensaBean.getIdRicompensa());


            Ricompensa ricompensa = new Ricompensa(
                    ricompensaBean.getNome(),
                    ricompensaBean.getValore(),
                    ricompensaBean.getDescrizione(),
                    ricompensaBean.getDataScadenza()
                );
            Riscatto riscatto = new Riscatto(
            		ricompensa,
            		utente.getIdUtente(),
                    puntiRiscatto,
            		codiceRiscatto,
                    java.time.LocalDate.now().toString()
            	);
            		


			utente.aggiornaPunti(utente.getPunti()-puntiRiscatto);
			utente.aggiungiRiscatto(riscatto);
			
			utenteBaseDao.sottraiPunti(utente.getIdUtente(), puntiRiscatto);
			riscattoDao.registra(riscatto);
			return true;

		} catch (DailyRedemptionLimitException | InsufficientPointsException | GestioneRiscattoException e) {
			throw e;
		} catch (Exception e) {
			logger.severe("Errore durante il riscatto della ricompensa: " + e.getMessage());
			return false;
		}
	}

	private void verificaLimiteGiornaliero() throws DailyRedemptionLimitException {

		int numeroRiscattiOggi = (int) utente.getRiscatti().stream().filter(r -> DateUtils.isOggi(r.getDataRiscatto())).count();

		if (numeroRiscattiOggi >= 5) {
			throw new DailyRedemptionLimitException("Hai raggiunto il limite giornaliero di riscatti.");
		}
	}

	private void verificaPuntiSufficienti(int puntiNecessari) throws InsufficientPointsException {
		int puntiUtente = ottieniPuntiUtente();

		if (puntiUtente < puntiNecessari) {
			throw new InsufficientPointsException("Punti insufficienti per riscattare la ricompensa.");
		}
	}

	private String ottieniCodiceRiscatto(int idRicompensa) throws GestioneRiscattoException {
		try {
			return ricompensaDao.getCodiceRiscatto(idRicompensa);
		} catch (ConnessioneAPIException e) {
			// messaggio che deve essere mostrato all'utente (nascondo l'errore interno)
			throw new GestioneRiscattoException(
					"Impossibile connettersi per ottenere il codice di riscatto. Riprova più tardi.", e);
		} catch (CodiceRiscattoNonTrovatoException e) {
			throw new GestioneRiscattoException("Nessun codice di riscatto trovato per la ricompensa selezionata.", e);
		}
	}

	public List<SegnalazioneBean> ottieniSegnalazioniUtente() {
		try {
			List<Segnalazione> segnalazioniUtente = utente.getSegnalazioni();

			if (!segnalazioniUtente.isEmpty()) {

				for (Segnalazione s : segnalazioniUtente) {
					Coordinate coordinate = new Coordinate(s.getLatitudine(), s.getLongitudine());
					String posizioneTesto = coordinateDao.ottieniPosizione(coordinate);

					s.setPosizione(posizioneTesto);
				}
				return convertSegnalazioneListToBeanList(segnalazioniUtente);

			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public int calcolaPuntiNecessari(int valoreRicompensa) {
		if (valoreRicompensa >= 1 && valoreRicompensa <= 100) {
			return 15;
		} else if (valoreRicompensa > 100 && valoreRicompensa <= 200) {
			return 30;
		} else if (valoreRicompensa > 200 && valoreRicompensa <= 300) {
			return 45;
		} else if (valoreRicompensa > 300 && valoreRicompensa <= 400) {
			return 60;
		} else {
			return 75; // default se il valore è superiore a 400
		}
	}


	private List<RicompensaBean> convertRicompensaListToBeanList(List<Ricompensa> ricompense) {
		if (ricompense == null) {
			return new ArrayList<>();
		}

		List<RicompensaBean> ricompensaBeanList = new ArrayList<>();

		for (Ricompensa r : ricompense) {
			RicompensaBean ricompensaBean = convertRicompensaToBean(r);
			ricompensaBeanList.add(ricompensaBean);
		}

		return ricompensaBeanList;
	}

	private RicompensaBean convertRicompensaToBean(Ricompensa r) {
		RicompensaBean ricompensaBean = new RicompensaBean();
		ricompensaBean.setIdRicompensa(r.getIdRicompensa());
		ricompensaBean.setNome(r.getNome());
		ricompensaBean.setValore(r.getValore());
		ricompensaBean.setDescrizione(r.getDescrizione());
		ricompensaBean.setDataScadenza(r.getDataScadenza());
		return ricompensaBean;
	}

	private List<RiscattoBean> convertRiscattoListToBeanList(List<Riscatto> riscatti) {
		List<RiscattoBean> riscattoBeanList = new ArrayList<>();

		for (Riscatto riscatto : riscatti) {
			RicompensaBean ricompensaBean = new RicompensaBean();
			ricompensaBean.setNome(riscatto.getNomeRicompensa());
			ricompensaBean.setValore(riscatto.getValoreRicompensa());
			ricompensaBean.setDescrizione(riscatto.getDescrizioneRicompensa());
			ricompensaBean.setDataScadenza(riscatto.getDataScadenzaRicompensa());

			RiscattoBean riscattoBean = new RiscattoBean();
			riscattoBean.setIdUtente(riscatto.getIdUtente());
			riscattoBean.setPunti(riscatto.getPunti());
			riscattoBean.setCodiceRiscatto(riscatto.getCodiceRiscatto());
			riscattoBean.setDataRiscatto(riscatto.getDataRiscatto());
			riscattoBean.setRicompensaBean(ricompensaBean);

			riscattoBeanList.add(riscattoBean);
		}

		return riscattoBeanList;
	}

	private List<SegnalazioneBean> convertSegnalazioneListToBeanList(List<Segnalazione> segnalazioniRiscontrate) {

		List<SegnalazioneBean> segnalazioneBeanList = new ArrayList<>();

		for (Segnalazione s : segnalazioniRiscontrate) {
			SegnalazioneBean segnalazioneBean = convertSegnalazioneToBean(s);
			segnalazioneBeanList.add(segnalazioneBean);
		}

		return segnalazioneBeanList;
	}

	private SegnalazioneBean convertSegnalazioneToBean(Segnalazione s) {
		SegnalazioneBean segnalazioneBean = new SegnalazioneBean();

		segnalazioneBean.setDescrizione(s.getDescrizione());
		segnalazioneBean.setFoto(s.getFoto());
		segnalazioneBean.setLatitudine(s.getLatitudine());
		segnalazioneBean.setLongitudine(s.getLongitudine());
		segnalazioneBean.setPuntiAssegnati(s.getPuntiAssegnati());
		segnalazioneBean.setPosizione(s.getPosizione());

		return segnalazioneBean;
	}
}
