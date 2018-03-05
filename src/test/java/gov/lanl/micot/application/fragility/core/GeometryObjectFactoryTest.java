package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.fragility.FragilityParameters;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import junit.framework.TestCase;

import java.util.ArrayList;

public class GeometryObjectFactoryTest extends TestCase {
    private FragilityParameters parser;

    public void setUp() throws Exception {

        String[] cmds = new String[8];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_LineStrings.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/windField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "wind";
        cmds[6] = "-e";
        cmds[7] = "wind";

        parser = new FragilityParameters(cmds);

    }

    public void testGeometryLineString(){

        String assets = parser.getAssetInputPath();

        // assets
        GFMDataReader gfmdr = new GFMDataReader();
        gfmdr.readGeoJsonFile(assets);
        ArrayList<GeometryObject> dataAssets = gfmdr.getGeometryObjects();

        assertTrue(dataAssets.get(0) instanceof GeometryLineString );

    }

    public void testGeometryNull(){

        // test null returns
        GeometryObject go1 = new GeometryObjectFactory().getGeometry("newLine", "3");
        assertNull(go1);

        GeometryObject go2 = new GeometryObjectFactory().getGeometry(null, "3");
        assertNull(go2);
    }

}