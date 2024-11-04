package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import exceptions.DailyRedemptionLimitException;
import exceptions.InsufficientPointsException;
import model.dao.ListaRicompenseGithubDAO;
import model.dao.ListaRicompenseGithubDAOImplementazione;
import model.dao.UtenteDAO;
import model.dao.UtenteDAOImplementazione;
import model.dao.RicompensaDAO;
import model.dao.RicompensaDAOImplementazione;
import model.dao.SegnalazioneDAO;
import model.dao.SegnalazioneDAOImplementazione;
import model.domain.Ricompensa;
import model.domain.RicompensaBean;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;

public class RiscattaRicompensaController {

	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static Conversione conversione = new Conversione();
	private static ListaRicompenseGithubDAO listaRicompenseDAO;
	private static RicompensaDAO ricompensaDAO;
	private static UtenteDAO utenteDAO;
	private static SegnalazioneDAO segnalazioneDAO;
	private static volatile RiscattaRicompensaController instance;
	private static final Logger logger = Logger.getLogger(RiscattaRicompensaController.class.getName());
    private List<Ricompensa> ricompenseUtente = new ArrayList<>();

	private RiscattaRicompensaController() {
		try {

			listaRicompenseDAO = ListaRicompenseGithubDAOImplementazione.getInstance();
			ricompensaDAO = RicompensaDAOImplementazione.getInstance();
			utenteDAO = UtenteDAOImplementazione.getInstance();
			segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();

		} catch (Exception e) {
			logger.severe("Errore durante l'inizializzazione di uno dei DAO: " + e.getMessage());
			throw new RuntimeException("Inizializzazione fallita");
		}
	}

