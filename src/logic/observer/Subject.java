package logic.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

	
	
    protected List<Observer> osservatori = new ArrayList<>();
	
	
	
	
	public void registraOsservatore(Observer observer) {
		osservatori.add(observer);
		
	}


	public void rimuoviOsservatore(Observer observer) {
		   int i = osservatori.indexOf(observer);
	        if (i >= 0) {
	        	osservatori.remove(i);
	        }
		
	}


	public void notificaOsservatori() { // metodo che notifica gli osservatori quando cambia lo stato del Subject
        for (Observer observer : osservatori) {
            observer.update();
            System.out.println("Observer:"+observer);
        }
		
	}
	
}
