package boundary;

// ConcreteComponent
public class ValidatoreBase implements ValidaInput {
    private String messaggioErrore;

    @Override
    public boolean valida(String input) {
        if (input == null || input.trim().isEmpty()) {
            messaggioErrore = "Il campo non può essere vuoto.";
            return false;
        }
        return true;
    }

    @Override
    public String getMessaggioErrore() {
        return messaggioErrore;
    }
}
