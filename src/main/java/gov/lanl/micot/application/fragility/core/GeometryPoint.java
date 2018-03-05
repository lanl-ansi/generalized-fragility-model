package gov.lanl.micot.application.fragility.core;

import java.util.List;

/**
 * This class provides the user with methods to control GeoJSON Points.
 *
 * @author Trevor Crawford
 */
public class GeometryPoint implements GeometryObject {

    // GeoJSON POINT coordinates
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
