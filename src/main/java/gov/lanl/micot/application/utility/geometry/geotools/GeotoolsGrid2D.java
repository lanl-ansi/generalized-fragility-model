package gov.lanl.micot.application.utility.geometry.geotools;

import gov.lanl.micot.application.utility.geometry.Grid2D;
import gov.lanl.micot.application.utility.geometry.Point;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.opengis.coverage.grid.GridCoverageReader;

import java.io.File;
import java.io.IOException;

public class GeotoolsGrid2D implements Grid2D, GeotoolsGeometry {

    private GridCoverage2D grid;

    public GridCoverage2D readArcGrid(String fileLocation){

        File f = new File(fileLocation);
        try {
            GridCoverageReader reader = new ArcGridReader(f);
            this.grid = (GridCoverage2D) reader.read(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.grid;
    }


}
