package gov.lanl.nisac.fragility.core;

import org.geotools.coverage.grid.GridCoverage2D;

public class HazardTif implements HazardField {

    private final String fileLocation;
    private final String identifier;
    private GridCoverage2D grid;

    public HazardTif(String fileName){
        this.fileLocation = fileName;
        this.identifier = "wind"; //TODO: generalize tif identifiers
    }


    public double getExposure(double[] latLon) {
        return 0;
    }

    @Override
    public String getFileLocation() {
        return fileLocation;
    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public GridCoverage2D getField() {
        return this.grid;
    }

}
