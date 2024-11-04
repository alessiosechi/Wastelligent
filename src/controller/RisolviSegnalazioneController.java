package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.dao.SegnalazioneDAO;
import model.dao.SegnalazioneDAOImplementazione;
import model.dao.UtenteDAO;
import model.dao.UtenteDAOImplementazione;
import model.domain.AssegnazioneBean;
import model.domain.OperatoreEcologico;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;
import model.domain.UtenteBean;

public class RisolviSegnalazioneController {

	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static volatile RisolviSegnalazioneController instance;
	private static Conversione conversione = new Conversione();
	private static SegnalazioneDAO segnalazioneDAO;
	private static UtenteDAO utenteDAO;
	private static final Logger logger = Logger.getLogger(RisolviSegnalazioneController.class.getName());
	

	
	private RisolviSegnalazioneController() {
		try {

			segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();
			utenteDAO=UtenteDAOImplementazione.getInstance();
		} catch (Exception e) {
	        logger.severe("Errore durante l'inizializzazione di uno dei DAO: " + e.getMessage());
			throw new RuntimeException("Inizializzazione fallita");
		}
	}
	
	
	
	public static RisolviSegnalazioneController getInstance() {
		RisolviSegnalazioneController result = instance;

		if (instance == null) {
			synchronized (RisolviSegnalazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RisolviSegnalazioneController();
				}
			}
		}

		return result;
	}
	

    public List<SegnalazioneBean> getSegnalazioniRicevute() {

        
		try {
	    	String stato="Ricevuta";
	        List<Segnalazione> segnalazioniDaCompletare=segnalazioneDAO.getSegnalazioniByStato(stato);

			if (!segnalazioniDaCompletare.isEmpty()) {
				for (Segnalazione s : segnalazioniDaCompletare) {
			        String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());

					s.setPosizione(posizioneTesto);
				}

				return conversione.convertSegnalazioneRiscontrataListToBeanList(segnalazioniDaCompletare);
				
				
				
				
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
        
        
        
        
    }



	public void eliminaSegnalazione(int idSegnalazione) {
		
		try {
			

			segnalazioneDAO.eliminaSegnalazione(idSegnalazione);
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public List<UtenteBean> getOperatoriEcologiciDisponibili() {
	    try {

	        List<OperatoreEcologico> operatoriEcologici = utenteDAO.estraiOperatoriEcologiciDisponibili();

	        return conversione.convertOperatoriEcologiciListToBeanList(operatoriEcologici);

	    } catch (Exception e) {
	        logger.severe("Errore nel recupero degli operatori ecologici: " + e.getMessage());
	        return new ArrayList<>();
	    }
	}

	public boolean assegnaOperatore(AssegnazioneBean assegnazioneBean) {
	    try {

	    	int idSegnalazione = assegnazioneBean.getSegnalazione().getIdSegnalazione();
	    	int idOperatore=assegnazioneBean.getOperatore().getIdUtente();
	    	int idEsperto=assegnazioneBean.getEsperto().getIdUtente();
	    	
	        segnalazioneDAO.assegnaOperatore(idSegnalazione, idOperatore, idEsperto);
	        
	        
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	

	
    public List<SegnalazioneBean> getSegnalazioniAssegnate(int idOperatore) {

        
		try {

			String stato="In corso";

	        List<Segnalazione> segnalazioniAssegnate=segnalazioneDAO.getSegnalazioniAssegnate(idOperatore, stato);

			if (!segnalazioniAssegnate.isEmpty()) {
				for (Segnalazione s : segnalazioniAssegnate) {
			        String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());

					s.setPosizione(posizioneTesto);
				}

				return conversione.convertSegnalazioneRiscontrataListToBeanList(segnalazioniAssegnate);
				
				
				
				
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
        
        
        
        
    }
	
	
	
	
	

    public boolean completaSegnalazione(int idSegnalazione) {
	    try {
	        segnalazioneDAO.aggiornaStato(idSegnalazione);

	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
    }
    
    
    
	public List<UtenteBean> convertOperatoriEcologiciListToBeanList(List<OperatoreEcologico> operatoriEcologici) {
		if (operatoriEcologici != null) {
			List<UtenteBean> operatoriBeanList = new ArrayList<>();

			for (OperatoreEcologico operatore : operatoriEcologici) {
				UtenteBean utenteBean = new UtenteBean();
				utenteBean.setIdUtente(operatore.getIdUtente());
				utenteBean.setUsername(operatore.getUsername());
				operatoriBeanList.add(utenteBean);
			}

			return operatoriBeanList;
		} else {
			return new ArrayList<>();
		}
	}
    
	
}
