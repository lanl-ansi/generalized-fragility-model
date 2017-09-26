package gov.lanl.nisac;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMDataReader;
import gov.lanl.nisac.lpnorm.RDTProcessing;

import java.util.ArrayList;

public class Fragility {

    private static CommandLineOptions parser;

    public static void main(String[] args) {
        parser = new CommandLineOptions(args);

        if(args.length < 1){
            System.err.println("no arguments provided - use help 'h' option");
            System.exit(1);
        }

        if (parser.hasRDT()) {

            String path = parser.getRdtInputPath();
            RDTProcessing.inferPoles(path);

        } else {
            mainRoutine();
        }

    }

    private static void mainRoutine() {

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
}
