package gov.lanl.nisac.lpnorm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.lanl.nisac.CommandLineOptions;
import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMDataReader;
import gov.lanl.nisac.fragility.io.GFMDataWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static gov.lanl.nisac.lpnorm.PoleConstants.*;

/**
 * This class handles RDT data pricessing and generates the corresponding scenario block
 */
public final class RDTProcessing {

    private static ObjectNode rdtData;
    private static ObjectMapper mapper = new ObjectMapper();
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
    public static void inferPoles(String filePath, CommandLineOptions parser) {

        if (parser.isHasNumberOfScenarios()) numberOfScenarios = parser.getNumberOfScenarios();
        if (parser.isHasScenarioOutput()) scenarioOutputPath = parser.getScenarioOutput();
        if (parser.isHasRDTPoles()) polesOutputPath = parser.getRdtPolesOutput();

        rdtData = readRDTJSON(filePath);
        GFMDataWriter.writeResults(rdtData, polesOutputPath);

        String output = parser.getOutputFilePath();
        String estimator = parser.getEstimator();

        // hazards
        String[] hazardFiles = parser.getHazardInputPaths();
        String[] ids = parser.getIdentifiers();
        String assets = parser.getAssetInputPath();
        ArrayList<HazardField> hazardObjects = GFMDataReader.readHazardFile(hazardFiles, ids);

        // assets
        GFMDataReader.readGeoJsonFile(polesOutputPath);
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

        generateScenarios(broker.getResponsesArray(), broker.getAssetProperties());

    }

    /**
     * generates RDT scenario block based off of response estimations
     *
     * @param response
     * @param assetProperties
     */
    private static void generateScenarios(ArrayNode response, ArrayList<JsonNode> assetProperties) {

        Random r = new Random();


        int timeCt = 0;
        ArrayNode lineIdentifier;

        HashMap<String, String> poles = new HashMap();

        // associate pole identifiers to it's JSON node
        for (JsonNode n : assetProperties)
            poles.put(n.get("id").asText(), n.get("lineId").asText());


        ArrayNode scenarioArray = mapper.createArrayNode();

        // generating scenarios
        do {

            lineIdentifier = mapper.createArrayNode();

            for (JsonNode rd : response) {
                // if response > rand(0,1) --> line is disabled
                if (rd.get("value").asDouble() > r.nextFloat()) {
                    String line = poles.get(rd.get("id").asText());

                    if (!lineIdentifier.has(line)) {
                        lineIdentifier.add(line);
                    }
                }
            }

            timeCt += 1;

            ObjectNode singleScenario = mapper.createObjectNode()
                    .put("id", String.valueOf(timeCt))
                    .putPOJO("hardened_disabled_lines", mapper.createArrayNode())
                    .putPOJO("disable_lines", lineIdentifier);


            scenarioArray.add(singleScenario);

        } while (timeCt < numberOfScenarios);


        JsonNode allScenarios = mapper.createObjectNode()
                .putPOJO("scenarios", scenarioArray);

        GFMDataWriter.writeResults(allScenarios, scenarioOutputPath);

    }

