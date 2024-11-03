package controller;


import model.domain.Posizione;

public interface ServizioGeocoding {
    Posizione ottieniCoordinate(String posizioneTesto) throws Exception;
    String ottieniPosizione(double latitudine, double longitudine) throws Exception;
}

