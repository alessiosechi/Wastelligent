package logic.decorator;

// Component




/*
 * Login: metto popup per dire mancano username o password, eccezioni password/nome utente errati
 * Regitsrazione: metto il decorator
 */
public interface ValidaInput {
    boolean valida(String input);
    String getMessaggioErrore();
}


