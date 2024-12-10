package logic.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.beans.OperatoreEcologicoBean;
import logic.beans.SegnalazioneBean;
import logic.exceptions.OperatoreNonDisponibileException;
import logic.facade.OperatoreEcologicoCreatorFacade;
import logic.model.dao.CoordinateDao;
import logic.model.dao.DaoFactory;
import logic.model.dao.SegnalazioneDao;
import logic.model.dao.UtenteDao;
import logic.model.domain.SegnalazioniOperatore;
import logic.model.domain.SegnalazioniAttive;
import logic.model.domain.SegnalazioniRisolte;
import logic.model.domain.OperatoreEcologico;
import logic.model.domain.Ruolo;
import logic.model.domain.Segnalazione;
import logic.model.domain.StatoSegnalazione;
import logic.model.domain.Coordinate;
import logic.model.domain.LoggedUser;
import logic.observer.Observer;

public class RisolviSegnalazioneController { 


	private static volatile RisolviSegnalazioneController instance;
	private static final Logger logger = Logger.getLogger(RisolviSegnalazioneController.class.getName());
	private SegnalazioneDao segnalazioneDAO;
	private UtenteDao utenteDao;
	private CoordinateDao coordinateDao;
	
	private RisolviSegnalazioneController() {
		try {
			segnalazioneDAO = DaoFactory.getDao(SegnalazioneDao.class);
			utenteDao=DaoFactory.getDao(UtenteDao.class);
			coordinateDao=DaoFactory.getDao(CoordinateDao.class);
		} catch (Exception e) {
	        logger.severe("Errore durante l'inizializzazione dei DAO: " + e.getMessage());
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
	
	
	
    public void registraOsservatoreSegnalazioniAttive(Observer observer) {
    	SegnalazioniAttive.getInstance().registraOsservatore(observer);
    }
    public void registraOsservatoreSegnalazioniAssegnate(Observer observer) {
    	SegnalazioniOperatore.getInstance().registraOsservatore(observer);
    }
    
    public List<SegnalazioneBean> getSegnalazioniAttive() {   
    	return convertSegnalazioneListToBeanList(SegnalazioniAttive.getInstance().getSegnalazioni());  
    }
    
    public List<SegnalazioneBean> getSegnalazioniAssegnate() {   
    	return convertSegnalazioneListToBeanList(SegnalazioniOperatore.getInstance().getSegnalazioni());
    }
	

    public List<SegnalazioneBean> getSegnalazioniRicevute() { 
     
		try {
	        List<Segnalazione> segnalazioniDaCompletare=segnalazioneDAO.getSegnalazioniByStato(StatoSegnalazione.RICEVUTA.getStato()); 
	        
	        
			if (!segnalazioniDaCompletare.isEmpty()) {
				for (Segnalazione s : segnalazioniDaCompletare) {
					Coordinate coordinate = new Coordinate(s.getLatitudine(), s.getLongitudine());
			        String posizioneTesto = coordinateDao.ottieniPosizione(coordinate);
					s.setPosizione(posizioneTesto);
				}
				
				SegnalazioniAttive.getInstance().setSegnalazioni(segnalazioniDaCompletare);
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
			SegnalazioniAttive.getInstance().rimuoviSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean));
		} catch (Exception e) {
			logger.severe("Errore nell'eliminazione della segnalazione: " + e.getMessage());
		}
	}
	
	public List<OperatoreEcologicoBean> getOperatoriEcologiciDisponibili() {
	    try {
	    	List<OperatoreEcologico> operatoriEcologici=utenteDao.getUtentiByRuolo(Ruolo.OPERATORE_ECOLOGICO);
	        return convertOperatoriEcologiciListToBeanList(operatoriEcologici);

	    } catch (Exception e) {
	        logger.severe("Errore nel recupero degli operatori ecologici: " + e.getMessage());
	        return new ArrayList<>();
	    }
	}

	
	public boolean assegnaOperatore(SegnalazioneBean segnalazioneBean, OperatoreEcologicoBean operatoreEcologicoBean)
			throws OperatoreNonDisponibileException {
		try {
			OperatoreEcologico operatore = OperatoreEcologicoCreatorFacade.caricaOperatore(operatoreEcologicoBean.getIdUtente(), operatoreEcologicoBean.getUsername());
			if (operatore.getSegnalazioni().size() > 5) {
				throw new OperatoreNonDisponibileException("L'operatore ha troppe segnalazioni assegnate. Riprova più tardi.");
			}
			segnalazioneDAO.aggiornaStato(segnalazioneBean.getIdSegnalazione(), StatoSegnalazione.IN_CORSO.getStato());
			segnalazioneDAO.assegnaOperatore(segnalazioneBean.getIdSegnalazione(),operatoreEcologicoBean.getIdUtente());
			SegnalazioniAttive.getInstance().rimuoviSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean));
			return true;
		} catch (OperatoreNonDisponibileException e) {
			throw e;
		} catch (Exception e) {
			return false;
		}
	}
	
	

	
	public List<SegnalazioneBean> getSegnalazioniDaRisolvere() {
		LoggedUser utente = LoggedUser.getInstance();

		try {
			List<Segnalazione> segnalazioniDaRisolvere = segnalazioneDAO.getSegnalazioniAssegnate(utente.getIdUtente());

			if (!segnalazioniDaRisolvere.isEmpty()) {
				for (Segnalazione s : segnalazioniDaRisolvere) {
					Coordinate coordinate = new Coordinate(s.getLatitudine(), s.getLongitudine());
					String posizioneTesto = coordinateDao.ottieniPosizione(coordinate);
					s.setPosizione(posizioneTesto);
				}

				SegnalazioniOperatore.getInstance().setSegnalazioni(segnalazioniDaRisolvere);
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

			SegnalazioniOperatore.getInstance().rimuoviSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean));
			SegnalazioniRisolte.getInstance().aggiungiSegnalazione(convertSegnalazioneBeanToEntity(segnalazioneBean));

			return true;
		} catch (Exception e) {
			logger.severe("Errore nel completamento della segnalazione: " + e.getMessage());
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
