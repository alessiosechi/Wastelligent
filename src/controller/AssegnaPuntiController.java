package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.observer.Observer;
import model.dao.DaoFactory;
import model.dao.SegnalazioneDao;
import model.dao.UtenteDao;
import model.domain.ListaSegnalazioniRisolte;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;
import model.domain.StatoSegnalazione;

public class AssegnaPuntiController {

	
	
	private static volatile AssegnaPuntiController instance;
	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static final Logger logger = Logger.getLogger(AssegnaPuntiController.class.getName());
	private static SegnalazioneDao segnalazioneDAO;
	private static UtenteDao utenteDAO;

	
	private AssegnaPuntiController() {
	}
    public List<SegnalazioneBean> getSegnalazioniRisolte() {   
    	return convertSegnalazioneListToBeanList(ListaSegnalazioniRisolte.getInstance().getSegnalazioniRisolte());  
    }
    
  
    public void registraOsservatoreSegnalazioniRisolte(Observer observer) {
    	ListaSegnalazioniRisolte.getInstance().registraOsservatore(observer);
    }

	
	public static AssegnaPuntiController getInstance() {
		AssegnaPuntiController result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (AssegnaPuntiController.class) {
				result = instance;
				if (result == null) {
					instance = result = new AssegnaPuntiController();
					
					try {
						segnalazioneDAO = DaoFactory.getDao(SegnalazioneDao.class);
						utenteDAO = DaoFactory.getDao(UtenteDao.class);
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
	        utenteDAO.aggiungiPuntiUtente(segnalazioneBean.getIdUtente(), puntiAssegnati);

	        return true;
	    } catch (Exception e) {
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
