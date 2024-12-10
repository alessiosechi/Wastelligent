package logic.model.dao;


import logic.model.domain.Coordinate;

public interface CoordinateDao {
    Coordinate ottieniCoordinate(String posizioneTesto);
    String ottieniPosizione(Coordinate coordinate);
}

