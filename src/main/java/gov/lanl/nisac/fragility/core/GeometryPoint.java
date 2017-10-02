package gov.lanl.nisac.fragility.core;

import java.util.ArrayList;

public class GeometryPoint implements GeometryObject {

    // geoJSON POINT coordinates
    private ArrayList<double[]> coordinates;
    private String identifier;
    private double exposureValue;

    public GeometryPoint(String id) {
        this.identifier = id;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
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
