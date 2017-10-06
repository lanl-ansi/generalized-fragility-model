package gov.lanl.nisac.fragility.core;

import java.util.List;

public class GeometryPoint implements GeometryObject {

    // geoJSON POINT coordinates
    private List<double[]> coordinates;
    private String identifier;

    public GeometryPoint(String id) {
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
