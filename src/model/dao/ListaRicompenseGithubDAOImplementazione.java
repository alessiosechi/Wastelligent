package model.dao;

import model.domain.Ricompensa;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import exceptions.CodiceRiscattoNonTrovatoException;
import exceptions.ConnessioneAPIException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ListaRicompenseGithubDAOImplementazione implements ListaRicompenseGithubDAO {
	private static final String API_URL_REWARDS = "https://raw.githubusercontent.com/alessiosechi/API_Rewards_public/main/rewards.json";
    private static final String API_URL_CODES = "https://raw.githubusercontent.com/alessiosechi/API_Rewards_public/main/codes.json";

	private static volatile ListaRicompenseGithubDAOImplementazione instance;
	private static final Logger logger = Logger.getLogger(ListaRicompenseGithubDAOImplementazione.class.getName());

	public static ListaRicompenseGithubDAOImplementazione getInstance() {
		ListaRicompenseGithubDAOImplementazione result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (ListaRicompenseGithubDAOImplementazione.class) {
				result = instance;
				if (result == null) {
					instance = result = new ListaRicompenseGithubDAOImplementazione();
				}

			}
		}

		return result;
	}

	@Override
	public List<Ricompensa> getRicompense() throws ConnessioneAPIException { // restituisco una lista di oggetti Ricompensa
		List<Ricompensa> ricompense = new ArrayList<>();

		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL_REWARDS)).build();

			// invio la richiesta al server e salvo la risposta
			String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

			// uso Gson per convertire la risposta JSON in oggetti Ricompensa
			//Gson gson = new Gson();
			JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
			JsonArray rewardsArray = jsonObject.getAsJsonArray("rewards");
			

			
			

			// convert
			for (int i = 0; i < rewardsArray.size(); i++) {
				// converto ogni elemento dell'array in un JsonObject che rappresenta una ricompensa
				JsonObject rewardObj = rewardsArray.get(i).getAsJsonObject();

				// converto l'oggetto JSON in un oggetto Ricompensa
				// Ricompensa ricompensa = gson.fromJson(rewardObj, Ricompensa.class);
				// in questo modo, i nomi degli attributi della classe Ricompensa devono corrispondere a quelli nel file json

				int idRicompensa =rewardObj.get("id").getAsInt();
		        String nome = rewardObj.get("nome").getAsString();
		        int valore = rewardObj.get("valore").getAsInt();
		        String descrizione = rewardObj.get("descrizione").getAsString();
		        String dataScadenza = rewardObj.get("dataScadenza").getAsString();
				
				Ricompensa ricompensa = new Ricompensa(idRicompensa, nome, valore, descrizione, dataScadenza); // ho rimosso il coupling tra i nomi
				
				

				// aggiungo la ricompensa alla lista di ricompense
				ricompense.add(ricompensa);
			}
		} catch (IOException e) {
            throw new ConnessioneAPIException("Errore durante la connessione a Github", e);
        } catch (JsonSyntaxException e) {
            throw new ConnessioneAPIException("Errore nel parsing del JSON", e);
        }catch (Exception e) {
            logger.severe("Si è verificato un errore imprevisto: "+ e.getMessage());
		}

		return ricompense;
	}
	
	
	
	
    public String getCodiceRiscatto(int idRicompensa) throws ConnessioneAPIException, CodiceRiscattoNonTrovatoException{
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL_CODES)).build();
            String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray codesArray = jsonObject.getAsJsonArray("codes");

            // Itera sugli oggetti nel JSON array per trovare il codice associato all'idRicompensa
            for (int i = 0; i < codesArray.size(); i++) {
                JsonObject codeObj = codesArray.get(i).getAsJsonObject();
                int id = codeObj.get("id").getAsInt();
                
                if (id == idRicompensa) {
                    return codeObj.get("reward_code").getAsString(); // Restituisce il codice di riscatto
                }
            }
            throw new CodiceRiscattoNonTrovatoException("Codice riscatto non trovato"); 
            
        } catch (IOException e) {
            // Gestione di errori di connessione
            throw new ConnessioneAPIException("Errore durante la connessione a Github", e);
        } catch (JsonSyntaxException e) {
            // Gestione di errori di parsing JSON
            throw new ConnessioneAPIException("Errore nel parsing del JSON", e);
        } catch (Exception e) {
            logger.severe("Si è verificato un errore imprevisto: "+ e.getMessage());
        }
		return null;
    }
	
	
	
	
	

}
