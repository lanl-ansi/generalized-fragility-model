package gov.lanl.nisac;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMDataReader;

import java.util.ArrayList;

public class Fragility {

    private static CommandLineOptions parser;

    public static void main(String[] args) {
        mainRoutine(args);
    }

    private static void mainRoutine(String[] args) {
        parser = new CommandLineOptions(args);

        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
        ArrayList<HazardField> hazardObjects = GFMDataReader.readHazardFile(hazardFiles, ids);

        // assets
        GFMDataReader.readGeoJsonFile(assets);
        ArrayList<GeometryObject> dataAssets = GFMDataReader.getGeometryObjects();
        ArrayList<JsonNode> props = GFMDataReader.getProperties();

        // GFM set-up and execution
        GFMEngine broker = new GFMEngine();
        broker.setHazardfields(hazardObjects);
        broker.setGeometryObjects(dataAssets);
        broker.setAssetProperties(props);
        broker.produceExposures();

        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator(estimator, broker, output);
        r1.writeResults();
    }
}
