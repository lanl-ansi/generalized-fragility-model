package gov.lanl.nisac.fragility.core;

import gov.lanl.nisac.CommandLineOptions;
import gov.lanl.nisac.fragility.io.GFMDataReader;
import junit.framework.TestCase;

import java.util.ArrayList;

public class GeometryObjectFactoryTest extends TestCase {
    private CommandLineOptions parser;

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

        parser = new CommandLineOptions(cmds);

    }

    public void testGeometryLineString(){

        String assets = parser.getAssetInputPath();

        // assets
        GFMDataReader.readGeoJsonFile(assets);
        ArrayList<GeometryObject> dataAssets = GFMDataReader.getGeometryObjects();

        assertTrue(dataAssets.get(0) instanceof GeometryLineString );

    }

}