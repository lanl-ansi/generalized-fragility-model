package gov.lanl.nisac.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.CommandLineOptions;
import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMDataReader;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;

public class PowerPoleWindIceStressTest extends TestCase {

    private CommandLineOptions parser;

    public void setupWindIceTest() throws Exception {

        String[] cmds = new String[10];

        System.out.println("junk");

        cmds[0] = "-a";
        cmds[1] = "test_data\\inputs\\example_poles.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data\\fields\\windField_example.asc;test_data\\fields\\iceField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "wind;ice";
        cmds[6] = "-e";
        cmds[7] = "windIce";
        cmds[8] = "-o";
        cmds[9] = "fragility_ice_output.json";

        parser = new CommandLineOptions(cmds);
        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
        System.out.println(assets);
        System.out.println(ids[0]);
        System.out.println(ids[1]);

        ArrayList<HazardField> hazardObjects = GFMDataReader.readHazardFile(hazardFiles, ids);

        // assets
        GFMDataReader.readGeoJsonFile(assets);
        ArrayList<GeometryObject> dataAssets = GFMDataReader.getGeometryObjects();
        ArrayList<JsonNode> props = GFMDataReader.getProperties();

        // GFM set-up and produce exposures
        GFMEngine broker = new GFMEngine();
        broker.setHazardfields(hazardObjects);
        broker.setGeometryObjects(dataAssets);
        broker.setAssetProperties(props);
        broker.produceExposures();

        // compute response approximations
        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator(estimator, broker, output);
        r1.writeResults();
    }

    public void testWindIceResults() throws Exception {
        System.out.println("--- ---- --->");
        setupWindIceTest();

        File f = new File("fragility_ice_output.json");
        System.out.println(f.exists());

        assertTrue(f.exists());
        f.delete();
    }


}