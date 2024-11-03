package controller;

import java.util.ArrayList;
import java.util.List;

import model.domain.OperatoreEcologico;
import model.domain.Posizione;
import model.domain.PosizioneBean;
import model.domain.Ricompensa;
import model.domain.RicompensaBean;
import model.domain.Segnalazione;
import model.domain.SegnalazioneBean;

import model.domain.Utente;
import model.domain.UtenteBean;

// Converte oggetti Bean in oggetti non Bean e viceversa

public class Conversione {

	// EffettuaSegnalazioneController

	public UtenteBean convertToBean(Utente utente) {
		UtenteBean utenteBean = new UtenteBean();
		utenteBean.setIdUtente(utente.getIdUtente());
		utenteBean.setUsername(utente.getUsername());
		return utenteBean;
	}

	public PosizioneBean convertToBean(Posizione posizione) {
		PosizioneBean posizioneBean = new PosizioneBean();
		posizioneBean.setLatitudine(posizione.getLatitudine());
		posizioneBean.setLongitudine(posizione.getLongitudine());
		return posizioneBean;
	}

	public Segnalazione convertToEntity(SegnalazioneBean segnalazioneBean) {
		Segnalazione segnalazione = new Segnalazione();
		segnalazione.setDescrizione(segnalazioneBean.getDescrizione());
		segnalazione.setFoto(segnalazioneBean.getFoto());
		segnalazione.setStato("Ricevuta"); // da modificare in seguito
		segnalazione.setIdUtente(segnalazioneBean.getIdUtente());
		segnalazione.setLatitudine(segnalazioneBean.getLatitudine());
		segnalazione.setLongitudine(segnalazioneBean.getLongitudine());
		return segnalazione;
	}

	// RiscattaRicompensaController


	public List<RicompensaBean> convertRicompensaListToBeanList(List<Ricompensa> ricompense) {

		if (ricompense != null) {

			List<RicompensaBean> ricompensaBeanList = new ArrayList<>();

			// itero su ogni oggetto Ricompensa e lo converto in un oggetto RicompensaBean
			for (Ricompensa r : ricompense) {

				RicompensaBean ricompensaBean = new RicompensaBean();

				ricompensaBean.setIdRicompensa(r.getIdRicompensa());
				ricompensaBean.setNome(r.getNome());
				ricompensaBean.setValore(r.getValore());
				ricompensaBean.setDescrizione(r.getDescrizione());
				ricompensaBean.setDataScadenza(r.getDataScadenza());

				/*
				 * i campi idUtente, dataRiscatto e puntiUtilizzati sono presenti solo nelle
				 * ricompense riscattate e dato che questo metodo viene utilizzato sia per le
				 * ricompense disponibili dall'api sia per quelle riscattate dal db, verifico se
				 * i suddetti campi sono inizializzati, altrimenti farei assegnazioni inutili
				 * 
				 */

				if (r.getIdUtente() > 0) {
					ricompensaBean.setIdUtente(r.getIdUtente());
				}

				if (r.getDataRiscatto() != null) {
					ricompensaBean.setDataRiscatto(r.getDataRiscatto());
				}
				if (r.getCodiceRiscatto() != null) {
					ricompensaBean.setCodiceRiscatto(r.getCodiceRiscatto());
				}
				if (r.getPunti() > 0) {
					ricompensaBean.setPunti(r.getPunti());
				}

				ricompensaBeanList.add(ricompensaBean);
			}

			return ricompensaBeanList;

		} else {
			return new ArrayList<>();
		}

	}

	public Ricompensa convertToEntity(RicompensaBean ricompensaBean) {
		Ricompensa ricompensa = new Ricompensa();

		ricompensa.setIdUtente(ricompensaBean.getIdUtente());
		ricompensa.setNome(ricompensaBean.getNome());
		ricompensa.setValore(ricompensaBean.getValore());
		ricompensa.setDescrizione(ricompensaBean.getDescrizione());
		ricompensa.setDataScadenza(ricompensaBean.getDataScadenza());
		ricompensa.setIdRicompensa(ricompensaBean.getIdRicompensa());

		return ricompensa;
	}



	public List<UtenteBean> convertOperatoriEcologiciListToBeanList(List<OperatoreEcologico> operatoriEcologici) {
		if (operatoriEcologici != null) {
			List<UtenteBean> operatoriBeanList = new ArrayList<>();

			for (OperatoreEcologico operatore : operatoriEcologici) {
				UtenteBean bean = new UtenteBean();
				bean.setIdUtente(operatore.getIdUtente());
				bean.setUsername(operatore.getUsername());
				operatoriBeanList.add(bean);
			}

			return operatoriBeanList;
		} else {
			return new ArrayList<>();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<SegnalazioneBean> convertSegnalazioneRiscontrataListToBeanList(
			List<Segnalazione> segnalazioniRiscontrate) {
		if (segnalazioniRiscontrate != null) {
			List<SegnalazioneBean> segnalazioneBeanList = new ArrayList<>();

			// Itero su ogni oggetto SegnalazioneRiscontrata e lo converto in un oggetto
			// SegnalazioneRiscontrataBean
			for (Segnalazione sr : segnalazioniRiscontrate) {
				SegnalazioneBean segnalazioneBean = new SegnalazioneBean();

				// Imposta i valori nel bean

				segnalazioneBean.setDescrizione(sr.getDescrizione());
				segnalazioneBean.setFoto(sr.getFoto());
				segnalazioneBean.setIdUtente(sr.getIdUtente());
				segnalazioneBean.setLatitudine(sr.getLatitudine());
				segnalazioneBean.setLongitudine(sr.getLongitudine());
				segnalazioneBean.setPuntiAssegnati(sr.getPuntiAssegnati());
				segnalazioneBean.setPosizione(sr.getPosizione());
				segnalazioneBean.setStato(sr.getStato());
				segnalazioneBean.setIdSegnalazione(sr.getIdSegnalazione());

				segnalazioneBeanList.add(segnalazioneBean);
			}

			return segnalazioneBeanList;
		} else {
			return new ArrayList<>();
		}
	}

}
