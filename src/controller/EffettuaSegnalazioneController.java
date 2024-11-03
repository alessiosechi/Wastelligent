package controller;

import java.util.List;
import java.util.logging.Logger;

import exceptions.SegnalazioneVicinaException;
import model.dao.SegnalazioneDAO;
import model.dao.SegnalazioneDAOImplementazione;
import model.domain.Posizione;
import model.domain.PosizioneBean;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;

public class EffettuaSegnalazioneController {
	private static volatile EffettuaSegnalazioneController instance;
	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();

	private static Conversione conversione = new Conversione();
	private static SegnalazioneDAO segnalazioneDAO;
	private static final Logger logger = Logger.getLogger(EffettuaSegnalazioneController.class.getName());

	private EffettuaSegnalazioneController() {
		try {
			// inizializzazione di segnalazioneDAO
			segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();
		} catch (Exception e) {
			logger.severe("Errore durante l'inizializzazione di segnalazioneDAO: " + e.getMessage());
			throw new RuntimeException("Impossibile inizializzare segnalazioneDAO");
		}
	}

	public static EffettuaSegnalazioneController getInstance() {
		EffettuaSegnalazioneController result = instance;

		if (instance == null) {
			synchronized (EffettuaSegnalazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new EffettuaSegnalazioneController();
				}
			}
		}

		return result;
	}

	// metodo che restituisce le coordinate in base alla posizione fornita
	public PosizioneBean getCoordinates(String location) throws Exception {
		Posizione posizione = servizioGeocoding.ottieniCoordinate(location);


		return conversione.convertToBean(posizione);
	}


	public void inviaSegnalazione(SegnalazioneBean segnalazioneBean) throws SegnalazioneVicinaException{
	    try {

	        Segnalazione segnalazione = conversione.convertToEntity(segnalazioneBean);

	        // verifico se esistono segnalazioni nel raggio di 10 metri
	        verificaSegnalazioneVicino(segnalazione);

	        // se non ci sono segnalazioni troppo vicine, salvo la segnalazione
	        segnalazioneDAO.salvaSegnalazione(segnalazione);



	    } catch (SegnalazioneVicinaException e) {
	        logger.warning("Tentativo di inviare una segnalazione vicina: " + e.getMessage());
	        throw e; // propago l'eccezione SegnalazioneVicinaException al livello superiore


	    } catch (Exception e) {
	        logger.severe("Errore durante l'invio della segnalazione: " + e.getMessage());

	    }
	}

	
	
	
	
	
	private void verificaSegnalazioneVicino(Segnalazione segnalazione) throws SegnalazioneVicinaException{
	    try {

	        List<Segnalazione> segnalazioniEsistenti = segnalazioneDAO.getSegnalazioni();

	        // calcolo la distanza e verifico se è entro 20 metri
	        for (Segnalazione esistente : segnalazioniEsistenti) {
	            double distanza = calcolaDistanza(
	                segnalazione.getLatitudine(),
	                segnalazione.getLongitudine(),
	                esistente.getLatitudine(),
	                esistente.getLongitudine()
	            );

	            double max=20.0;
	            if (distanza <= max) { 
	                throw new SegnalazioneVicinaException("Segnalazione troppo vicina a un'altra esistente."); 

	            }
	        }	    
	    } catch (SegnalazioneVicinaException e) {
	        throw e; // rilancio l'eccezione che ho appena catturato, la passo al livello superiore

	    } catch (Exception e) {
	        logger.severe("Errore durante la verifica delle eventuali segnalazioni vicine: " + e.getMessage());
	        //throw new RuntimeException("Errore imprevisto durante la verifica delle eventuali segnalazioni vicine.", e);
	    }

	}

	
	
	
	
    private double calcolaDistanza(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // raggio della Terra in metri (lo utilizzo per convertire il risultato in metri)
        
        // converto le distanze in radianti
        double latDistance = Math.toRadians(lat2 - lat1); 
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        // a e c utilizzate nella formula dell'emisenoverso
        // a rappresenta una misura intermedia della distanza angolare tra i due punti sulla superficie della Terra
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        // c rappresenta l'angolo centrale tra i due punti rispetto al centro della Terra
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        double distanza = R * c; // converto l'angolo in metri

        return distanza;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	public PosizioneBean convertToBean(Posizione posizione) {
//		PosizioneBean posizioneBean = new PosizioneBean();
//		posizioneBean.setLatitudine(posizione.getLatitudine());
//		posizioneBean.setLongitudine(posizione.getLongitudine());
//		return posizioneBean;
//	}
//	
//	
//	
//	
//	
//	public Segnalazione convertToEntity(SegnalazioneBean segnalazioneBean) {
//		Segnalazione segnalazione = new Segnalazione();
//		segnalazione.setDescrizione(segnalazioneBean.getDescrizione());
//		segnalazione.setFoto(segnalazioneBean.getFoto());
//		segnalazione.setStato("Ricevuta"); 
//		segnalazione.setIdUtente(segnalazioneBean.getIdUtente());
//		segnalazione.setLatitudine(segnalazioneBean.getLatitudine());
//		segnalazione.setLongitudine(segnalazioneBean.getLongitudine());
//		return segnalazione;
//	}

}
