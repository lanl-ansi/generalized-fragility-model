package gov.lanl.nisac.fragility.core;

import org.geotools.coverage.grid.GridCoverage2D;

/**
 * Hazard class for tif formatted data
 */
public class HazardTif implements HazardField {

    private final String fileLocation;
    private final String identifier;
    private GridCoverage2D grid;

    /**
     * Constructor
     * @param fileName defines file location
     */
    HazardTif(String fileName){
        this.fileLocation = fileName;
        this.identifier = "wind"; //TODO: generalize tif identifiers
    }

    public double getExposure(double[] latLon) {
        return 0;
    }

    /**
     *
     * @return absolute/relative file location
     */
    @Override
    public String getFileLocationPath() {
        return fileLocation;
    }

    /**
     *
     * @return only file name with extension
     */
    @Override
    public String getFileName() {
        return null;
    }

    /**
     *
     * @return returns a 2D geotools  grid
     */
    @Override
    public GridCoverage2D getField() {
        return this.grid;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

}
