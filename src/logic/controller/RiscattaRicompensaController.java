package logic.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import logic.beans.RicompensaBean;
import logic.beans.SegnalazioneBean;
import logic.exceptions.CodiceRiscattoNonTrovatoException;
import logic.exceptions.ConnessioneAPIException;
import logic.exceptions.DailyRedemptionLimitException;
import logic.exceptions.GestioneRiscattoException;
import logic.exceptions.InsufficientPointsException;
import logic.model.dao.DaoFactory;
import logic.model.dao.ListaRicompenseDao;
import logic.model.dao.RicompensaDao;
import logic.model.dao.SegnalazioneDao;
import logic.model.dao.UtenteBaseDao;
import logic.model.domain.Ricompensa;
import logic.model.domain.RicompenseRiscattate;
import logic.model.domain.Segnalazione;
import logic.model.domain.UtenteCorrente;
import logic.observer.Observer;

public class RiscattaRicompensaController { // OK

	private static volatile RiscattaRicompensaController instance;
	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static final Logger logger = Logger.getLogger(RiscattaRicompensaController.class.getName());
    private static int idUtente;
    
	private static ListaRicompenseDao listaRicompenseDAO;
	private static RicompensaDao ricompensaDAO;
	private static UtenteBaseDao utenteBaseDAO;
	private static SegnalazioneDao segnalazioneDAO;


	private RiscattaRicompensaController() {
	}
	
