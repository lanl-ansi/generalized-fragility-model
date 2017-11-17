package gov.lanl.micot.application.fragility.core;

import java.util.HashMap;
import java.util.List;

/**
 * This class provides the user with methods to control GeoJSON line strings.
 *
 * @author Trevor Crawford
 */
public class GeometryLineString implements GeometryObject {

    private List<double[]> coordinates;
    private String identifier;
    private HashMap<Integer, double[]> exposureValues;

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
    public void setCoordinates(List<double[]> latLons) {
        this.coordinates = latLons;
    }

    @Override
    public List<double[]> getCoordinates() {
        return this.coordinates;
    }
}
