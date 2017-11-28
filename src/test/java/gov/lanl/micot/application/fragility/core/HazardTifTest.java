package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.utilities.gis.RasterField;
import junit.framework.TestCase;

public class HazardTifTest extends TestCase {
    public RasterField hat;
    public void setUp(){
        hat = new RasterField("test_data/fields/windField_example.tif","wind");
    }

    public void testGetFileLocationPath() throws Exception {
    }

    public void testGetIdentifier() throws Exception {
        assertEquals(hat.getIdentifier(),"wind");
    }

}