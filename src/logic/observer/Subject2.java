package logic.observer;

public interface Subject2 {
	void registraOsservatore(Observer observer);
    void rimuoviOsservatore(Observer observer);
	void notificaOsservatori();
}
