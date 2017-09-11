package gov.lanl.nisac.fragility.core;

import org.geotools.coverage.grid.GridCoverage2D;

public interface HazardField {

    String getFileLocation();

    String getFileName();

    GridCoverage2D getField();

}
