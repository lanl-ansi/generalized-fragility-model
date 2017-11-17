package gov.lanl.micot.application.fragility.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.lanl.micot.application.fragility.core.*;
import gov.lanl.micot.application.utility.gis.RasterField;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods to read formatted files and stores them in local field variables.
 *
 * @author Trevor Crawford
 */
public class GFMDataReader {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode fileNodes = null;
    private GeometryObjectFactory geoObjectBuilder = new GeometryObjectFactory();

    private ArrayList<JsonNode> properties = new ArrayList<>();
    private ArrayList<GeometryObject> geometryObjects = new ArrayList<>();
    private ArrayList<RasterField> hazardFields = new ArrayList<>();

    /**
     * Default constructor.
     */
    public GFMDataReader() {
    }

    /**
     * Method for accessing an array list of <tt>GeometryObjects</tt>.
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
     * Method that reads in a GeoJSON file by file location.
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
            } else if (g instanceof GeometryMultiPoint) {
                addLineString(g, coordNode);
            } else {
                System.out.println("WARNING: " + g.getIdentifier() + " Geometry type is not implemented");
            }
        }
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

    /**
     * @param fileName String array of file locations
     * @param id       string array of identifiers in tandem with <tt>fileName<tt/>
     * @return an array list of type <tt>HazardField<tt/>
     */
    public ArrayList<RasterField> readHazardFile(String[] fileName, String[] id) {

        int numberFiles = (fileName == null) ? 0 : fileName.length;
        int numberIdentifiers = (id == null) ? 0 : id.length;

        if (numberFiles != numberIdentifiers) {
            System.out.println("EXITING:  Number of files and identifiers aren't equal");
            System.exit(2);
        }

        for (int i = 0; i < numberFiles; i++) {
            RasterField hf = new RasterField(fileName[i], id[i]);
            this.hazardFields.add(hf);
        }

        return this.hazardFields;
    }

}
