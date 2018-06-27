package gov.lanl.micot.application.fragility.core;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class GeometryObjectFactoryTest extends TestCase {


    public void testGetGeometryGeometryLineString() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("LineString", "1");
        assertThat(go, instanceOf(GeometryLineString.class));
        assertEquals(go.getIdentifier(), "1");

    }

    public void testGetGeometryGeometryPoint() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("Point", "11");
        assertThat(go, instanceOf(GeometryPoint.class));
        assertEquals(go.getIdentifier(), "11");

    }

    public void testGetGeometryGeometryMultiPoint() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("MultiPoint", "12");
        assertThat(go, instanceOf(GeometryMultiPoint.class));
        assertEquals(go.getIdentifier(), "12");

    }

    public void testGetGeometryNull() {
        GeometryObject go = new GeometryObjectFactory().getGeometry("nothing", "666");
        assertEquals(go, null);

    }

}