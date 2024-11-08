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
import model.domain.StatoSegnalazione;
import model.domain.UtenteBean;

public class RisolviSegnalazioneController {

	private ServizioGeocoding servizioGeocoding = new ServizioGeocodingAdapter();
	private static volatile RisolviSegnalazioneController instance;
	private static SegnalazioneDAO segnalazioneDAO;
	private static UtenteDAO utenteDAO;
	private static final Logger logger = Logger.getLogger(RisolviSegnalazioneController.class.getName());
	

	
	private RisolviSegnalazioneController() {
	}
	
	
	
	public static RisolviSegnalazioneController getInstance() {
		RisolviSegnalazioneController result = instance;

		if (instance == null) {
			synchronized (RisolviSegnalazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RisolviSegnalazioneController();
					
					try {

						segnalazioneDAO = SegnalazioneDAOImplementazione.getInstance();
						utenteDAO=UtenteDAOImplementazione.getInstance();
					} catch (Exception e) {
				        logger.severe("Errore durante l'inizializzazione di uno dei DAO: " + e.getMessage());
					}
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

				return convertSegnalazioneListToBeanList(segnalazioniDaCompletare);
				
				
				
				
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

				return convertSegnalazioneListToBeanList(segnalazioniAssegnate);
				
				
				
				
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
	        segnalazioneDAO.aggiornaStato(idSegnalazione, StatoSegnalazione.RISOLTA.getStato());

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
    
    
    
    
	
}
