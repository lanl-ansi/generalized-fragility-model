package gov.lanl.nisac.fragility.core;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.geometry.jts.JTS;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;

/**
 * Hazard class for Esri asc formatted data
 */
public class HazardAsc implements HazardField {

    private final String fileLocationPath;
    private final String identifier;

    private String fileName;
    private GridCoverage2D grid;

    /**
     * Constructor
     *
     * @param fileLocation - string path to file
     * @param id           - identifier
     */
    HazardAsc(String fileLocation, String id) {
        fileLocationPath = fileLocation;
        identifier = id;
        setFileName(fileLocation);
        openFile();

    }

    /**
     * @param fileLocation - defines absolute file location
     */
    private void setFileName(String fileLocation) {
        if (fileLocation.contains("\\")) {
            int idx = fileLocation.lastIndexOf("\\");
            String name = fileLocation.substring(idx + 1);
            fileName = name;
        }
    }

    @Deprecated
    public double getExposure(double[] latLon) {

        Coordinate crd = new Coordinate(latLon[0], latLon[1]);
        CoordinateReferenceSystem crs = grid.getCoordinateReferenceSystem2D();
        DirectPosition p = JTS.toDirectPosition(crd, crs);
        double[] r = grid.evaluate(p, new double[1]);

        return r[0];
    }

    @Override
    public String getFileLocationPath() {
        return fileLocationPath;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public GridCoverage2D getField() {
        return this.grid;
    }

    private void openFile() {

        File f = new File(fileLocationPath);

        try {
            GridCoverageReader reader = new ArcGridReader(f);
            this.grid = (GridCoverage2D) reader.read(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIdentifier() {
        return identifier;
    }
}
