package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.FragilityParameters;
import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.fragility.core.GeometryObject;
import gov.lanl.micot.application.fragility.core.ResponseEstimatorFactory;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThresholdFloodTest extends TestCase {
    private FragilityParameters parser;


    @Before
    public void setUp() throws Exception {

        String[] cmds = new String[10];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_flood_fragility.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/floodField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "flood";
        cmds[6] = "-e";
        cmds[7] = "ThresholdFlood";
        cmds[8] = "-o";
        cmds[9] = "flood_responses.json";

        this.parser = new FragilityParameters(cmds);

    }


    public void testCalcFragility() {

        String output = this.parser.getOutputFilePath();
        String estimator = this.parser.getResponseEstimator();

        // hazards
        String[] hazardFiles = this.parser.getHazardInputPaths();
        String[] ids = this.parser.getIdentifiers();
        String assets = this.parser.getAssetInputPath();
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

        // compute response approximations
        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator(estimator, broker, output);

        HashMap<String, Double> rsps = r1.responses;
        System.out.println(rsps);

        assertEquals(1.0, rsps.get("0"), 1e-9);
        assertEquals(0.0, rsps.get("1"), 1e-9);

    }

}