package logic.decorator;

//ConcreteDecoratorB
public class ValidatoreSpaziVuoti extends ValidatoreInputDecorator {

    private String messaggioErrore;

    public ValidatoreSpaziVuoti(ValidaInput validatore) {
        super(validatore);
    }

    @Override
    public boolean valida(String input) {
        // prima valida con il decoratore precedente
        if (!super.valida(input)) {
            return false;
        }

        // controllo se l'input contiene spazi
        if (input.contains(" ")) {
            messaggioErrore = "L'input non può contenere spazi vuoti.";
            return false;
        }

        return true;
    }

    @Override
    public String getMessaggioErrore() {
        return messaggioErrore != null ? messaggioErrore : super.getMessaggioErrore();
    }
}
