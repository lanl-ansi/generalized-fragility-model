package gov.lanl.micot.application.rdt;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.fragility.core.GeometryObject;
import gov.lanl.micot.application.fragility.core.ResponseEstimator;
import gov.lanl.micot.application.fragility.core.ResponseEstimatorFactory;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import gov.lanl.micot.application.utilities.gis.RasterField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragilityRDT {

    private static RDTParameters parser;

    public static void main(String[] args) {
        parser = new RDTParameters(args);

        if (args.length<1){
            System.err.println("no arguments provided - use help '-h' option");
        }

        if (parser.hasAssets()){
            mainRoutine();

        }else{

            String rdtFilePath = parser.getRdtInputPath();

            // infer default poles and run Fragility routine
            RDTProcessing.inferPoles(rdtFilePath, parser);
        }

    }


    private static void mainRoutine() {

        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
        GFMDataReader gfmDataReader = new GFMDataReader();
        ArrayList<HazardField> hazardObjects = gfmDataReader.readHazardFile(hazardFiles, ids);

        // assets
        gfmDataReader.readGeoJsonFile(assets);
        ArrayList<GeometryObject> dataAssets = gfmDataReader.getGeometryObjects();
        List<Map<String, PropertyData>> props = gfmDataReader.getProperties();

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
