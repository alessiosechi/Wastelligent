package boundary;


public class ControllerGraficoFactory {

    public Object createController(ViewInfo viewInfo) {
        try {
            // Restituisce l'istanza singleton del controller tramite il metodo getInstance()
            return viewInfo.getControllerClass().getMethod("getInstance").invoke(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Errore nella creazione del controller per: " + viewInfo, e);
        }
    }
}

