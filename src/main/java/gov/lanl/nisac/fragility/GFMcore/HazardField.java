package gov.lanl.nisac.fragility.GFMcore;

import org.geotools.coverage.grid.GridCoverage2D;

public interface HazardField {

    double getExposure(double[] latLon);

    String getFileLocation();

    String getFileName();

    GridCoverage2D getField();

}
