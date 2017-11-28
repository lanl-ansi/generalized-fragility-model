package gov.lanl.micot.application.utilities.json.javax;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.lanl.micot.application.fragility.core.*;
import gov.lanl.micot.application.utilities.asset.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JacksonJSONArray {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode fileNodes = null;
    private GeometryObjectFactory geoObjectBuilder = new GeometryObjectFactory();
    private ArrayList<GeometryObject> geometryObjects = new ArrayList<>();
    private List<Map<String, PropertyData>> properties = new ArrayList<>();

    public JacksonJSONArray() {
    }

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

    private void parseProperties(JsonNode p) {

        Iterator<Map.Entry<String, JsonNode>> pIterator = p.fields();
        Map<String, PropertyData> property = new HashMap<>();

        while (pIterator.hasNext()) {

            Map.Entry<String, JsonNode> entry = pIterator.next();

            PropertyData propertyField = null;


            if (entry.getValue().isInt()) {
                propertyField = new IntegerValue(entry.getValue().asInt(-9999));
            } else if (entry.getValue().isNumber()) {
                propertyField = new DoubleValue(entry.getValue().asDouble());
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

}
