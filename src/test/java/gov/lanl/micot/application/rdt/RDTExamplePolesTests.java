package gov.lanl.micot.application.rdt;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.micot.application.fragility.core.*;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.RasterField;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RDTExamplePolesTests extends TestCase {


    public void testExamplePoles() throws Exception {
        System.out.println("Test: testExamplePoles");
        RDTParameters parser;
        String[] cmds = new String[14];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles.json";
        cmds[2] = "-num";
        cmds[3] = "3";
        cmds[4] = "-so";
        cmds[5] = "scenarioBlock.json";
        cmds[6] = "-o";
        cmds[7] = "fragility_output_example_poles.json";
        cmds[8] = "-i";
        cmds[9] = "wind";
        cmds[10] = "-e";
        cmds[11] = "wind";
        cmds[12] = "-hf";
        cmds[13] = "test_data/fields/windField_example.asc";


        parser = new RDTParameters(cmds);
        mainRoutine(parser);

        File file;
        file = new File("scenarioBlock.json");
        assertTrue(file.exists());
        file.delete();


        file = new File("fragility_output_example_poles.json");
        assertTrue(file.exists());
        file.delete();


    }

    public void testExamplePoles1(){

        System.out.println("Test: testExamplePoles1");

        RDTParameters parser;
        String[] cmds = new String[14];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles_1.json";
        cmds[2] = "-num";
        cmds[3] = "3";
        cmds[4] = "-so";
        cmds[5] = "scenarioBlock1.json";
        cmds[6] = "-o";
        cmds[7] = "fragility_output_example_poles_1.json";
        cmds[8] = "-i";
        cmds[9] = "wind";
        cmds[10] = "-e";
        cmds[11] = "wind";
        cmds[12] = "-hf";
        cmds[13] = "test_data/fields/windField_example.asc";


        parser = new RDTParameters(cmds);
        mainRoutine(parser);

        File file;
        file = new File("scenarioBlock1.json");
        assertTrue(file.exists());
//        file.delete();


        file = new File("fragility_output_example_poles_1.json");
        assertTrue(file.exists());
//        file.delete();

    }

    public void testExamplePoles2(){

        System.out.println("Test: testExamplePoles2");

        RDTParameters parser;
        String[] cmds = new String[14];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles_2.json";
        cmds[2] = "-num";
        cmds[3] = "3";
        cmds[4] = "-so";
        cmds[5] = "scenarioBlock2.json";
        cmds[6] = "-o";
        cmds[7] = "fragility_output_example_poles_2.json";
        cmds[8] = "-i";
        cmds[9] = "wind";
        cmds[10] = "-e";
        cmds[11] = "wind";
        cmds[12] = "-hf";
        cmds[13] = "test_data/fields/windField_example.asc";


        parser = new RDTParameters(cmds);
        mainRoutine(parser);

        File file;
        file = new File("scenarioBlock2.json");
        assertTrue(file.exists());
        file.delete();

        file = new File("fragility_output_example_poles_2.json");
        assertTrue(file.exists());
        file.delete();

    }

    private void mainRoutine(RDTParameters parser) {

        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
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

        // ----------------------------------------
        // checking for RDT scenario option
        if (parser.isHasScenarioOutput()) {
            // how many scenarios? (default is one)
            if (parser.isHasNumberOfScenarios())
                RDTProcessing.setNumberOfScenarios(parser.getNumberOfScenarios());

            if(parser.isHasScenarioOutput())
                RDTProcessing.setScenarioOutputPath(parser.getScenarioOutput());
            // generate scenario block
            RDTProcessing.generateScenarios(broker.getResponsesArray(), broker.getAssetProperties());
        }

    }
}
