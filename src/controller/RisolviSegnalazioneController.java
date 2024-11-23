package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.observer.Observer;
import model.dao.DaoFactory;
import model.dao.SegnalazioneDao;
import model.dao.UtenteDao;
import model.domain.AssegnazioneBean;
import model.domain.ListaSegnalazioniAssegnate;
import model.domain.ListaSegnalazioniAttive;
import model.domain.OperatoreEcologico;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;
import model.domain.StatoSegnalazione;
import model.domain.OperatoreEcologicoBean;
import model.domain.UtenteCorrente;

public class RisolviSegnalazioneController { // QUASI OK

	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static volatile RisolviSegnalazioneController instance;
	private static final Logger logger = Logger.getLogger(RisolviSegnalazioneController.class.getName());
	private static SegnalazioneDao segnalazioneDAO;
	private static UtenteDao utenteDAO;
	
	private RisolviSegnalazioneController() {
	}
	
	
	public static RisolviSegnalazioneController getInstance() {
		RisolviSegnalazioneController result = instance;

		if (instance == null) {
			synchronized (RisolviSegnalazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RisolviSegnalazioneController();

					try {
						segnalazioneDAO = DaoFactory.getDao(SegnalazioneDao.class);
						utenteDAO=DaoFactory.getDao(UtenteDao.class);
					} catch (Exception e) {
				        logger.severe("Errore durante l'inizializzazione dei DAO: " + e.getMessage());
					}
					
					
				}
			}
		}

