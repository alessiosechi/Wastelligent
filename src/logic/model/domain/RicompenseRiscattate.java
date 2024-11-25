package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public class RicompenseRiscattate extends Subject{

    private List<Ricompensa> ricompense = new ArrayList<>();
    private static RicompenseRiscattate instance;


    public static RicompenseRiscattate getInstance() {
        if (instance == null) {
            instance = new RicompenseRiscattate();
        }
        return instance;
    }
    
    
	public void aggiungiRicompensa(Ricompensa ricompensa) {
		ricompense.add(ricompensa);
		notificaOsservatori();
	}
	
    public  void setRicompense(List<Ricompensa> ricompense) {
        this.ricompense=ricompense;
    }
	
    public  List<Ricompensa> getRicompense() {
        return ricompense;
    }
}
