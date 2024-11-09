package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.domain.Posizione;

import com.google.gson.JsonArray;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.logging.Logger;

public class ServizioGeocodingAdapter implements ServizioGeocoding {
    private static final String CHIAVE_API_GEOCODING = "eeaf6def78f849edb3ea062981a4af9a"; // Chiave API
	private static final Logger logger = Logger.getLogger(ServizioGeocodingAdapter.class.getName());

    @Override
    public Posizione ottieniCoordinate(String posizioneTesto) {
        try {
            // Codifica la query
            String posizioneCodificata = URLEncoder.encode(posizioneTesto, "UTF-8");

            // Costruisci l'URL come stringa
            String urlStringa = String.format("https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s", posizioneCodificata, CHIAVE_API_GEOCODING);

            URI uri = new URI(urlStringa);
            URL url = uri.toURL();

            // Apertura della connessione e lettura della risposta
            HttpURLConnection connessione = (HttpURLConnection) url.openConnection();
            connessione.setRequestMethod("GET");

            try (InputStreamReader lettore = new InputStreamReader(connessione.getInputStream());
                 Scanner scanner = new Scanner(lettore)) {

                StringBuilder risposta = new StringBuilder();
                while (scanner.hasNextLine()) {
                    risposta.append(scanner.nextLine());
                }

                // Analisi della risposta
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(risposta.toString(), JsonObject.class);
                JsonArray risultati = json.getAsJsonArray("results");

                if (!risultati.isEmpty()) {
                    JsonObject geometria = risultati.get(0).getAsJsonObject().getAsJsonObject("geometry");
                    double latitudine = geometria.get("lat").getAsDouble();
                    double longitudine = geometria.get("lng").getAsDouble();
                    return new Posizione(latitudine, longitudine);
                } else {
                    // In caso di risultato vuoto
                    return null;
                }
            }
        } catch (Exception e) {
            logger.severe("Errore durante il recupero delle coordinate: " + e.getMessage());
            return null;  
        }
    }
    
    @Override
    public String ottieniPosizione(double latitudine, double longitudine) {
        try {
            // Costruisci l'URL come stringa
            String urlStringa = String.format("https://api.opencagedata.com/geocode/v1/json?q=%f,+%f&key=%s", latitudine, longitudine, CHIAVE_API_GEOCODING);

            URI uri = new URI(urlStringa);
            URL url = uri.toURL();

            // Apertura della connessione e lettura della risposta
            HttpURLConnection connessione = (HttpURLConnection) url.openConnection();
            connessione.setRequestMethod("GET");

            try (InputStreamReader lettore = new InputStreamReader(connessione.getInputStream());
                 Scanner scanner = new Scanner(lettore)) {

                StringBuilder risposta = new StringBuilder();
                while (scanner.hasNextLine()) {
                    risposta.append(scanner.nextLine());
                }

                // Analisi della risposta
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(risposta.toString(), JsonObject.class);
                JsonArray risultati = json.getAsJsonArray("results");

                if (!risultati.isEmpty()) {
                    // Ottieni l'indirizzo formattato dal primo risultato
                    JsonObject primoRisultato = risultati.get(0).getAsJsonObject();
                    return primoRisultato.get("formatted").getAsString(); // Restituisce l'indirizzo formattato
                } else {
                    return "null"; 
                }
            }
        } catch (Exception e) {
            logger.severe("Errore durante il recupero dell'indirizzo: " + e.getMessage());
            return "null"; 
        }
    }

    
    


}
