package gov.lanl.nisac.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;

import gov.lanl.nisac.CommandLineOptions;
import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMDataReader;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Test of the static asset stress estimator
 * @author Russell Bent
 */
public class AssetStaticStressTest extends TestCase {
    private CommandLineOptions parser;

    public void testStatic() {

        String[] cmds = new String[6];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles_static_fragility.json";
        cmds[2] = "-e";
        cmds[3] = "static";
        cmds[4] = "-o";
        cmds[5] = "fragility_output.json";

        parser = new CommandLineOptions(cmds);

        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
        GFMDataReader gfmdr = new GFMDataReader();
        ArrayList<HazardField> hazardObjects = gfmdr.readHazardFile(hazardFiles, ids);

        // assets
        gfmdr.readGeoJsonFile(assets);
        ArrayList<GeometryObject> dataAssets = gfmdr.getGeometryObjects();
        ArrayList<JsonNode> props = gfmdr.getProperties();

        // GFM set-up and produce exposures
        GFMEngine broker = new GFMEngine();
        broker.setHazardfields(hazardObjects);
        broker.setGeometryObjects(dataAssets);
        broker.setAssetProperties(props);
        broker.produceExposures();

        // compute response approximations
        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator(estimator, broker, output);
        
        assertEquals(r1.getResponse("0"), 0.3); // tests that the input data is correctly parsed
        assertEquals(r1.getResponse("1"), 0.5); // tests the default
    }


}