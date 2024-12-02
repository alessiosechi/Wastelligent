package logic.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.beans.SegnalazioneBean;
import logic.model.dao.DaoFactory;
import logic.model.dao.SegnalazioneDao;
import logic.model.dao.UtenteBaseDao;
import logic.model.domain.SegnalazioniRisolte;
import logic.model.domain.Segnalazione;
import logic.model.domain.StatoSegnalazione;
import logic.observer.Observer;

public class AssegnaPuntiController { // OK

	private static volatile AssegnaPuntiController instance;
	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static final Logger logger = Logger.getLogger(AssegnaPuntiController.class.getName());
	private static SegnalazioneDao segnalazioneDAO;
	private static UtenteBaseDao utenteBaseDAO;

	
	private AssegnaPuntiController() {
	}
	

    
	
	public static AssegnaPuntiController getInstance() {
		AssegnaPuntiController result = instance;

		if (instance == null) {
			synchronized (AssegnaPuntiController.class) {
				result = instance;
				if (result == null) {
					instance = result = new AssegnaPuntiController();
					
					try {
						segnalazioneDAO = DaoFactory.getDao(SegnalazioneDao.class);
						utenteBaseDAO = DaoFactory.getDao(UtenteBaseDao.class);
					} catch (Exception e) {
				        logger.severe("Errore durante l'inizializzazione dei DAO: " + e.getMessage());
					}
				}

			}
		}

		return result;
	}
	
    public void registraOsservatoreSegnalazioniRisolte(Observer observer) {
    	SegnalazioniRisolte.getInstance().registraOsservatore(observer);
    }
    public List<SegnalazioneBean> getSegnalazioniRisolte() {   
    	return convertSegnalazioneListToBeanList(SegnalazioniRisolte.getInstance().getSegnalazioni());  
    }
	
    public List<SegnalazioneBean> getSegnalazioniDaRiscontrate() {       
		try {
	        List<Segnalazione> segnalazioniDaRiscontrare=segnalazioneDAO.getSegnalazioniByStato(StatoSegnalazione.RISOLTA.getStato());

			if (!segnalazioniDaRiscontrare.isEmpty()) {
				for (Segnalazione s : segnalazioniDaRiscontrare) {
			        String posizioneTesto = servizioGeocoding.ottieniPosizione(s.getLatitudine(), s.getLongitudine());

					s.setPosizione(posizioneTesto);
				}
				
		        SegnalazioniRisolte.getInstance().setSegnalazioni(segnalazioniDaRiscontrare);
				return convertSegnalazioneListToBeanList(segnalazioniDaRiscontrare);
			} else {
				return new ArrayList<>();
			}

		} catch (Exception e) {
			return new ArrayList<>();
		}
        

    }
	
	public boolean assegnaPunti(SegnalazioneBean segnalazioneBean) {
	    try {
	        int puntiAssegnati = segnalazioneBean.getPuntiAssegnati();
	              
	        segnalazioneDAO.assegnaPunti(segnalazioneBean.getIdSegnalazione(), puntiAssegnati);
	        utenteBaseDAO.aggiungiPunti(segnalazioneBean.getIdUtente(), puntiAssegnati);
	        
	        SegnalazioniRisolte.getInstance().rimuoviSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean)); 

	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}	
	
    private Segnalazione convertSegnalazioneBeanToEntity(SegnalazioneBean s) {
        Segnalazione segnalazione = new Segnalazione();

		segnalazione.setDescrizione(s.getDescrizione());
		segnalazione.setFoto(s.getFoto());
		segnalazione.setLatitudine(s.getLatitudine());
		segnalazione.setLongitudine(s.getLongitudine());
		segnalazione.setPuntiAssegnati(s.getPuntiAssegnati());
		segnalazione.setPosizione(s.getPosizione());
		segnalazione.setStato(s.getStato());
		segnalazione.setIdSegnalazione(s.getIdSegnalazione());
		segnalazione.setIdUtente(s.getIdUtente());

        return segnalazione;
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

        return segnalazioneBean;
    }


}
