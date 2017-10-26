package gov.lanl.nisac.fragility.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.lanl.nisac.fragility.core.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GFMDataReader {
    // TODO: make this class not static

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode fileNodes = null;
    private static GeometryObjectFactory geoObjectBuilder = new GeometryObjectFactory();
    private static HazardFieldFactory hazardObjectBuilder = new HazardFieldFactory();

    private static ArrayList<JsonNode> properties = new ArrayList<>();
    private static ArrayList<GeometryObject> geometryObjects = new ArrayList<>();
    private static ArrayList<HazardField> hazardFields = new ArrayList<>();

    private GFMDataReader() {
    }

    public static ArrayList<GeometryObject> getGeometryObjects() {
        return geometryObjects;
    }

    public static ArrayList<JsonNode> getProperties() {
        return properties;
    }

    public static void readGeoJsonFile(String FileLocation) {

        InputStream inStream;

        try {
            inStream = new FileInputStream(FileLocation);
            fileNodes = objectMapper.readTree(inStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


        for (JsonNode n : fileNodes.findValue("features")) {
            JsonNode coordNode = n.get("geometry").get("coordinates");
            String geoType = n.get("geometry").get("type").asText();
            String identifier = n.get("properties").get("id").asText();

            properties.add(n.get("properties"));

            GeometryObject g = geoObjectBuilder.getGeometry(geoType, identifier);

            if (g instanceof GeometryPoint) {
                addPoint(g, coordNode);
            } else if (g instanceof GeometryLineString) {
                addLineString(g, coordNode);
            }
        }
    }

    private static void addPoint(GeometryObject g, JsonNode coordNode) {

        List<double[]> crd = new ArrayList<>();
        double[] coordHolder = new double[2];

        coordHolder[0] = coordNode.get(0).asDouble();
        coordHolder[1] = coordNode.get(1).asDouble();
        crd.add(coordHolder);
        g.setCoordinates(crd);

        geometryObjects.add(g);

    }

    private static void addLineString(GeometryObject g, JsonNode coordNode) {

        List<double[]> crd = new ArrayList<>();
        double[] coordHolder;

        for (int i = 0; i < coordNode.size(); i++) {
            coordHolder = new double[2];
            coordHolder[0] = coordNode.get(i).get(0).asDouble();
            coordHolder[1] = coordNode.get(i).get(1).asDouble();
            crd.add(coordHolder);
        }

        g.setCoordinates(crd);
        geometryObjects.add(g);

    }

    public static ArrayList<HazardField> readHazardFile(String[] fileName, String[] id) {

        int numberFiles = fileName.length;
        int numberIdentifiers = id.length;

        if (numberFiles != numberIdentifiers) {
            System.out.println("EXITING:  Number of files and identifiers aren't equal");
            System.exit(2);
        }

        for (int i = 0; i < numberFiles; i++) {
            HazardField hf = hazardObjectBuilder.getHazardField(fileName[i], id[i]);
            hazardFields.add(hf);
        }

        return hazardFields;
    }

}