		return result;
	}
	
    public void registraOsservatoreSegnalazioniAttive(Observer observer) {
    	ListaSegnalazioniAttive.getInstance().registraOsservatore(observer);
    }
    public void registraOsservatoreSegnalazioniAssegnate(Observer observer) {
    	ListaSegnalazioniAssegnate.getInstance().registraOsservatore(observer);
    }
    
    public List<SegnalazioneBean> getSegnalazioniAttive() {   
    	return convertSegnalazioneListToBeanList(ListaSegnalazioniAttive.getInstance().getSegnalazioniAttive());  
    }
    
    public List<SegnalazioneBean> getSegnalazioniAssegnate() {   
        UtenteCorrente utente = UtenteCorrente.getInstance();
        int idOperatore = utente.getUtente().getIdUtente();

    	return convertSegnalazioneListToBeanList(ListaSegnalazioniAssegnate.getInstance().getSegnalazioniPerOperatore(idOperatore));  
    }
	

    public List<SegnalazioneBean> getSegnalazioniRicevute() { 
     
		try {
	        List<Segnalazione> segnalazioniDaCompletare=segnalazioneDAO.getSegnalazioniByStato(StatoSegnalazione.RICEVUTA.getStato()); 
	        
	        
			if (!segnalazioniDaCompletare.isEmpty()) {
				for (Segnalazione s : segnalazioniDaCompletare) {
			        String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());
					s.setPosizione(posizioneTesto);
				}
				
			
				return convertSegnalazioneListToBeanList(segnalazioniDaCompletare);
				
	
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			return new ArrayList<>();
		}
        
    
        
    }



	public void eliminaSegnalazione(SegnalazioneBean segnalazioneBean) {	
		try {
			
			segnalazioneDAO.eliminaSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean)); 		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<OperatoreEcologicoBean> getOperatoriEcologiciDisponibili() {
	    try {

	        List<OperatoreEcologico> operatoriEcologici = utenteDAO.estraiOperatoriEcologici();
	        return convertOperatoriEcologiciListToBeanList(operatoriEcologici);

	    } catch (Exception e) {
	        logger.severe("Errore nel recupero degli operatori ecologici: " + e.getMessage());
	        return new ArrayList<>();
	    }
	}

	public boolean assegnaOperatore(AssegnazioneBean assegnazioneBean) {
	    try {
	    	

	    	int idSegnalazione = assegnazioneBean.getSegnalazione().getIdSegnalazione();
	    	int idOperatore=assegnazioneBean.getOperatore().getIdUtente();
	    	int idEsperto=UtenteCorrente.getInstance().getUtente().getIdUtente();

	    		 
	        segnalazioneDAO.assegnaOperatore(idSegnalazione, idOperatore, idEsperto); // CAMBIARE: idEsperto non serve a nulla qui
	        
	        
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	

	
    public List<SegnalazioneBean> getSegnalazioniDaRisolvere() {

    	UtenteCorrente utente=UtenteCorrente.getInstance();
    	   
		try {
	        List<Segnalazione> segnalazioniDaRisolvere=segnalazioneDAO.getSegnalazioniAssegnate(utente.getUtente().getIdUtente(), StatoSegnalazione.IN_CORSO.getStato());

			if (!segnalazioniDaRisolvere.isEmpty()) {
				for (Segnalazione s : segnalazioniDaRisolvere) {
			        String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());
					s.setPosizione(posizioneTesto);
				}
				return convertSegnalazioneListToBeanList(segnalazioniDaRisolvere);	
			} else {
				return new ArrayList<>();
			}
		} catch (Exception e) {
			return new ArrayList<>();
		}
    }
	
	
	
	
	

    public boolean completaSegnalazione(SegnalazioneBean segnalazioneBean) {
	    try {
	        segnalazioneDAO.aggiornaStato(segnalazioneBean.getIdSegnalazione(), StatoSegnalazione.RISOLTA.getStato());     
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
    }
    

    
	public List<OperatoreEcologicoBean> convertOperatoriEcologiciListToBeanList(List<OperatoreEcologico> operatoriEcologici) {
		if (operatoriEcologici != null) {
			List<OperatoreEcologicoBean> operatoriBeanList = new ArrayList<>();

			for (OperatoreEcologico operatore : operatoriEcologici) {
				OperatoreEcologicoBean operatoreEcologicoBean = new OperatoreEcologicoBean();
				operatoreEcologicoBean.setIdUtente(operatore.getIdUtente());
				operatoreEcologicoBean.setUsername(operatore.getUsername());
				operatoriBeanList.add(operatoreEcologicoBean);
			}

			return operatoriBeanList;
		} else {
			return new ArrayList<>();
		}
	}
	
	
	
	
	private List<SegnalazioneBean> convertSegnalazioneListToBeanList(List<Segnalazione> segnalazioni) {

		
        if (segnalazioni == null) {
            return new ArrayList<>();
        }

		List<SegnalazioneBean> segnalazioneBeanList = new ArrayList<>();

        for (Segnalazione s : segnalazioni) {
            SegnalazioneBean segnalazioneBean = convertSegnalazioneToBean(s);
            segnalazioneBeanList.add(segnalazioneBean);
        }

        return segnalazioneBeanList;
	}
	
	
	
    private SegnalazioneBean convertSegnalazioneToBean(Segnalazione s) {
        SegnalazioneBean segnalazioneBean = new SegnalazioneBean();

		segnalazioneBean.setDescrizione(s.getDescrizione());
		segnalazioneBean.setFoto(s.getFoto());
		segnalazioneBean.setIdUtente(s.getIdUtente());
		segnalazioneBean.setLatitudine(s.getLatitudine());
		segnalazioneBean.setLongitudine(s.getLongitudine());
		segnalazioneBean.setPuntiAssegnati(s.getPuntiAssegnati());
		segnalazioneBean.setPosizione(s.getPosizione());
		segnalazioneBean.setStato(s.getStato());
		segnalazioneBean.setIdSegnalazione(s.getIdSegnalazione());
		
		segnalazioneBean.setIdOperatore(s.getIdOperatore());

        return segnalazioneBean;
    }
    
    private Segnalazione convertSegnalazioneBeanToEntity(SegnalazioneBean s) {
        Segnalazione segnalazione = new Segnalazione();

		segnalazione.setDescrizione(s.getDescrizione());
		segnalazione.setFoto(s.getFoto());
		segnalazione.setIdUtente(s.getIdUtente());
		segnalazione.setLatitudine(s.getLatitudine());
		segnalazione.setLongitudine(s.getLongitudine());
		segnalazione.setPuntiAssegnati(s.getPuntiAssegnati());
		segnalazione.setPosizione(s.getPosizione());
		segnalazione.setStato(s.getStato());
		segnalazione.setIdSegnalazione(s.getIdSegnalazione());

        return segnalazione;
    }
    
    
	
}
