package gov.lanl.nisac.fragility.GFMcore;

import org.geotools.coverage.grid.GridCoverage2D;

public class HazardTif implements HazardField {

    private final String fileLocation;
    private final GFMEngine broker;
    private GridCoverage2D grid;

    public HazardTif(String fileName, GFMEngine mediator){
        this.fileLocation = fileName;
        this.broker = mediator;
    }

    @Override
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
