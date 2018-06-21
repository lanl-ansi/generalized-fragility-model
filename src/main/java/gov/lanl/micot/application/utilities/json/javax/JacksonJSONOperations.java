package gov.lanl.micot.application.utilities.json.javax;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.lanl.micot.application.fragility.core.*;
import gov.lanl.micot.application.fragility.io.GFMDataWriter;
import gov.lanl.micot.application.utilities.asset.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static gov.lanl.micot.application.rdt.PoleConstants.*;

/**
 * Class that manages RDT functionality from RDT input, inferring pole data to geometry objects, and
 */
public class JacksonJSONOperations {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode fileNodes = null;
    private GeometryObjectFactory geoObjectBuilder = new GeometryObjectFactory();
    private ArrayList<GeometryObject> geometryObjects = new ArrayList<>();
    private List<Map<String, PropertyData>> properties = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();
    private ArrayNode responsesArray = mapper.createArrayNode();

    private static String polesOutputPath = "poles_output.json";

    public JacksonJSONOperations() {
    }

    /**
     * Method for reading and parsing JSON data into appropriate field data structures
     *
     * @param FileLocation string file location
     */
    public void readJsonFile(String FileLocation) {

        InputStream inStream;

        try {
            inStream = new FileInputStream(FileLocation);
            this.fileNodes = this.objectMapper.readTree(inStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // loops through asset file, adding the appropriate GeometryObjects to var geometryObjects
        for (JsonNode n : this.fileNodes.findValue("features")) {
            JsonNode coordNode = n.get("geometry").get("coordinates");
            String geoType = n.get("geometry").get("type").asText();
            String identifier = n.get("properties").get("id").asText();
            // parse JSON properties
            parseProperties(n.get("properties"));

            GeometryObject g = this.geoObjectBuilder.getGeometry(geoType, identifier);

            if (g instanceof GeometryPoint) {
                addPoint(g, coordNode);
            } else if (g instanceof GeometryLineString) {
                addLineString(g, coordNode);
            } else if (g instanceof GeometryMultiPoint) {
                addLineString(g, coordNode);
            } else {
                System.out.println("WARNING: " + g.getIdentifier() + " Geometry type is not implemented");
            }
        }
    }

    /**
     * Helper Method for parsing property member values from JSON.
     *
     * @param p
     */
    private void parseProperties(JsonNode p) {

        Iterator<Map.Entry<String, JsonNode>> pIterator = p.fields();
        Map<String, PropertyData> property = new HashMap<>();

        while (pIterator.hasNext()) {

            Map.Entry<String, JsonNode> entry = pIterator.next();
            PropertyData propertyField = null;

            if (entry.getValue().isInt()) {
                propertyField = new IntegerValue(entry.getValue().asInt(-9999));
            } else if (entry.getValue().isNumber()) {
                propertyField = new DoubleValue(entry.getValue().asDouble(-9999.0));
            } else if (entry.getValue().isBoolean()) {
                propertyField = new BooleanValue(entry.getValue().asBoolean(false));
            } else if (entry.getValue().isTextual()) {
                propertyField = new StringValue(entry.getValue().asText("no value specified"));
            } else {
                propertyField = null;
                System.out.println(entry.getValue().asText());
            }

            property.put(entry.getKey(), propertyField);
        }
        properties.add(property);
    }

    /**
     * Getter method for property data
     *
     * @return List of property data that is mapped by id values
     */
    public List<Map<String, PropertyData>> getPropertyObjects() {
        return properties;
    }

    /**
     * This method stores a longitude and latitude values into a <tt>GeometryPoint<tt/> object.
     *
     * @param g         <tt>GeometryObject<tt/>  that is used to store longitude and latitude locations
     * @param coordNode JsonNode container for longitude and latitude values
     */
    private void addPoint(GeometryObject g, JsonNode coordNode) {

        List<double[]> crd = new ArrayList<>();
        double[] coordHolder = new double[2];

        coordHolder[0] = coordNode.get(0).asDouble();
        coordHolder[1] = coordNode.get(1).asDouble();
        crd.add(coordHolder);
        g.setCoordinates(crd);

        this.geometryObjects.add(g);

    }

    /**
     * This method stores a longitude and latitude values into a <tt>GeometryLineString<tt/> object.
     *
     * @param g         <tt>GeometryObject<tt/> that is used to store longitude and latitude locations
     * @param coordNode JsonNode container for longitude and latitude values
     */
    private void addLineString(GeometryObject g, JsonNode coordNode) {

        List<double[]> crd = new ArrayList<>();
        double[] coordHolder;

        for (int i = 0; i < coordNode.size(); i++) {
            coordHolder = new double[2];
            coordHolder[0] = coordNode.get(i).get(0).asDouble();
            coordHolder[1] = coordNode.get(i).get(1).asDouble();
            crd.add(coordHolder);
        }

        g.setCoordinates(crd);
        this.geometryObjects.add(g);

    }

    public ArrayList<GeometryObject> getGeometryData() {
        return geometryObjects;
    }

    /**
     * General routine that writes a JSON array to specified file path.  If file path null
     * then default file name "fragility_output.json" is given.
     *
     * @param objs
     * @param filePath
     */
    public static void writePoleOutput(ObjectNode objs, String filePath) {

        if (!(filePath == null)) {
            polesOutputPath = filePath;
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try {
            writer.writeValue(new File(polesOutputPath), objs);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Helper method that stores estimator responses into an intermediate ArrayNode, then calls
     * GFMDataWriter method to write results.
     *
     * @param responses      HashMap of asset identifiers and response values.
     * @param fileOutputPath String file path to write JSON results.
     */
    public void writeJSON(HashMap<String, Double> responses, String fileOutputPath) {
        responses.forEach((k, v) -> {
            ObjectNode singleNode = mapper.createObjectNode()
                    .put("id", k)
                    .put("value", v);
            responsesArray.add(singleNode);
        });

        System.out.println("Writing response estimators");
        GFMDataWriter.writeResults(responsesArray, fileOutputPath);
    }

    public void readWriteRDTJSON(String filePath, String polesOutputPath) {

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

        for (JsonNode n : fileNodes.findValue("lines")) {

            boolean xfmrExist = false;
            JsonNode xmfValue = n.findValue("is_transformer");

            // member may not exist, so checking
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

                    ObjectNode obj;
                    for (int i = 0; i < numPoles; i++) {
                        obj = createPoleAsset(id_count, lid, new double[]{x0, y0}, lid);

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
        }


        System.out.println("Number of poles created: " + id_count);

        // create geoJSON Feature Collection
        ObjectNode featureNode = mapper.createObjectNode()
                .put("type", "FeatureCollection")
                .putPOJO("features", features);

        writePoleOutput(featureNode, polesOutputPath);
    }

    private ObjectNode createPoleAsset(int id, String line, double[] coord, String lid) {

        String sid = String.valueOf(id);
        // creates one GeoJSON Point Object
        ObjectNode featureNode = objectMapper.createObjectNode()
                .putPOJO("geometry", objectMapper.createObjectNode()
                        .put("type", "Point")
                        .putPOJO("coordinates", objectMapper.createArrayNode()
                                .add(coord[0])
                                .add(coord[1])
                        ))
                .put("type", "Feature")

                .putPOJO("properties", objectMapper.createObjectNode()
                        .put("id", sid)
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
                        .put("powerLineId", line));

        return featureNode;

    }

    public void writeRDTScenarios(Map<Integer, List<String>> lineScenarios, String scenarioFilePath) {

        ArrayNode scenarioArray = mapper.createArrayNode();

        lineScenarios.forEach((k, v) -> {

            ArrayNode lineIdentifier = mapper.valueToTree(v);

            ObjectNode singleScenario = mapper.createObjectNode()
                    .put("id", String.valueOf(k))
                    .putPOJO("hardened_disabled_lines", mapper.createArrayNode())
                    .putPOJO("disable_lines", lineIdentifier);

            scenarioArray.add(singleScenario);

        });

        JsonNode allScenarios = mapper.createObjectNode()
                .putPOJO("scenarios", scenarioArray);

        GFMDataWriter.writeResults(allScenarios, scenarioFilePath);

    }

}
