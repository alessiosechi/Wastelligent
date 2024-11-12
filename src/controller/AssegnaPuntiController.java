package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.observer.Observer;
import model.dao.SegnalazioneDAO;
import model.dao.SegnalazioneDAOImplementazione;
import model.dao.UtenteDAO;
import model.dao.UtenteDAOImplementazione;
import model.domain.ListaSegnalazioniRisolte;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;
import model.domain.StatoSegnalazione;

public class AssegnaPuntiController {

	
	
	private static volatile AssegnaPuntiController instance;
	private static SegnalazioneDAO segnalazioneDAO;
	private static UtenteDAO utenteDAO;
	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static final Logger logger = Logger.getLogger(AssegnaPuntiController.class.getName());
	private static ListaSegnalazioniRisolte segnalazioniRisolte;
	
	private AssegnaPuntiController() {
	}
    public List<SegnalazioneBean> getSegnalazioniRisolte() {   
    	return convertSegnalazioneListToBeanList(segnalazioniRisolte.getSegnalazioniRisolte());  
    }
    
    
    public void registraOsservatoreSegnalazioniRisolte(Observer observer) {
    	segnalazioniRisolte.registraOsservatore(observer);
    }

	
	public static AssegnaPuntiController getInstance() {
		AssegnaPuntiController result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (AssegnaPuntiController.class) {
				result = instance;
				if (result == null) {
					instance = result = new AssegnaPuntiController();
					segnalazioniRisolte=ListaSegnalazioniRisolte.getInstance();
					try {
						segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();
						utenteDAO = UtenteDAOImplementazione.getInstance();
					} catch (Exception e) {
				        logger.severe("Errore durante l'inizializzazione dei DAO: " + e.getMessage());
					}
				}

			}
		}

		return result;
	}
	
    public List<SegnalazioneBean> getSegnalazioniDaRiscontrare() {

        
		try {
	        List<Segnalazione> segnalazioniDaRiscontrare=segnalazioneDAO.getSegnalazioniByStato(StatoSegnalazione.RISOLTA.getStato());

			if (!segnalazioniDaRiscontrare.isEmpty()) {
				for (Segnalazione s : segnalazioniDaRiscontrare) {
			        String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());

					s.setPosizione(posizioneTesto);
				}
				segnalazioniRisolte.setSegnalazioniRisolte(segnalazioniDaRiscontrare);
				return convertSegnalazioneListToBeanList(segnalazioniDaRiscontrare);
				
				
				
				
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
	        
	        
	        segnalazioniRisolte.rimuoviSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean));
	        segnalazioneDAO.assegnaPunti(idSegnalazione, puntiAssegnati);
	        utenteDAO.aggiungiPuntiUtente(idUtente, puntiAssegnati);
       
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
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
