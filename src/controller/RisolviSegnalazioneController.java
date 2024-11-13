package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.observer.Observer;
import model.dao.SegnalazioneDAO;
import model.dao.SegnalazioneDAOImplementazione;
import model.dao.UtenteDAO;
import model.dao.UtenteDAOImplementazione;
import model.domain.AssegnazioneBean;
import model.domain.ListaSegnalazioniAssegnate;
import model.domain.ListaSegnalazioniAttive;
import model.domain.OperatoreEcologico;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;
import model.domain.StatoSegnalazione;
import model.domain.OperatoreEcologicoBean;
import model.domain.UtenteCorrente;

public class RisolviSegnalazioneController {

	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static volatile RisolviSegnalazioneController instance;
	private static SegnalazioneDAO segnalazioneDAO;
	private static UtenteDAO utenteDAO;
	private static final Logger logger = Logger.getLogger(RisolviSegnalazioneController.class.getName());
	private static ListaSegnalazioniAttive segnalazioniAttive;
	private static ListaSegnalazioniAssegnate segnalazioniAssegnate;
	
	private RisolviSegnalazioneController() {
	}
	
	
	
	public static RisolviSegnalazioneController getInstance() {
		RisolviSegnalazioneController result = instance;

		if (instance == null) {
			synchronized (RisolviSegnalazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RisolviSegnalazioneController();
					segnalazioniAttive=ListaSegnalazioniAttive.getInstance();
					segnalazioniAssegnate=ListaSegnalazioniAssegnate.getInstance();

					try {

						segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();
						utenteDAO=UtenteDAOImplementazione.getInstance();
					} catch (Exception e) {
				        logger.severe("Errore durante l'inizializzazione dei DAO: " + e.getMessage());
					}
					
					
				}
			}
		}

		return result;
	}
	
    public void registraOsservatoreSegnalazioniAttive(Observer observer) {
    	segnalazioniAttive.registraOsservatore(observer);
    }
    public void registraOsservatoreSegnalazioniAssegnate(Observer observer) {
    	segnalazioniAssegnate.registraOsservatore(observer);
    }
    
    public List<SegnalazioneBean> getSegnalazioniAttive() {   
    	return convertSegnalazioneListToBeanList(segnalazioniAttive.getSegnalazioniAttive());  
    }
    public List<SegnalazioneBean> getSegnalazioniAssegnate() {   
    	return convertSegnalazioneListToBeanList(segnalazioniAssegnate.getSegnalazioniAssegnate());  
    }
	

    public List<SegnalazioneBean> getSegnalazioniRicevute() {

        
		try {
	        List<Segnalazione> segnalazioniDaCompletare=segnalazioneDAO.getSegnalazioniByStato(StatoSegnalazione.RICEVUTA.getStato());
	        
	        

			if (!segnalazioniDaCompletare.isEmpty()) {
				for (Segnalazione s : segnalazioniDaCompletare) {
			        String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());
					s.setPosizione(posizioneTesto);
				}
				
				
				segnalazioniAttive.setSegnalazioniAttive(segnalazioniDaCompletare);
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
			
			segnalazioneDAO.eliminaSegnalazione(segnalazioneBean.getIdSegnalazione());
			segnalazioniAttive.rimuoviSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean));
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public List<OperatoreEcologicoBean> getOperatoriEcologiciDisponibili() {
	    try {

	        List<OperatoreEcologico> operatoriEcologici = utenteDAO.estraiOperatoriEcologiciDisponibili();
	        
	        /*
	         * Come gestisco questo per la versione DEMO? Potrei fare una cosa del genere:
	         * 		private List<OperatoreEcologico> operatoriEcologici = new ArrayList<>();
	         *      operatoriEcologici.add(new OperatoreEcologico("op1", "Mario Rossi", "mario@wastelligent.com"));
	         *      operatoriEcologici.add(new OperatoreEcologico("op2", "Luca Bianchi", "luca@wastelligent.com"));
	         */

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
	    	UtenteCorrente utente=UtenteCorrente.getInstance();
	    	
	    	Segnalazione s = convertSegnalazioneBeanToEntity(assegnazioneBean.getSegnalazione());
	    	segnalazioniAttive.rimuoviSegnalazione(s);
	    	
	        segnalazioneDAO.assegnaOperatore(idSegnalazione, idOperatore, utente.getUtente().getIdUtente());
	        
	        
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
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

				segnalazioniAssegnate.setSegnalazioniAssegnate(segnalazioniDaRisolvere);
				return convertSegnalazioneListToBeanList(segnalazioniDaRisolvere);
				
				
				
				
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
        
        
        
        
    }
	
	
	
	
	

    public boolean completaSegnalazione(SegnalazioneBean segnalazioneBean) {
	    try {
	        segnalazioneDAO.aggiornaStato(segnalazioneBean.getIdSegnalazione(), StatoSegnalazione.RISOLTA.getStato());
	        segnalazioniAssegnate.rimuoviSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean));

	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
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
    
    private Segnalazione convertSegnalazioneBeanToEntity(SegnalazioneBean s) {
        Segnalazione segnalazione = new Segnalazione();
        // verificare se servono tutti
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
