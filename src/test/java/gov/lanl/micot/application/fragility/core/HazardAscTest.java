package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.utility.gis.RasterField;
import junit.framework.TestCase;

public class HazardAscTest extends TestCase {

    public RasterField hat;
    public void setUp(){
        hat = new RasterField("test_data/fields/windField_example.asc","wind");
    }

    public void testGetFileLocationPath() throws Exception {
    }

    public void testGetIdentifier() throws Exception {
        assertEquals(hat.getIdentifier(),"wind");
    }

}