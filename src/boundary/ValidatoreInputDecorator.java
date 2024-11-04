package boundary;

// Decorator
public abstract class ValidatoreInputDecorator implements ValidaInput {
    protected ValidaInput validatoreDecorato;
    
    
    
    
    /*
     * Abstract classes should not have public constructors. 
     * Constructors of abstract classes can only be called in constructors of their subclasses. 
     * So there is no point in making them public. The protected modifier should be enough.
     */

    protected ValidatoreInputDecorator(ValidaInput validatore) {
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
