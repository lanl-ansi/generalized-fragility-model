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
     * @param fileLocation - string path to file
     * @param id - indentifier
     */
    HazardAsc(String fileLocation, String id) {
        this.fileLocationPath = fileLocation;
        this.identifier = id;
        setFileName(fileLocation);
        openFile();

    }

    /**
     *
     * @param fileLocation - defines absolute file location
     */
    private void setFileName(String fileLocation){
        if (fileLocation.contains("/")){
            int idx = fileLocation.lastIndexOf("/");
            String name = fileLocation.substring(idx+1);
            this.fileName = name;
        }
    }

    @Override
    public String getFileLocationPath() {
        return this.fileLocationPath;
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
        return this.identifier;
    }
}
