package gov.lanl.micot.application.fragility.core;

import junit.framework.TestCase;



public class GeometryObjectFactoryTest extends TestCase {


    public void testGetGeometryGeometryLineString() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("LineString", "1");

        assertTrue(go instanceof GeometryLineString);
        assertEquals(go.getIdentifier(), "1");

    }

    public void testGetGeometryGeometryPoint() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("Point", "11");
        assertTrue(go instanceof GeometryPoint);
        assertEquals(go.getIdentifier(), "11");

    }

    public void testGetGeometryGeometryMultiPoint() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("MultiPoint", "12");
        assertTrue(go instanceof GeometryMultiPoint);
        assertEquals(go.getIdentifier(), "12");

    }

    public void testGetGeometryNull() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("nothing", "666");
        assertEquals(go, null);

    }

    public void testshapeNull() {
        GeometryObject go = new GeometryObjectFactory().getGeometry(null, null);
        assertEquals(go, null);

    }

}