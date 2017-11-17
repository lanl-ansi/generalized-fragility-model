package gov.lanl.micot.application.utility.gis;

import gov.lanl.micot.application.fragility.core.HazardField;
import gov.lanl.micot.application.utility.gis.geotools.GeotoolsGIS;
import gov.lanl.micot.application.utility.gis.geotools.GeotoolsGrid2D;

import java.util.List;

/**
 * This is a wrapper class that exposes GeoTools functionality to other Fragility classes.
 *
 * @author Trevor Crawford
 */
public class RasterField implements HazardField, GeotoolsGIS {

    private GeotoolsGrid2D grid2D = new GeotoolsGrid2D();
    private String identifier;

    /**
     * Constructor method to initialize a new hazard raster data.
     *
     * @param fileLocation file path location
     * @param id unique identifier for raster data
     */
    public RasterField(String fileLocation, String id) {

        if(fileLocation.contains(".asc")){
            this.grid2D.readArcGrid(fileLocation);
        }else if(fileLocation.contains(".tif")){
            this.grid2D.readTiffGrid(fileLocation);
        }else{
            System.err.println("Hazard field file extension not implemented");
            System.exit(-1);
        }

        this.identifier = id;
    }

    /**
     * This method provides a single exposure value for a latitude and longitude location.
     *
     * @param crd
     * @return
     */
    public double evaluatePoint(List<double[]> crd) {
        return this.grid2D.getPointValue(crd);
    }

    /**
     * This method provides exposure values for an array list of latitude and longitude location.
     *
     * @param crd
     * @return
     */
    public List<Double> evaluatePoints(List<double[]> crd) {
        return this.grid2D.getPointValues(crd);
    }

    /**
     * This method provides a unique identifier.
     * @return a string raster identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * This method provides a count of how many points are outside the raster extent.
     * @return a count of points
     */
    public int getOutsideExtentCount(){
        return this.grid2D.getOutsideExtentCount();
    }
}
