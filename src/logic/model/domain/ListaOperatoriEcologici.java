package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

public class ListaOperatoriEcologici {

    private List<OperatoreEcologico> operatoriEcologici= new ArrayList<>();
    private static ListaOperatoriEcologici instance;


    public static ListaOperatoriEcologici getInstance() {
        if (instance == null) {
            instance = new ListaOperatoriEcologici();
        }
        return instance;
    }
    
    
	public void aggiungiOperatore(OperatoreEcologico operatore) {
		operatoriEcologici.add(operatore);
	}

    public  List<OperatoreEcologico> getOperatoriEcologici() {
        return operatoriEcologici;
    }
}
