package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.FragilityParameters;
import gov.lanl.micot.application.fragility.core.*;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PowerPoleWindStressTIFFTest extends TestCase {
    private FragilityParameters parser;

    public void setUPTest() throws Exception  {
        super.setUp();

        String[] cmds = new String[10];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/windField_example.tif";
        cmds[4] = "-i";
        cmds[5] = "wind";
        cmds[6] = "-e";
        cmds[7] = "PowerPoleWindStress";
        cmds[8] = "-o";
        cmds[9] = "fragility_output_tifff.json";

        parser = new FragilityParameters(cmds);

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

        // compute response approximations
        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator(estimator, broker, output);
        r1.writeResults();
    }

    public void testWriteResults() throws Exception {
        setUPTest();
        System.out.println("--- ---- --->");

        File ftiff = new File("fragility_output_tifff.json");
        TimeUnit.MILLISECONDS.sleep(500);
        assertTrue(ftiff.exists());
        ftiff.delete();
    }

}