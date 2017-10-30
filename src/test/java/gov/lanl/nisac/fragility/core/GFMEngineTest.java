package gov.lanl.nisac.fragility.core;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.CommandLineOptions;
import gov.lanl.nisac.fragility.io.GFMDataReader;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GFMEngineTest extends TestCase {

    private static GFMEngine broker;
    private ArrayList<GeometryObject> dataAssets = null;
    private ArrayList<HazardField> hazardObjects = null;
    private ArrayList<JsonNode> props = null;

    public void setUp() {

        String[] cmds = new String[10];


        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/windField_example.asc test_data/fields/iceField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "wind ice";
        cmds[6] = "-e";
        cmds[7] = "windIce";
        cmds[8] = "-o";
        cmds[9] = "fragility_ice_output.json";

        CommandLineOptions parser = new CommandLineOptions(cmds);
        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();

        GFMDataReader gfmdr = new GFMDataReader();

        hazardObjects = gfmdr.readHazardFile(hazardFiles, ids);

        // assets
        gfmdr.readGeoJsonFile(assets);

        dataAssets = gfmdr.getGeometryObjects();

        props = gfmdr.getProperties();

        broker = new GFMEngine();
        broker.setHazardfields(hazardObjects);
        broker.setGeometryObjects(dataAssets);
        broker.setAssetProperties(props);
        broker.produceExposures();
    }

    public void testSetGeometryObjects() throws Exception {
        broker.setGeometryObjects(dataAssets);
    }

    public void testSetHazardfields() throws Exception {
        broker.setHazardfields(hazardObjects);
    }

    public void testSetAssetProperties() throws Exception {
        broker.setAssetProperties(props);
    }

    public void testGetAssetProperties() throws Exception {
        assertNotNull(broker.getAssetProperties());
    }

    public void testGetExposures() throws Exception {
        assertNotNull(broker.getExposures());
    }

    public void testStoreResults() throws Exception {
        HashMap<String, Double> responses = new HashMap<>();
        responses.put("1", 0.23334);
        responses.put("2", 0.45534);

        broker.storeResults(responses, "example.json");

        File f = new File("example.json");
        assertTrue(f.exists());

        f.delete();
    }

    public void testGetResponsesArray() throws Exception {

        assertNotNull(broker.getResponsesArray());
    }

    public void testProduceExposures() throws Exception {

        broker.produceExposures();

    }


}