package gov.lanl.micot.application.fragility.core;

import junit.framework.TestCase;

public class HazardAscTest extends TestCase {

    public HazardAsc hat;
    public void setUp(){
        hat = new HazardAsc("test_data/fields/windField_example.asc","wind");
    }

    public void testGetFileLocationPath() throws Exception {
    }

    public void testGetFileName() throws Exception {
        assertEquals(hat.getFileName(),"windField_example.asc");
    }

    public void testGetField() throws Exception {
        assertNotNull(hat.getField());
    }

    public void testGetIdentifier() throws Exception {
        assertEquals(hat.getIdentifier(),"wind");
    }

}