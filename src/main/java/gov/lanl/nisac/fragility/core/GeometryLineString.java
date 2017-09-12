package gov.lanl.nisac.fragility.core;

import java.util.ArrayList;

public class GeometryLineString implements GeometryObject {

    private ArrayList<double[]> coordinates;
    private String identifier;

    /**
     * Constructor
     * @param id LineString identifier
     */
    public GeometryLineString(String id) {
        this.identifier = id;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setCoordinates(ArrayList<double[]> latLons) {
        this.coordinates = latLons;
    }

    @Override
    public ArrayList<double[]> getCoordinates() {
        return this.coordinates;
    }
}
