package controller;


import model.domain.Posizione;

public interface ServizioGeocoding {
    Posizione ottieniCoordinate(String posizioneTesto);
    String ottieniPosizione(double latitudine, double longitudine);
}

