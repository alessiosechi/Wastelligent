package logic.config;



public class PersistenceConfigurator {

    private static PersistenceProvider currentProvider;
    private PersistenceConfigurator() {
    	// Costruttore vuoto
    }

    public static void configurePersistence(PersistenceProvider provider) {
        currentProvider = provider;
    }

    public static PersistenceProvider getCurrentProvider() {
        return currentProvider;
    }


}
