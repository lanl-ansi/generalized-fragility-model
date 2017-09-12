package gov.lanl.nisac.fragility.core;

import java.util.ArrayList;

public interface GeometryObject {

    /**
     *
     * @return identifier
     */
    String getIdentifier();

    /**
     * @param lonLat longitude and latitiude coordinates
     */
    void setCoordinates(ArrayList<double[]> lonLat);

    /**
     *
     * @return returns a list of coordinates that defines a Geometry object
     */
    ArrayList<double[]> getCoordinates();

}

