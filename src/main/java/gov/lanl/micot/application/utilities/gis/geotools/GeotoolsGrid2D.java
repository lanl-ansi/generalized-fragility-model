package gov.lanl.micot.application.utilities.gis.geotools;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.JTS;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Trevor Crawford
 */
public class GeotoolsGrid2D implements GeotoolsGIS {

    private GridCoverage2D grid;
    private CoordinateReferenceSystem crs;

    private int outsideExtentCount = 0;

    /**
     * Method to read in ArcGrid formatted file and stored in GridCoverage2D field variable.
     *
     * @param fileLocation
     */
    public void readArcGrid(String fileLocation) {

        File f = new File(fileLocation);
        this.grid = null;

        try {
            GridCoverageReader reader = new ArcGridReader(f);
            this.grid = (GridCoverage2D) reader.read(null);
            this.crs = this.grid.getCoordinateReferenceSystem2D();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to read in GeoTIFF formatted file and stored in GridCoverage2D field variable.
     *
     * @param fileLocation file string path.
     */
    public void readTiffGrid(String fileLocation){

        File f = new File(fileLocation);
        this.grid = null;

        try {
            GridCoverageReader reader = new GeoTiffReader(f);
            this.grid = (GridCoverage2D) reader.read(null);
            this.crs = this.grid.getCoordinateReferenceSystem2D();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that returns a point value from longitude and latitude coordinate values.
     *
     * @param crd longitude and latitude double array values
     * @return single exposure value
     */
    public double getPointValue(List<double[]> crd) {
        double x;       // longitude
        double y;       // latitude
        double[] r;     // exposures value

        // getting list of single coordinate: [lon, lat]

        x = crd.get(0)[0];
        y = crd.get(0)[1];
        Coordinate crds = new Coordinate(x, y);
        DirectPosition p = JTS.toDirectPosition(crds, crs);

        try {
            r = this.grid.evaluate(p, new double[1]);
        } catch (org.opengis.coverage.PointOutsideCoverageException e) {
            // if outside coverage, set to default value of 0.0
            this.outsideExtentCount += 1;
            r = new double[1];
            r[0] = 0.0;
        }

        return r[0];
    }

    /**
     * Method that returns a point values from an array longitude and latitude coordinate values.
     *
     * @param crd longitude and latitude double array values
     * @return an array of exposure value
     */
    public List<Double> getPointValues(List<double[]> crd) {
        double x;       // longitude
        double y;       // latitude
        double[] r;     // exposures value
        List<Double> rList = new ArrayList<>();

        // getting list of single coordinate: [lon, lat]
        for (double[] aCrd : crd) {

            x = aCrd[0];
            y = aCrd[1];

            Coordinate crds = new Coordinate(x, y);
            DirectPosition p = JTS.toDirectPosition(crds, crs);

            try {
                r = this.grid.evaluate(p, new double[1]);
                rList.add(r[0]);
            } catch (PointOutsideCoverageException e) {
                // if outside coverage, set to default value of 0.0
                this.outsideExtentCount += 1;
                r = new double[1];
                r[0] = 0.0;
                rList.add(r[0]);
            }
        }
        return rList;
    }

    /**
     * Method that returns the count of assets outside of raster extent(s).
     *
     * @return count of assets outside of raster extent(s)
     */
    public int getOutsideExtentCount() {
        return this.outsideExtentCount;
    }
}