    /**
     * special method that reads in RDT json and infers pole data from bus locations
     * @param filePath
     * @return Jackson GeoJSON formatted object
     */
    private static ObjectNode readRDTJSON(String filePath) {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode fileNodes = null;
        HashMap<String, List<Double>> nodeLocation;
        InputStream inStream;

        try {
            inStream = new FileInputStream(filePath);
            fileNodes = mapper.readTree(inStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        nodeLocation = new HashMap<>();

        for (JsonNode n : fileNodes.findValue("buses")) {
            String sid = String.valueOf(n.get("id").asText());
            double xValue = n.get("x").doubleValue();
            double yValue = n.get("y").doubleValue();

            nodeLocation.put(sid, new ArrayList<>());
            nodeLocation.get(sid).add(xValue);
            nodeLocation.get(sid).add(yValue);
        }

        int id_count = 0;
        ArrayNode features = mapper.createArrayNode();

        // create poles for each line segment
        for (JsonNode n : fileNodes.findValue("lines")) {

            // checking to make sure NOT a transformer
            boolean xfmrExist = false;
            JsonNode xmfValue = n.findValue("is_transformer");

            // node may not exist, so checking
            if (xmfValue != null) {
                xfmrExist = n.get("is_transformer").asBoolean();
            }

            if (!xfmrExist) {

                String lid = String.valueOf(n.get("id").asText());
                String node1 = String.valueOf(n.get("node1_id").asText());
                String node2 = String.valueOf(n.get("node2_id").asText());

                if (nodeLocation.containsKey(node1) && nodeLocation.containsKey(node2)) {
                    double x0 = nodeLocation.get(node1).get(0);
                    double y0 = nodeLocation.get(node1).get(1);

                    // get lat/lon values
                    double v1 = nodeLocation.get(node2).get(0) - nodeLocation.get(node1).get(0);
                    double v2 = nodeLocation.get(node2).get(1) - nodeLocation.get(node1).get(1);
                    double ndist = Math.sqrt(v1 * v1 + v2 * v2);


                    // v = (x1,y1) - (x2,y2)
                    // normalized vector u = v  / ||v||
                    // now point distance is along line (x1,y1) + du =
                    v1 = v1 / ndist;
                    v2 = v2 / ndist;

                    // using 111.111 km per degree (or 111,111 meters)
                    // pole spacing at 91 meters 0.000817463169242 degrees
                    // 0.000817463169242
                    double numPoles = Math.floor(ndist * DEG_TO_METERS) / POLE_SPACING;
                    numPoles = Math.floor(numPoles);

//                if (numPoles < 1) {
//                    System.out.println(" distance between nodes: " + node1 + " & " + node2 +
//                            " is " + ndist + " decimal degrees ( < 91 meters )");
//                    System.out.println(" -- NO POLES created for line: " + lid);
//                }

                    ObjectNode obj;
                    for (int i = 0; i < numPoles; i++) {
                        obj = createPoleAsset(id_count, lid, new double[]{x0, y0});

                        features.add(obj);

                        // move to next pole location
                        x0 = x0 + v1 * NEXT_DISTANCE;
                        y0 = y0 + v2 * NEXT_DISTANCE;
                        id_count = id_count + 1;
                    }
                } else {
                    System.out.println(node1 + " and/or " + node2 + " doesn't exist for line id " + lid + "");
                }
            }

        }// end for


        System.out.println("Number of poles created: " + id_count);

        // create geoJSON Feature Collection
        ObjectNode featureNode = mapper.createObjectNode()
                .put("type", "FeatureCollection")
                .putPOJO("features", features);

        return featureNode;
    }

    private static ObjectNode createPoleAsset(int id, String line, double[] coord) {
//        ObjectMapper mapper = new ObjectMapper();

        // creates one GeoJSON Point Object
        ObjectNode featureNode = mapper.createObjectNode()
                .putPOJO("geometry", mapper.createObjectNode()
                        .put("type", "Point")
                        .putPOJO("coordinates", mapper.createArrayNode()
                                .add(coord[0])
                                .add(coord[1])
                        ))
                .put("type", "Feature")

                .putPOJO("properties", mapper.createObjectNode()
                        .put("id", id)
                        .put("baseDiameter", BASE_DIAMETER)
                        .put("cableSpan", CABLE_SPAN)
                        .put("commAttachmentHeight", COMM_ATTACHMENT_HEIGHT)
                        .put("commCableDiameter", COMM_CABLE_DIAMETER)
                        .put("commCableNumber", COMM_CABLE_NUMBER)
                        .put("commCableWireDensity", COMM_CABLE_WIRE_DENSITY)
                        .put("height", HEIGHT)
                        .put("meanPoleStrength", MEAN_POLE_STRENGTH)
                        .put("powerAttachmentHeight", POWER_ATTACHMENT_HEIGHT)
                        .put("powerCableDiameter", POWER_CABLE_DIAMETER)
                        .put("powerCableNumber", POWER_CABLE_NUMBER)
                        .put("powerCableWireDensity", POWER_CABLE_WIRE_DENSITY)
                        .put("powerCircuitName", POWER_CIRCUIT_NAME)
                        .put("stdDevPoleStrength", STD_DEV_POLE_STRENGTH)
                        .put("topDiameter", TOP_DIAMETER)
                        .put("woodDensity", WOOD_DENSITY)
                        .put("lineId", line));

        return featureNode;

    }

}
