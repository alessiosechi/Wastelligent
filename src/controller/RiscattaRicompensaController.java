package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import exceptions.CodiceRiscattoNonTrovatoException;
import exceptions.ConnessioneAPIException;
import exceptions.DailyRedemptionLimitException;
import exceptions.GestioneRiscattoException;
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

	
	
	// i DAO sono stateless, li dichiaro statici
	private static ListaRicompenseGithubDAO listaRicompenseDAO;
	private static RicompensaDAO ricompensaDAO;
	private static UtenteDAO utenteDAO;
	private static SegnalazioneDAO segnalazioneDAO;
	
	private static volatile RiscattaRicompensaController instance;
	private static final Logger logger = Logger.getLogger(RiscattaRicompensaController.class.getName());
    private List<Ricompensa> ricompenseUtente = new ArrayList<>();

	private RiscattaRicompensaController() {
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

	public List<RicompensaBean> ottieniRicompenseUtente(int idUtente) {
		try {

	        ricompenseUtente = ricompensaDAO.getRicompenseByUtente(idUtente);

			if (!ricompenseUtente.isEmpty()) {
				return convertRicompensaListToBeanList(ricompenseUtente);
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public boolean riscatta(RicompensaBean ricompensaBean) throws DailyRedemptionLimitException, InsufficientPointsException, GestioneRiscattoException{

		try {
			Ricompensa ricompensa = convertRicompensaToEntity(ricompensaBean);

	        // controllo il numero di riscatti effettuati oggi
	        int numeroRiscattiOggi = 0;
	        for (Ricompensa r : ricompenseUtente) {

	            if (isOggi(r.getDataRiscatto())) {
	                numeroRiscattiOggi++;
	            }
	        }
	        
	        if (numeroRiscattiOggi >= 5) {
	            throw new DailyRedemptionLimitException("Hai raggiunto il limite giornaliero di riscatti.");
	        }
			
			
			int idUtente = ricompensa.getIdUtente();
			int puntiUtente = ottieniPuntiUtente(idUtente);
			int puntiNecessari = calcolaPuntiNecessari(ricompensa.getValore());
			
	        if (puntiUtente < puntiNecessari) {
	            throw new InsufficientPointsException("Punti insufficienti per riscattare la ricompensa.");
	        }
	        
			ricompensa.setPunti(puntiNecessari);
	        String codiceRiscatto = ottieniCodiceRiscatto(ricompensa.getIdRicompensa());
			ricompensa.setCodiceRiscatto(codiceRiscatto);

	
			// queste due operazioni dovrebbero essere transazionali
			// registro il riscatto
			ricompensaDAO.registraRicompensaRiscattata(ricompensa);

			// sottraggo i punti all'utente
			utenteDAO.sottraiPuntiUtente(idUtente, puntiNecessari);

			return true; // riscatto riuscito

		}catch (DailyRedemptionLimitException | InsufficientPointsException | GestioneRiscattoException e) {
		    throw e; 
		} catch (Exception e) {
	        logger.severe("Errore durante il riscatto della ricompensa: " + e.getMessage());
			return false;
		}

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

				return convertSegnalazioneRiscontrataListToBeanList(segnalazioniRiscontrate);

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
					
					
					// inizializzo i DAO solo una volta, quando viene creata l'istanza di RiscattaRicompensaController
	                try {
	                    listaRicompenseDAO = ListaRicompenseGithubDAOImplementazione.getInstance();
	                    ricompensaDAO = RicompensaDAOImplementazione.getInstance();
	                    utenteDAO = UtenteDAOImplementazione.getInstance();
	                    segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();
	                } catch (Exception e) {
	                    // Gestione dell'errore durante l'inizializzazione
	                    logger.severe("Errore durante l'inizializzazione dei DAO: " + e.getMessage());
	                }
				}

			}
		}

		return result;
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

        // impostazione campi opzionali solo se inizializzati
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
        // verificare se servono tutti
		segnalazioneBean.setDescrizione(s.getDescrizione());
		segnalazioneBean.setFoto(s.getFoto());
		segnalazioneBean.setIdUtente(s.getIdUtente());
		segnalazioneBean.setLatitudine(s.getLatitudine());
		segnalazioneBean.setLongitudine(s.getLongitudine());
		segnalazioneBean.setPuntiAssegnati(s.getPuntiAssegnati());
		segnalazioneBean.setPosizione(s.getPosizione());
		segnalazioneBean.setStato(s.getStato());
		segnalazioneBean.setIdSegnalazione(s.getIdSegnalazione());

        return segnalazioneBean;
    }
}
