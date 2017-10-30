package gov.lanl.nisac.fragility.core;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.JTS;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;

/**
 * Hazard class for tif formatted data
 */
public class HazardTif implements HazardField {

    private final String fileLocationPath;
    private final String identifier;

    private String fileName;
    private GridCoverage2D grid;

    /**
     * Constructor
     * @param fileLocation defines file location
     */
    HazardTif(String fileLocation, String id){
        this.fileLocationPath = fileLocation;
        this.identifier = id;
        setFileName(fileLocation);
        openFile();

    }

    private void setFileName(String fileLocation){
        if (fileLocation.contains("/")){
            int idx = fileLocation.lastIndexOf("/");
            String name = fileLocation.substring(idx+1);
            this.fileName = name;
        }
    }

    private void openFile() {

        File f = new File(this.fileLocationPath);

        try {
            GridCoverageReader reader = new GeoTiffReader(f);
            this.grid = (GridCoverage2D) reader.read(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return absolute/relative file location
     */
    @Override
    public String getFileLocationPath() {
        return this.fileLocationPath;
    }

    /**
     *
     * @return only file name with extension
     */
    @Override
    public String getFileName() {
        return this.fileName;
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
        return this.identifier;
    }

}
