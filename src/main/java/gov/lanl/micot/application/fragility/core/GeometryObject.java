package gov.lanl.micot.application.fragility.core;

import java.util.List;

/**
 * The user of this interface has control over GeometryObject operations.
 *
 * @author Trevor Crawford
 */
public interface GeometryObject {

    /**
     *
     * @return identifier
     */
    String getIdentifier();

    /**
     * @param lonLat longitude and latitiude coordinates
     */
    void setCoordinates(List<double[]> lonLat);

    /**
     *
     * @return returns a list of coordinates that defines a Geometry object
     */
    List<double[]> getCoordinates();

}

