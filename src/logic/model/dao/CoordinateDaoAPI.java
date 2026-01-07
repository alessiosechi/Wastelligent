package logic.model.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import logic.model.domain.Coordinate;

import com.google.gson.JsonArray;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public class CoordinateDaoAPI implements CoordinateDao {
	private static final Logger logger = Logger.getLogger(CoordinateDaoAPI.class.getName());
	private static final String PROPERTIES_FILE = "resources/config-opencage.properties";

	private String caricaChiave() {
		try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
			Properties properties = new Properties();
			properties.load(input);
			return properties.getProperty("OPENCAGE_API_KEY");
		} catch (IOException e) {
			logger.severe("Errore caricamento chiave OpenCage: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Coordinate ottieniCoordinate(String posizioneTesto) {
		String key = caricaChiave();
		if (key == null) return null;

		try {
			// codifico in un formato URL-safe
			String posizioneCodificata = URLEncoder.encode(posizioneTesto, "UTF-8");

			// creo la stringa da usare per la richiesta HTTP
			String urlStringa = String.format("https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s",
					posizioneCodificata, key);

			URI uri = new URI(urlStringa);
			URL url = uri.toURL();

			// apro la connessione HTTP
			HttpURLConnection connessione = (HttpURLConnection) url.openConnection();
			connessione.setRequestMethod("GET");

			try (InputStreamReader lettore = new InputStreamReader(connessione.getInputStream());
					Scanner scanner = new Scanner(lettore)) {

				StringBuilder risposta = new StringBuilder();
				while (scanner.hasNextLine()) {
					risposta.append(scanner.nextLine());
				}

				// converto la risposta in un oggetto JsonObject
				Gson gson = new Gson();
				JsonObject json = gson.fromJson(risposta.toString(), JsonObject.class);
				JsonArray risultati = json.getAsJsonArray("results");

				if (!risultati.isEmpty()) {
					// estraggo i valori lat e lng dalla sezione geometry della risposta JSON
					JsonObject geometria = risultati.get(0).getAsJsonObject().getAsJsonObject("geometry");
					double latitudine = geometria.get("lat").getAsDouble();
					double longitudine = geometria.get("lng").getAsDouble();
					return new Coordinate(latitudine, longitudine);
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			logger.severe("Errore durante il recupero delle coordinate: " + e.getMessage());
			return null;
		}
	}

	@Override
	public String ottieniPosizione(Coordinate coordinate) {
		String key = caricaChiave();
		if (key == null) return null;

		try {
			String urlStringa = String.format("https://api.opencagedata.com/geocode/v1/json?q=%f,+%f&key=%s",
					coordinate.getLatitudine(), coordinate.getLongitudine(), key);

			URI uri = new URI(urlStringa);
			URL url = uri.toURL();

			HttpURLConnection connessione = (HttpURLConnection) url.openConnection();
			connessione.setRequestMethod("GET");

			try (InputStreamReader lettore = new InputStreamReader(connessione.getInputStream());
					Scanner scanner = new Scanner(lettore)) {

				StringBuilder risposta = new StringBuilder();
				while (scanner.hasNextLine()) {
					risposta.append(scanner.nextLine());
				}

				Gson gson = new Gson();
				JsonObject json = gson.fromJson(risposta.toString(), JsonObject.class);
				JsonArray risultati = json.getAsJsonArray("results");

				if (!risultati.isEmpty()) {
					JsonObject primoRisultato = risultati.get(0).getAsJsonObject();
					return primoRisultato.get("formatted").getAsString();
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			logger.severe("Errore durante il recupero dell'indirizzo: " + e.getMessage());
			return null;
		}
	}

}