	public static RiscattaRicompensaController getInstance() {
		RiscattaRicompensaController result = instance;

		if (instance == null) {
			synchronized (RiscattaRicompensaController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RiscattaRicompensaController();
				
	                try {
	                    listaRicompenseDAO = DaoFactory.getDao(ListaRicompenseDao.class);      
	                    ricompensaDAO = DaoFactory.getDao(RicompensaDao.class);
	                    utenteBaseDAO = DaoFactory.getDao(UtenteBaseDao.class);
	                    segnalazioneDAO = DaoFactory.getDao(SegnalazioneDao.class);
	                    

	                    idUtente=UtenteCorrente.getInstance().getUtente().getIdUtente();
	                } catch (Exception e) {
	                    logger.severe("Errore durante l'inizializzazione: " + e.getMessage());
	                }
				}

			}
		}
		return result;
	}
	
	
    public void registraOsservatoreRicompenseRiscattate(Observer observer) {
    	RicompenseRiscattate.getInstance().registraOsservatore(observer);
    }
    public List<RicompensaBean> getRicompenseRiscattate() {   
    	return convertRicompensaListToBeanList(RicompenseRiscattate.getInstance().getRicompense());  
    }

	public List<RicompensaBean> ottieniRicompenseAPI() throws ConnessioneAPIException {
		try {
			List<Ricompensa> ricompenseAPI = listaRicompenseDAO.getRicompense();

			if (!ricompenseAPI.isEmpty()) {
				return convertRicompensaListToBeanList(ricompenseAPI);
			} else {
				return new ArrayList<>();
			}

		} catch (ConnessioneAPIException e) {
			// qui rilancio sostituendo il messaggio con uno più generico e comprendibile per l'utente
			throw new ConnessioneAPIException("Si è verificato un errore durante la connessione al server.",e);
		}catch (Exception e) {
			return new ArrayList<>();
		}
	}
	
	
	public int ottieniPuntiUtente() {
		return utenteBaseDAO.estraiPunti(idUtente);
	}
	

	public List<RicompensaBean> ottieniRicompenseUtente() {
		try {
	        List<Ricompensa> ricompenseUtente = ricompensaDAO.getRicompenseByUtente(idUtente);

			if (!ricompenseUtente.isEmpty()) {
				
				RicompenseRiscattate.getInstance().setRicompense(ricompenseUtente);
				return convertRicompensaListToBeanList(ricompenseUtente);
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			return new ArrayList<>();
		}
	}


	public boolean riscatta(RicompensaBean ricompensaBean) throws DailyRedemptionLimitException, InsufficientPointsException, GestioneRiscattoException{

		
		try {
			Ricompensa ricompensa = convertRicompensaToEntity(ricompensaBean);
			
	        verificaLimiteGiornaliero();
	        verificaPuntiSufficienti(ricompensa);

	        String codiceRiscatto = ottieniCodiceRiscatto(ricompensa.getIdRicompensa());

			ricompensa.setCodiceRiscatto(codiceRiscatto);
			ricompensa.setIdUtente(idUtente);
	        
			// imposto la data attuale
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = sdf.format(today);
			ricompensa.setDataRiscatto(formattedDate);
			
			utenteBaseDAO.sottraiPunti(idUtente, ricompensa.getPunti());
			ricompensaDAO.registraRicompensaRiscattata(ricompensa);
			
			RicompenseRiscattate.getInstance().aggiungiRicompensa(ricompensa);
			return true; // riscatto riuscito

		}catch (DailyRedemptionLimitException | InsufficientPointsException | GestioneRiscattoException e) {
		    throw e; 
		} catch (Exception e) {
	        logger.severe("Errore durante il riscatto della ricompensa: " + e.getMessage());
			return false;
		}

	}
	
	

	private void verificaLimiteGiornaliero() throws DailyRedemptionLimitException {
	    int numeroRiscattiOggi = (int) ricompensaDAO.getRicompenseByUtente(idUtente)
	        .stream()
	        .filter(r -> isOggi(r.getDataRiscatto()))
	        .count();

	    if (numeroRiscattiOggi >= 5) {
	        throw new DailyRedemptionLimitException("Hai raggiunto il limite giornaliero di riscatti.");
	    }
	}
	private void verificaPuntiSufficienti(Ricompensa ricompensa) throws InsufficientPointsException {
	    int puntiUtente = ottieniPuntiUtente();
	    int puntiNecessari = calcolaPuntiNecessari(ricompensa.getValore());

	    if (puntiUtente < puntiNecessari) {
	        throw new InsufficientPointsException("Punti insufficienti per riscattare la ricompensa.");
	    }

	    ricompensa.setPunti(puntiNecessari);
	}

	private static String ottieniCodiceRiscatto(int idRicompensa) throws GestioneRiscattoException  {
	    try {
	        return listaRicompenseDAO.getCodiceRiscatto(idRicompensa);   
	    } catch (ConnessioneAPIException e) {
	    	// messaggio che deve essere mostrato all'utente (nascondo l'errore interno)
            throw new GestioneRiscattoException("Impossibile connettersi per ottenere il codice di riscatto. Riprova più tardi.", e);
        } catch (CodiceRiscattoNonTrovatoException e) {
            throw new GestioneRiscattoException("Nessun codice di riscatto trovato per la ricompensa selezionata.", e);
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

	public List<SegnalazioneBean> ottieniSegnalazioniRiscontrate() {
		try {
			List<Segnalazione> segnalazioniUtente = segnalazioneDAO.getSegnalazioniRiscontrate(idUtente);

			if (!segnalazioniUtente.isEmpty()) {

				for (Segnalazione s : segnalazioniUtente) {
					String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());

					s.setPosizione(posizioneTesto);
				}
				
				return convertSegnalazioneRiscontrataListToBeanList(segnalazioniUtente);

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

	private Ricompensa convertRicompensaToEntity(RicompensaBean ricompensaBean) {
		Ricompensa ricompensa = new Ricompensa();

		ricompensa.setIdRicompensa(ricompensaBean.getIdRicompensa());
		ricompensa.setIdUtente(ricompensaBean.getIdUtente());
		ricompensa.setNome(ricompensaBean.getNome());
		ricompensa.setDescrizione(ricompensaBean.getDescrizione());
		ricompensa.setValore(ricompensaBean.getValore());
		ricompensa.setDataScadenza(ricompensaBean.getDataScadenza());


		return ricompensa;
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


        if (r.getIdUtente() > 0) {
            ricompensaBean.setIdUtente(r.getIdUtente());
        }
        if (r.getDataRiscatto() != null) {
            ricompensaBean.setDataRiscatto(r.getDataRiscatto());
        }
        if (r.getCodiceRiscatto() != null) {
            ricompensaBean.setCodiceRiscatto(r.getCodiceRiscatto());
        }
        if (r.getPunti() > 0) {
            ricompensaBean.setPunti(r.getPunti());
        }

        return ricompensaBean;
    }

    
    
    
    
	private List<SegnalazioneBean> convertSegnalazioneRiscontrataListToBeanList(List<Segnalazione> segnalazioniRiscontrate) {
	
        if (segnalazioniRiscontrate == null) {
            return new ArrayList<>();
        }

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
		
		
		// i seguenti si possono levare
		segnalazioneBean.setIdUtente(s.getIdUtente());
		segnalazioneBean.setStato(s.getStato());
		segnalazioneBean.setIdSegnalazione(s.getIdSegnalazione());

        return segnalazioneBean;
    }
}
