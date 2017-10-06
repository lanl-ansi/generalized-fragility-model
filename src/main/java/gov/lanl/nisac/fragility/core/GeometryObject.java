package gov.lanl.nisac.fragility.core;

import java.util.List;

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

