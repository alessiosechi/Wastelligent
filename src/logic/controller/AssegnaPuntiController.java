package logic.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.beans.SegnalazioneBean;
import logic.model.dao.CoordinateDao;
import logic.model.dao.DaoFactory;
import logic.model.dao.SegnalazioneDao;
import logic.model.dao.UtenteBaseDao;
import logic.model.domain.SegnalazioniRisolte;
import logic.model.domain.Coordinate;
import logic.model.domain.Segnalazione;
import logic.model.domain.StatoSegnalazione;
import logic.observer.Observer;

public class AssegnaPuntiController{ 

	private static volatile AssegnaPuntiController instance;
	private static final Logger logger = Logger.getLogger(AssegnaPuntiController.class.getName());
	private SegnalazioneDao segnalazioneDao;
	private UtenteBaseDao utenteBaseDao;
	private CoordinateDao coordinateDao;
	
	private AssegnaPuntiController() {
	    try {
	        segnalazioneDao = DaoFactory.getDao(SegnalazioneDao.class);
	        utenteBaseDao = DaoFactory.getDao(UtenteBaseDao.class);
			coordinateDao=DaoFactory.getDao(CoordinateDao.class);
	    } catch (Exception e) {
	        logger.severe("Errore durante l'inizializzazione dei Dao: " + e.getMessage());
	    }
	}

	public static AssegnaPuntiController getInstance() {
		AssegnaPuntiController result = instance;

		if (instance == null) {
			synchronized (AssegnaPuntiController.class) {
				result = instance;
				if (result == null) {
					instance = result = new AssegnaPuntiController();
					
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
	        List<Segnalazione> segnalazioniDaRiscontrare=segnalazioneDao.getSegnalazioniByStato(StatoSegnalazione.RISOLTA.getStato());

			if (!segnalazioniDaRiscontrare.isEmpty()) {
				for (Segnalazione s : segnalazioniDaRiscontrare) {
					Coordinate coordinate = new Coordinate(s.getLatitudine(), s.getLongitudine());
			        String posizioneTesto = coordinateDao.ottieniPosizione(coordinate);

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
	        segnalazioneDao.aggiornaStato(segnalazioneBean.getIdSegnalazione(), StatoSegnalazione.RISCONTRATA.getStato());
	        segnalazioneDao.assegnaPunti(segnalazioneBean.getIdSegnalazione(), segnalazioneBean.getPuntiAssegnati());
	        utenteBaseDao.aggiungiPunti(segnalazioneBean.getIdUtente(), segnalazioneBean.getPuntiAssegnati());
	        
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
