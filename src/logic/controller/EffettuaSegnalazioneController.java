package logic.controller;

import java.util.List;
import java.util.logging.Logger;

import logic.beans.CoordinateBean;
import logic.beans.LocationRequestBean;
import logic.beans.SegnalazioneBean;
import logic.exceptions.SegnalazioneVicinaException;
import logic.model.dao.CoordinateDao;
import logic.model.dao.DaoFactory;
import logic.model.dao.SegnalazioneDao;
import logic.model.domain.Coordinate;
import logic.model.domain.Segnalazione;
import logic.model.domain.SegnalazioniAttive;
import logic.model.domain.StatoSegnalazione;
import logic.model.domain.LoggedUser;

public class EffettuaSegnalazioneController {
	private static EffettuaSegnalazioneController instance = null;
	private static final Logger logger = Logger.getLogger(EffettuaSegnalazioneController.class.getName());
	private SegnalazioneDao segnalazioneDAO;
	private CoordinateDao coordinateDao;

	private EffettuaSegnalazioneController() {
		try {
			segnalazioneDAO = DaoFactory.getDao(SegnalazioneDao.class);
			coordinateDao = DaoFactory.getDao(CoordinateDao.class);
		} catch (Exception e) {
			logger.severe("Errore durante l'inizializzazione del DAO: " + e.getMessage());
		}
	}

	public static EffettuaSegnalazioneController getInstance() {
		if (instance == null)
			instance = new EffettuaSegnalazioneController();

		return instance;
	}

	// metodo che restituisce le coordinate in base alla posizione fornita
	public CoordinateBean getCoordinates(LocationRequestBean locationRequestBean) {
		Coordinate posizione = coordinateDao.ottieniCoordinate(locationRequestBean.getLocation());

		return convertCoordinateToBean(posizione);
	}

	public void inviaSegnalazione(SegnalazioneBean segnalazioneBean) throws SegnalazioneVicinaException {
		// verifico se esistono segnalazioni nel raggio di 10 metri
		if (verificaSegnalazioniVicine(segnalazioneBean.getLatitudine(), segnalazioneBean.getLongitudine())) {
			throw new SegnalazioneVicinaException("Segnalazione troppo vicina a un'altra esistente.");
		}

		Segnalazione segnalazione = compilaSegnalazione(segnalazioneBean);
		segnalazioneDAO.salvaSegnalazione(segnalazione);
		SegnalazioniAttive.getInstance().aggiungiSegnalazione(segnalazione);

	}

	public CoordinateBean convertCoordinateToBean(Coordinate coordinate) {
		CoordinateBean coordinateBean = new CoordinateBean();
		coordinateBean.setLatitudine(coordinate.getLatitudine());
		coordinateBean.setLongitudine(coordinate.getLongitudine());
		return coordinateBean;
	}

	private boolean verificaSegnalazioniVicine(double latitudine, double longitudine) {
		List<Segnalazione> segnalazioniEsistenti = segnalazioneDAO
				.getSegnalazioniByStato(StatoSegnalazione.RICEVUTA.getStato());

		// calcolo la distanza e verifico se è entro 20 metri
		for (Segnalazione esistente : segnalazioniEsistenti) {
			double distanza = calcolaDistanza(latitudine, longitudine, esistente.getLatitudine(),
					esistente.getLongitudine());

			double max = 20.0;
			if (distanza <= max) {

				return true;
			}
		}
		return false;

	}

	private double calcolaDistanza(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371000; // raggio della Terra in metri (lo utilizzo per convertire il risultato in
								// metri)

		// converto le distanze in radianti
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		// a e c utilizzate nella formula dell'emisenoverso
		// a rappresenta una misura intermedia della distanza angolare tra i due punti
		// sulla superficie della Terra
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		// c rappresenta l'angolo centrale tra i due punti rispetto al centro della
		// Terra
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c; // converto l'angolo in metri

	}

	public Segnalazione compilaSegnalazione(SegnalazioneBean segnalazioneBean) {
		Segnalazione segnalazione = new Segnalazione();
		LoggedUser utente = LoggedUser.getInstance();
		segnalazione.setIdUtente(utente.getIdUtente());
		segnalazione.setDescrizione(segnalazioneBean.getDescrizione());
		segnalazione.setFoto(segnalazioneBean.getFoto());
		segnalazione.setStato(StatoSegnalazione.RICEVUTA.getStato());
		segnalazione.setLatitudine(segnalazioneBean.getLatitudine());
		segnalazione.setLongitudine(segnalazioneBean.getLongitudine());
		return segnalazione;
	}

}
