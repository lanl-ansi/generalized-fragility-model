package gov.lanl.micot.application.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.micot.application.fragility.FragilityParameters;
import gov.lanl.micot.application.fragility.core.*;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.RasterField;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PowerPoleWindIceStressTest extends TestCase {

    private FragilityParameters parser;

    public void setupWindIceTest() throws Exception {

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
        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
        System.out.println(assets);
        System.out.println(ids[0]);
        System.out.println(ids[1]);

        GFMDataReader gfmdr = new GFMDataReader();

        ArrayList<RasterField> hazardObjects = gfmdr.readHazardFile(hazardFiles, ids);

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