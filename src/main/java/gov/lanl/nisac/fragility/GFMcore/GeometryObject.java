package gov.lanl.nisac.fragility.GFMcore;

import java.util.ArrayList;

public interface GeometryObject {

    String getIdentifier();

    void setCoordinates(ArrayList<double[]> latLon);

    ArrayList<double[]> getCoordinates();

}

