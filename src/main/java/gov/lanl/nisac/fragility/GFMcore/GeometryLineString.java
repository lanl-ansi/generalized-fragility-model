package gov.lanl.nisac.fragility.GFMcore;

import java.util.ArrayList;
import java.util.List;

public class GeometryLineString implements GeometryObject {

    private ArrayList<double[]> coordinates;
    private String identifier;
    private GFMEngine broker;

    public GeometryLineString(String id, GFMEngine broker) {
        this.identifier = id;
        this.broker = broker;
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
