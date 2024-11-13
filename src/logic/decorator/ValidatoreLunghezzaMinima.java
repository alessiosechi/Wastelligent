package logic.decorator;

// ConcreteDecoratorA
public class ValidatoreLunghezzaMinima extends ValidatoreInputDecorator {
    private int lunghezzaMinima;
    private String messaggioErrore;

    public ValidatoreLunghezzaMinima(ValidaInput validatore, int lunghezzaMinima) {
        super(validatore);
        this.lunghezzaMinima = lunghezzaMinima;
    }

    @Override
    public boolean valida(String input) {
        // prima valida con il decoratore precedente
        if (!super.valida(input)) {
            return false;
        }
        
        // controllo se l'input rispetta la lunghezza minima
        if (input.length() < lunghezzaMinima) {
            messaggioErrore = "Il campo deve contenere almeno " + lunghezzaMinima + " caratteri.";
            return false;
        }
        return true;
    }

    @Override
    public String getMessaggioErrore() {
        return messaggioErrore != null ? messaggioErrore : super.getMessaggioErrore();
    }
}
