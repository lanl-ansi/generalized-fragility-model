package gov.lanl.micot.application.fragility.core;

import org.geotools.coverage.grid.GridCoverage2D;

/**
 * Hazard field interface methods
 */
public interface HazardField {

    /**
     *
     * @return absolute file path location
     */
    String getFileLocationPath();

    /**
     *
     * @return file name
     */
    String getFileName();

    /**
     *
     * @return returns a 2D geotools field
     */
    GridCoverage2D getField();

    /**
     *
     * @return unique identifier for a Hazard field
     */
    String getIdentifier();
}
