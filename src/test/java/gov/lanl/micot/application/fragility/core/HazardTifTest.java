package gov.lanl.micot.application.fragility.core;

import junit.framework.TestCase;

public class HazardTifTest extends TestCase {
    public HazardTif hat;
    public void setUp(){
        hat = new HazardTif("test_data/fields/windField_example.tif","wind");
    }

    public void testGetFileLocationPath() throws Exception {
    }

    public void testGetFileName() throws Exception {
        assertEquals(hat.getFileName(),"windField_example.tif");
    }

    public void testGetField() throws Exception {
        assertNotNull(hat.getField());
    }

    public void testGetIdentifier() throws Exception {
        assertEquals(hat.getIdentifier(),"wind");
    }

}