	public List<RicompensaBean> ottieniRicompenseAPI() {
		try {
			List<Ricompensa> ricompenseAPI = listaRicompenseDAO.getRicompense();

			if (!ricompenseAPI.isEmpty()) {

				return conversione.convertRicompensaListToBeanList(ricompenseAPI);
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<RicompensaBean> ottieniRicompenseUtente(int idUtente) {
		try {

	        ricompenseUtente = ricompensaDAO.getRicompenseByUtente(idUtente);

			if (!ricompenseUtente.isEmpty()) {
				return conversione.convertRicompensaListToBeanList(ricompenseUtente);
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public boolean riscatta(RicompensaBean ricompensaBean) throws DailyRedemptionLimitException, InsufficientPointsException{

		try {
			Ricompensa ricompensa = conversione.convertToEntity(ricompensaBean);

			int idUtente = ricompensa.getIdUtente();
			
			
	        
	        // controllo il numero di riscatti effettuati oggi
	        int numeroRiscattiOggi = 0;
	        for (Ricompensa r : ricompenseUtente) {

	            if (isOggi(r.getDataRiscatto())) {
	                numeroRiscattiOggi++;
	            }
	        }
	        
	        if (numeroRiscattiOggi >= 5) {
	        	logger.warning(String.format("Limite di 5 riscatti al giorno raggiunto per l'utente %s", idUtente));

	            throw new DailyRedemptionLimitException("Hai raggiunto il limite giornaliero di riscatti.");

	        }
			
			
			
			int puntiUtente = ottieniPuntiUtente(idUtente);
			int puntiNecessari = calcolaPuntiNecessari(ricompensa.getValore());
			
			
			
	        if (puntiUtente < puntiNecessari) {
	            logger.warning("Punti insufficienti per riscattare la ricompensa per l'utente " + idUtente);
	            throw new InsufficientPointsException("Punti insufficienti per riscattare la ricompensa.");
	        }

			ricompensa.setPunti(puntiNecessari);

			String codiceRiscatto = listaRicompenseDAO.getCodiceRiscatto(ricompensa.getIdRicompensa());
			ricompensa.setCodiceRiscatto(codiceRiscatto);

			logger.info(String.format("ID RICOMPENSA: %d, CODICE RISCATTO: %s", ricompensa.getIdRicompensa(), codiceRiscatto));


			// registro il riscatto
			ricompensaDAO.registraRicompensaRiscattata(ricompensa);

			// sottraggo i punti all'utente
			utenteDAO.sottraiPuntiUtente(idUtente, puntiNecessari);

			return true; // riscatto riuscito

		}catch (DailyRedemptionLimitException | InsufficientPointsException e) {
		    throw e; 
		} catch (Exception e) {
	        logger.severe("Errore durante il riscatto della ricompensa: " + e.getMessage());
			e.printStackTrace();
			return false;
		}

	}
	
	
	
	
	
	
	
	
	private boolean isOggi(String dataRiscatto) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	    try {
	        Date data = sdf.parse(dataRiscatto);
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(data);
	        Calendar oggi = Calendar.getInstance();
	        
	        return cal.get(Calendar.YEAR) == oggi.get(Calendar.YEAR) &&
	               cal.get(Calendar.DAY_OF_YEAR) == oggi.get(Calendar.DAY_OF_YEAR);
	    } catch (ParseException e) {
	        logger.severe("Errore nella conversione della data: " + e.getMessage());
	        return false; 
	    }
	}

	public int ottieniPuntiUtente(int idUtente) {

		return utenteDAO.estraiPuntiUtente(idUtente);

	}

	public List<SegnalazioneBean> ottieniSegnalazioniRiscontrate(int idUtente) {
		try {
			List<Segnalazione> segnalazioniRiscontrate = segnalazioneDAO.trovaSegnalazioniRiscontrate(idUtente);

			if (!segnalazioniRiscontrate.isEmpty()) {

				for (Segnalazione s : segnalazioniRiscontrate) {
					String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());

					s.setPosizione(posizioneTesto);
				}

				return conversione.convertSegnalazioneRiscontrataListToBeanList(segnalazioniRiscontrate);

			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
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

	public static RiscattaRicompensaController getInstance() {
		RiscattaRicompensaController result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (RiscattaRicompensaController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RiscattaRicompensaController();
				}

			}
		}

		return result;
	}

//	public Ricompensa convertToEntity(RicompensaBean ricompensaBean) {
//		Ricompensa ricompensa = new Ricompensa();
//
//		ricompensa.setIdRicompensa(ricompensaBean.getIdRicompensa());
//		ricompensa.setIdUtente(ricompensaBean.getIdUtente());
//		ricompensa.setNome(ricompensaBean.getNome());
//		ricompensa.setDescrizione(ricompensaBean.getDescrizione());
//		ricompensa.setValore(ricompensaBean.getValore());
//		ricompensa.setDataScadenza(ricompensaBean.getDataScadenza());
//
//
//		return ricompensa;
//	}

//    private List<RicompensaBean> convertRicompensaListToBeanList(List<Ricompensa> ricompense) {
//        if (ricompense == null) {
//            return new ArrayList<>();
//        }
//
//        List<RicompensaBean> ricompensaBeanList = new ArrayList<>();
//
//        for (Ricompensa r : ricompense) {
//            RicompensaBean ricompensaBean = convertToBean(r);
//            ricompensaBeanList.add(ricompensaBean);
//        }
//
//        return ricompensaBeanList;
//    }
//
//    private RicompensaBean convertToBean(Ricompensa r) {
//        RicompensaBean ricompensaBean = new RicompensaBean();
//
//        ricompensaBean.setIdRicompensa(r.getIdRicompensa());
//        ricompensaBean.setNome(r.getNome());
//        ricompensaBean.setValore(r.getValore());
//        ricompensaBean.setDescrizione(r.getDescrizione());
//        ricompensaBean.setDataScadenza(r.getDataScadenza());
//
//        // impostazione campi opzionali solo se inizializzati
//        if (r.getIdUtente() > 0) {
//            ricompensaBean.setIdUtente(r.getIdUtente());
//        }
//        if (r.getDataRiscatto() != null) {
//            ricompensaBean.setDataRiscatto(r.getDataRiscatto());
//        }
//        if (r.getCodiceRiscatto() != null) {
//            ricompensaBean.setCodiceRiscatto(r.getCodiceRiscatto());
//        }
//        if (r.getPunti() > 0) {
//            ricompensaBean.setPunti(r.getPunti());
//        }
//
//        return ricompensaBean;
//    }

}
