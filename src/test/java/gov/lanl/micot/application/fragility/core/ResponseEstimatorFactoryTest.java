package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.fragility.FragilityParameters;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.fragility.responseEstimators.ResponseEstimator;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseEstimatorFactoryTest extends TestCase {

    private static FragilityParameters parser;

    public void setUp(){
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

        parser = new FragilityParameters(cmds);
    }

    public void testRunResponseEstimator() throws Exception {


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
        List<Map<String, PropertyData>> props = gfmdr.getProperties();

        // GFM set-up and produce exposures
        GFMEngine broker = new GFMEngine();
        broker.setHazardfields(hazardObjects);
        broker.setGeometryObjects(dataAssets);
        broker.setAssetProperties(props);
        broker.produceExposures();

        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator ppw = ref.runResponseEstimator("wind",broker,"nothing");
        assertNotNull(ppw);

        ResponseEstimator ppw1 = ref.runResponseEstimator("windIce",broker,"nothing");
        assertNotNull(ppw1);

        ResponseEstimator ppw2 = ref.runResponseEstimator("flood",broker,"nothing");
        assertNull(ppw2);

        ResponseEstimator ppw3 = ref.runResponseEstimator("nothing",broker,"nothing");
        assertNull(ppw3);

    }

}