package logic.config;

public enum PersistenceProvider {
    IN_MEMORY("In Memory"),
    DATABASE("Database");

    private final String description;

    PersistenceProvider(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
