package logic.controller;

import java.util.List;
import java.util.logging.Logger;

import logic.beans.PosizioneBean;
import logic.beans.SegnalazioneBean;
import logic.exceptions.SegnalazioneVicinaException;
import logic.model.dao.DaoFactory;
import logic.model.dao.SegnalazioneDao;
import logic.model.domain.SegnalazioniAttive;
import logic.model.domain.Posizione;
import logic.model.domain.Segnalazione;
import logic.model.domain.StatoSegnalazione;
import logic.model.domain.UtenteCorrente;

public class EffettuaSegnalazioneController { // NON OK
	
	private static volatile EffettuaSegnalazioneController instance;
	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static final Logger logger = Logger.getLogger(EffettuaSegnalazioneController.class.getName());
	private static SegnalazioneDao segnalazioneDAO;

	private EffettuaSegnalazioneController() {
	}

	public static EffettuaSegnalazioneController getInstance() {
		EffettuaSegnalazioneController result = instance;


		if (instance == null) {
			synchronized (EffettuaSegnalazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new EffettuaSegnalazioneController();	
						
					try {
                        segnalazioneDAO = DaoFactory.getDao(SegnalazioneDao.class);
					} catch (Exception e) {
						logger.severe("Errore durante l'inizializzazione del DAO: " + e.getMessage());
					}
				}
			}
		}

		return result;
	}
	
	
	
	


	// metodo che restituisce le coordinate in base alla posizione fornita
	
	// DEVO USARE UNA BEAN
	public PosizioneBean getCoordinates(String location) {
		Posizione posizione = servizioGeocoding.ottieniCoordinate(location);

		return convertPosizioneToBean(posizione);
	}


	public void inviaSegnalazione(SegnalazioneBean segnalazioneBean) throws SegnalazioneVicinaException{
        Segnalazione segnalazione = convertSegnalazioneToEntity(segnalazioneBean);

        // verifico se esistono segnalazioni nel raggio di 10 metri
        if(verificaSegnalazioniVicine(segnalazione)) {
        	throw new SegnalazioneVicinaException("Segnalazione troppo vicina a un'altra esistente."); 
        }

        // se non ci sono segnalazioni troppo vicine, recupero l' idUtente e salvo la segnalazione
        UtenteCorrente utente=UtenteCorrente.getInstance();
        segnalazione.setIdUtente(utente.getUtente().getIdUtente());


        segnalazioneDAO.salvaSegnalazione(segnalazione);
        SegnalazioniAttive.getInstance().aggiungiSegnalazione(segnalazione); // qui
	}

	
	
	
	
	
	
	
	
	
	private boolean verificaSegnalazioniVicine(Segnalazione segnalazione) {
        List<Segnalazione> segnalazioniEsistenti = segnalazioneDAO.getSegnalazioniByStato(StatoSegnalazione.RICEVUTA.getStato()); 

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
                
            	return true;
            }
        }
        return false;

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
        
        return R * c; // converto l'angolo in metri


    }
	

	public PosizioneBean convertPosizioneToBean(Posizione posizione) {
		PosizioneBean posizioneBean = new PosizioneBean();
		posizioneBean.setLatitudine(posizione.getLatitudine());
		posizioneBean.setLongitudine(posizione.getLongitudine());
		return posizioneBean;
	}
	

	
	public Segnalazione convertSegnalazioneToEntity(SegnalazioneBean segnalazioneBean) {
		Segnalazione segnalazione = new Segnalazione();
		segnalazione.setDescrizione(segnalazioneBean.getDescrizione());
		segnalazione.setFoto(segnalazioneBean.getFoto());
		segnalazione.setStato(StatoSegnalazione.RICEVUTA.getStato()); 
		segnalazione.setIdUtente(segnalazioneBean.getIdUtente());
		segnalazione.setLatitudine(segnalazioneBean.getLatitudine());
		segnalazione.setLongitudine(segnalazioneBean.getLongitudine());
		return segnalazione;
	}

}
