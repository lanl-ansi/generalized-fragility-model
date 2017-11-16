package gov.lanl.micot.application.fragility.core;

import java.util.HashMap;
import java.util.List;

public class GeometryMultiPoint implements GeometryObject {

    private List<double[]> coordinates;
    private String identifier;
    private HashMap<Integer, double[]> exposureValues;

    public GeometryMultiPoint(String id){
        this.identifier = id;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setCoordinates(List<double[]> latLons) {
        this.coordinates = latLons;
    }

    @Override
    public List<double[]> getCoordinates() {
        return this.coordinates;
    }
}
