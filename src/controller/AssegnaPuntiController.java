package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.dao.SegnalazioneDAO;
import model.dao.SegnalazioneDAOImplementazione;
import model.dao.UtenteDAO;
import model.dao.UtenteDAOImplementazione;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;

public class AssegnaPuntiController {

	
	
	private static volatile AssegnaPuntiController instance;
	private static SegnalazioneDAO segnalazioneDAO;
	private static UtenteDAO utenteDAO;
	private static Conversione conversione = new Conversione();
	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static final Logger logger = Logger.getLogger(AssegnaPuntiController.class.getName());
	
	
	private AssegnaPuntiController() {
		try {
			segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();
			utenteDAO = UtenteDAOImplementazione.getInstance();
		} catch (Exception e) {
	        logger.severe("Errore durante l'inizializzazione di uno dei DAO: " + e.getMessage());
			throw new RuntimeException("Inizializzazione fallita");
		}
	}
	

	
	public static AssegnaPuntiController getInstance() {
		AssegnaPuntiController result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (AssegnaPuntiController.class) {
				result = instance;
				if (result == null) {
					instance = result = new AssegnaPuntiController();
				}

			}
		}

		return result;
	}
	
    public List<SegnalazioneBean> getSegnalazioniRisolte() {

        
		try {
	    	String stato="Risolta";
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
	
	public boolean assegnaPunti(SegnalazioneBean segnalazioneBean) {
	    try {

	        int idSegnalazione = segnalazioneBean.getIdSegnalazione();
	        int puntiAssegnati = segnalazioneBean.getPuntiAssegnati();
	        int idUtente=segnalazioneBean.getIdUtente();
	        
	        
	        
	        
	        segnalazioneDAO.assegnaPunti(idSegnalazione, puntiAssegnati);
	        utenteDAO.aggiungiPuntiUtente(idUtente, puntiAssegnati);

	        
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}	
	
	
	
	

	
	
}
