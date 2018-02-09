package gov.lanl.micot.application.rdt;

import gov.lanl.micot.application.fragility.core.*;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import gov.lanl.micot.application.fragility.responseEstimators.ResponseEstimator;
import gov.lanl.micot.application.utilities.json.AssetDataFromJackson;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import gov.lanl.micot.application.utilities.json.JsonDataFromJackson;

import java.util.*;

/**
 * This class handles RDT data processing and generates the corresponding scenario block
 */
public final class RDTProcessing {

    private static String scenarioOutputPath = "rdtScenarioBlock.json";
    private static String polesOutputPath = "RDT-to-Poles.json";
    private static int numberOfScenarios = 1;


    private RDTProcessing() {
    }

    /**
     * Infers default power pole data, based on RDT bus locations
     *
     * @param filePath - RDT (JSON) data file path
     * @param parser   - command line parser arguments
     */
    public static void inferPoles(String filePath, RDTParameters parser) {

        if (parser.isHasNumberOfScenarios()) numberOfScenarios = parser.getNumberOfScenarios();
        if (parser.isHasScenarioOutput()) scenarioOutputPath = parser.getScenarioOutput();
        if (parser.isHasRDTPoles()) polesOutputPath = parser.getRdtPolesOutput();

        AssetDataFromJackson rdtData = new JsonDataFromJackson();
        rdtData.readWriteRDTJSON(filePath, polesOutputPath);

        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();

        GFMDataReader gfmDataReader = new GFMDataReader();
        ArrayList<HazardField> hazardObjects = gfmDataReader.readHazardFile(hazardFiles, ids);

        // assets
        gfmDataReader.readGeoJsonFile(polesOutputPath);
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

        generateScenarios(broker.getResponsesArray(), broker.getAssetProperties());

    }

    /**
     * Generates RDT scenario block based-off of response estimations.
     * @param response repsonse values from asset exposures
     * @param assetProperties asset property data
     */
    public static void generateScenarios(HashMap<String, Double> response, List<Map<String, PropertyData>> assetProperties) {

        Random r = new Random();

        int currentScenarioCount = 0;

        HashMap<String, String> poles = new HashMap();

        for (Map<String, PropertyData> assetProperty : assetProperties) {
            poles.put(assetProperty.get("id").asString(), assetProperty.get("lineId").asString());
        }

        // list to build each scenario
        List<String> lineIdList;
        // global map for all scenarios
        Map<Integer, List<String>> scenarioLineList = new HashMap<>();

        // generating scenarios
        do {

            lineIdList = new ArrayList<>();

            // hasmap iterator
            for (Map.Entry<String, Double> pair : response.entrySet()){
                //iterate over the pairs
                if (pair.getValue() > r.nextFloat()) {
                    String line = poles.get(pair.getKey()).trim();

                    // ensure no duplicate line numbers add for each scenario
                    if (!lineIdList.contains(line)) {
                        lineIdList.add(line);
                    }
                }
            }

            // add new scenario
            scenarioLineList.put(currentScenarioCount, new ArrayList<>());
            scenarioLineList.get(currentScenarioCount).addAll(lineIdList);

            currentScenarioCount += 1;

        } while (currentScenarioCount < numberOfScenarios);

        // write out scenario json file
        AssetDataFromJackson adj = new JsonDataFromJackson();
        adj.writeRDTScenarios(scenarioLineList, scenarioOutputPath);
    }

    static void setNumberOfScenarios(int number){
        numberOfScenarios = number;
    }

    static void setScenarioOutputPath(String path){
        scenarioOutputPath = path;
    }
}
