package boundary;

// Decorator
public abstract class ValidatoreInputDecorator implements ValidaInput {
    protected ValidaInput validatoreDecorato;

    public ValidatoreInputDecorator(ValidaInput validatore) {
        this.validatoreDecorato = validatore;
    }

    @Override
    public boolean valida(String input) {
        return validatoreDecorato.valida(input);
    }

    @Override
    public String getMessaggioErrore() {
        return validatoreDecorato.getMessaggioErrore();
    }
}
