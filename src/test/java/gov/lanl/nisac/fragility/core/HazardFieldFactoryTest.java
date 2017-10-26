package gov.lanl.nisac.fragility.core;

import junit.framework.TestCase;

public class HazardFieldFactoryTest extends TestCase {

    public void testGetHazardFieldAsc() throws Exception {

        String fileNameAsc = "test_data/fields/iceField_example.asc";

        HazardFieldFactory hazfac = new HazardFieldFactory();

        HazardField hf1 = hazfac.getHazardField(fileNameAsc, "ice");
        assertTrue(hf1 instanceof HazardAsc );


    }

    public void testGetHazardFieldTif() throws Exception {

        String fileNameTif = "test_data/fields/windField_example.tif";

        HazardFieldFactory hazfac = new HazardFieldFactory();

        HazardField hf2 = hazfac.getHazardField(fileNameTif, "wind");
        assertTrue(hf2 instanceof HazardTif );

    }

    public void testGetHazardFieldNull() throws Exception {

        String fileNameTif = "test_data/fields/windField_example.xxx";

        HazardFieldFactory hazfac = new HazardFieldFactory();

        HazardField hf2 = hazfac.getHazardField(fileNameTif, "wind");
        assertNull(hf2);

    }

}