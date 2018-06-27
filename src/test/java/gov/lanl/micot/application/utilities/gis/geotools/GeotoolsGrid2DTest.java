package gov.lanl.micot.application.utilities.gis.geotools;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class GeotoolsGrid2DTest extends TestCase {

    private GeotoolsGrid2D gtool = new GeotoolsGrid2D();


    public void testGetPointValue() {
        gtool.readArcGrid("test_data/fields/windField_example.asc");

        List<double[]> crd = new ArrayList<>();

        double[] coord = new double[]{-72.65509313, 41.749083498};
        crd.add(coord);

        double dv = gtool.getPointValue(crd);
        assertEquals(dv, 63.3223482837, 1e-8);


    }


    public void testGetPointValues() {
        gtool.readArcGrid("test_data/fields/windField_example.asc");

        List<double[]> crds = new ArrayList<>();

        double[] coord1 = new double[]{-72.65509313, 41.749083498};
        crds.add(coord1);
        double[] coord2 = new double[]{-72.77406533304466, 41.69916604603211};
        crds.add(coord2);

        List<Double> rList;

        rList = gtool.getPointValues(crds);
        System.out.println(rList);

        assertEquals(rList.get(0), 63.3223482837, 1e-8);
        assertEquals(rList.get(1), 68.7835419802, 1e-8);
    }


    public void testGetOutsideExtentCount() {

        gtool.readTiffGrid("test_data/fields/windField_example.tif");

        List<double[]> crds = new ArrayList<>();

        double[] coord1 = new double[]{-12.65509313, 11.74908};
        crds.add(coord1);
        double[] coord2 = new double[]{-12.77406533304466, 11.69916};
        crds.add(coord2);

        List<Double> rList;
        rList = gtool.getPointValues(crds);

       int count = gtool.getOutsideExtentCount();

        assertEquals(count, 2);

    }



}