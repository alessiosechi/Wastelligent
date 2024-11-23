package model.dao;

import java.util.HashMap;
import java.util.Map;

import model.domain.EspertoEcologico;
import model.domain.ListaOperatoriEcologici;
import model.domain.OperatoreEcologico;
import model.domain.Utente;
import model.domain.UtenteBase;

public class LoginDaoInMemory implements LoginDao {

    private static final Map<String, Utente> utentiInMemory = new HashMap<>();
    private static final Map<String, String> usernamePasswordMap = new HashMap<>();
    
    private static final Map<String, String> OPERATORI_ECOLOGICI = Map.of(
    	    "luca.bianchi11", "luca.bianchi",
    	    "mario.rossi9", "mariorossi9",
    	    "giovanni.verdi7", "giovanni7"
    	);

    	private static final Map<String, String> ESPERTI_ECOLOGICI = Map.of(
    	    "luca2", "2",
    	    "sara.verdi3", "sara3",
    	    "alessandro.rossi5", "alex5"
    	);

    
    static {
        
        for (Map.Entry<String, String> entry : ESPERTI_ECOLOGICI.entrySet()) {
            String username = entry.getKey();
            String password = entry.getValue();
            EspertoEcologico esperto = new EspertoEcologico(utentiInMemory.size() + 1, username);
            utentiInMemory.put(username, esperto);
            usernamePasswordMap.put(username, password);
        }

        for (Map.Entry<String, String> entry : OPERATORI_ECOLOGICI.entrySet()) {
            String username = entry.getKey();
            String password = entry.getValue();
            OperatoreEcologico operatore = new OperatoreEcologico(utentiInMemory.size() + 1, username);
            ListaOperatoriEcologici.getInstance().aggiungiOperatore(operatore);
            utentiInMemory.put(username, operatore);
            usernamePasswordMap.put(username, password);
        }
    }

    @Override
    public int autenticazione(String username, String password) {    	
        // controllo se l'utente esiste nella mappa e se la password è corretta
        if (usernamePasswordMap.containsKey(username) && usernamePasswordMap.get(username).equals(password)) {
            Utente utente = utentiInMemory.get(username);
            if (utente instanceof UtenteBase) {
                return 1; // Utente base
            } else if (utente instanceof EspertoEcologico) {
                return 2; // Esperto ecologico
            } else if (utente instanceof OperatoreEcologico) {
                return 3; // Operatore ecologico
            }
        }
        return -1; // Username o password non validi
    }

    @Override
    public int getIdByUsername(String username) {

        Utente utente = utentiInMemory.get(username);
        if (utente != null) {
            return utente.getIdUtente(); 
        }
        return -1; 
    }
    

    @Override
    public boolean registraUtente(String username, String password) {
        // controllo se lo username è già stato preso
        if (isUsernameTaken(username)) {
            return false;
        }

        Utente nuovoUtente = new UtenteBase(utentiInMemory.size() + 1, username);
        utentiInMemory.put(username, nuovoUtente);
        usernamePasswordMap.put(username, password);
              
        return true;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return utentiInMemory.containsKey(username);
    }
       
    
    
//    public void stampaUtentiRegistrati() {
//        System.out.println("Utenti registrati nel sistema:");
//        for (Map.Entry<String, Utente> entry : utentiInMemory.entrySet()) {
//            Utente utente = entry.getValue();
//            System.out.printf("ID: %d, Username: %s%n", utente.getIdUtente(), entry.getKey());
//        }
//    }
}

