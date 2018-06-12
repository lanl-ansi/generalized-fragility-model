package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.fragility.FragilityParameters;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.fragility.responseEstimators.ResponseEstimator;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GFMEngineTest {

    private FragilityParameters parser;
    private ArrayList<GeometryObject> dataAssets;
    ArrayList<HazardField> hazardObjects;
    private GFMEngine broker;
    private List<Map<String, PropertyData>> props;
    GFMDataReader gfmDataReader;


    @Before
    public void setUp() throws Exception {

        String[] cmds = new String[8];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_AllGeometryTypes.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/windField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "wind";
        cmds[6] = "-e";
        cmds[7] = "PowerPoleWindStress";

        parser = new FragilityParameters(cmds);

        String output = parser.getOutputFilePath();
        String estimator = parser.getResponseEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
        this.gfmDataReader = new GFMDataReader();
        this.hazardObjects = gfmDataReader.readHazardFile(hazardFiles, ids);

        // assets
        gfmDataReader.readGeoJsonFile(assets);





    }

    @Test
    public void produceExposures() {

        this.dataAssets = gfmDataReader.getGeometryObjects();
        this.props = gfmDataReader.getProperties();

        // GFM set-up and produce exposures
        this.broker = new GFMEngine();
        this.broker.setHazardfields(hazardObjects);
        this.broker.setGeometryObjects(dataAssets);
        this.broker.setAssetProperties(props);

        this.broker.produceExposures();

        Map<String, HashMap<String, ArrayList<Double>>> expValues = this.broker.getExposures();
        System.out.println(expValues.get("wind").get("0").get(0));

        assertEquals(expValues.get("wind").get("0").get(0), 63.32234, 0.0001);

    }

    @Test
    public void setGeometryObjects() {

        this.dataAssets = gfmDataReader.getGeometryObjects();
        this.props = gfmDataReader.getProperties();

        // GFM set-up and produce exposures
        this.broker = new GFMEngine();
        this.broker.setHazardfields(hazardObjects);
        this.broker.setGeometryObjects(dataAssets);
        this.broker.setAssetProperties(props);

        assertNotNull(this.broker.getGeometryObjects());

    }

    @Test
    public void setHazardfields() {

        this.dataAssets = gfmDataReader.getGeometryObjects();
        this.props = gfmDataReader.getProperties();

        // GFM set-up and produce exposures
        this.broker = new GFMEngine();
        this.broker.setHazardfields(hazardObjects);
        this.broker.setAssetProperties(props);

        assertNotNull(this.broker.getHazardfields());

    }

    @Test
    public void setAssetProperties() {

        this.dataAssets = gfmDataReader.getGeometryObjects();
        this.props = gfmDataReader.getProperties();

        // GFM set-up and produce exposures
        this.broker = new GFMEngine();
        this.broker.setAssetProperties(props);

        assertNotNull(this.broker.getAssetProperties());
    }



    @Test
    public void getExposures() {

        this.dataAssets = gfmDataReader.getGeometryObjects();
        this.props = gfmDataReader.getProperties();

        // GFM set-up and produce exposures
        this.broker = new GFMEngine();
        this.broker.setHazardfields(hazardObjects);
        this.broker.setGeometryObjects(dataAssets);
        this.broker.setAssetProperties(props);
        this.broker.produceExposures();

        Map<String, HashMap<String, ArrayList<Double>>> expValues = this.broker.getExposures();

        assertNotNull(expValues);

    }

    @Test
    public void writeJSONOutputs() {

        this.dataAssets = gfmDataReader.getGeometryObjects();
        this.props = gfmDataReader.getProperties();

        // GFM set-up and produce exposures
        this.broker = new GFMEngine();
        this.broker.setHazardfields(hazardObjects);
        this.broker.setGeometryObjects(dataAssets);
        this.broker.setAssetProperties(props);

        this.broker.produceExposures();

        Map<String, HashMap<String, ArrayList<Double>>> expValues = this.broker.getExposures();
        String estimator = parser.getResponseEstimator();
        String output = parser.getOutputFilePath();

        // compute response approximations
        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator(estimator, broker, "test_output.json");
        r1.writeResults();

        File nf = new File("test_output.json");

        assertTrue(nf.exists());

        nf.delete();

        assertTrue(!nf.exists());

    }

    @Test
    public void getResponsesArray() {

        this.dataAssets = gfmDataReader.getGeometryObjects();
        this.props = gfmDataReader.getProperties();

        // GFM set-up and produce exposures
        this.broker = new GFMEngine();
        this.broker.setHazardfields(hazardObjects);
        this.broker.setGeometryObjects(dataAssets);
        this.broker.setAssetProperties(props);

        this.broker.produceExposures();

        Map<String, HashMap<String, ArrayList<Double>>> expValues = this.broker.getExposures();
        String estimator = parser.getResponseEstimator();
        String output = parser.getOutputFilePath();

        // compute response approximations
        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator(estimator, broker, "test_output2.json");
        r1.writeResults();

        File nf = new File("test_output2.json");

        assertTrue(nf.exists());

        nf.delete();

        assertTrue(!nf.exists());

        assertNotNull(this.broker.getResponsesArray());


    }
}