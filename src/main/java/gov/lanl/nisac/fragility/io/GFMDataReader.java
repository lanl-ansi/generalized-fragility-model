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

    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode fileNodes = null;
    private GeometryObjectFactory geoObjectBuilder = new GeometryObjectFactory();
    private HazardFieldFactory hazardObjectBuilder = new HazardFieldFactory();

    private ArrayList<JsonNode> properties = new ArrayList<>();
    private ArrayList<GeometryObject> geometryObjects = new ArrayList<>();
    private ArrayList<HazardField> hazardFields = new ArrayList<>();

    public GFMDataReader() {
    }

    /**
     * Method for accessing an array list of GeometryObjects.
     *
     * @return array list of GeometryObjects
     */
    public ArrayList<GeometryObject> getGeometryObjects() {
        return this.geometryObjects;
    }

    /**
     * Method for accessing an array list of JsonNode objects.
     *
     * @return array list of GeometryObjects
     */
    public ArrayList<JsonNode> getProperties() {
        return this.properties;
    }

    /**
     * Method that reads in a geojson file.
     *
     * @param FileLocation string formatted file location
     */
    public void readGeoJsonFile(String FileLocation) {

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

            this.properties.add(n.get("properties"));

            GeometryObject g = this.geoObjectBuilder.getGeometry(geoType, identifier);

            if (g instanceof GeometryPoint) {
                addPoint(g, coordNode);
            } else if (g instanceof GeometryLineString) {
                addLineString(g, coordNode);
            }
        }
    }

    private void addPoint(GeometryObject g, JsonNode coordNode) {

        List<double[]> crd = new ArrayList<>();
        double[] coordHolder = new double[2];

        coordHolder[0] = coordNode.get(0).asDouble();
        coordHolder[1] = coordNode.get(1).asDouble();
        crd.add(coordHolder);
        g.setCoordinates(crd);

        this.geometryObjects.add(g);

    }

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

    public ArrayList<HazardField> readHazardFile(String[] fileName, String[] id) {

        int numberFiles = fileName.length;
        int numberIdentifiers = id.length;

        if (numberFiles != numberIdentifiers) {
            System.out.println("EXITING:  Number of files and identifiers aren't equal");
            System.exit(2);
        }

        for (int i = 0; i < numberFiles; i++) {
            HazardField hf = hazardObjectBuilder.getHazardField(fileName[i], id[i]);
            this.hazardFields.add(hf);
        }

        return this.hazardFields;
    }

}
