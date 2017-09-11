package gov.lanl.nisac.fragility.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.lanl.nisac.fragility.core.*;
import org.geotools.coverage.grid.GridCoverage2D;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public final class GFMDataReader {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode fileNodes;
    private static GeometryObjectFactory geoObjectBuilder = new GeometryObjectFactory();
    private static HazardFieldFactory hazardObjectBuilder = new HazardFieldFactory();

    private static ArrayList<JsonNode> properties = new ArrayList<>();
    private static ArrayList<GeometryObject> geometryObjects = new ArrayList<>();
    private static ArrayList<HazardField> hazardFields = new ArrayList<>();
    private static HashMap<String, GridCoverage2D> hazardMap = new HashMap<>();

    private GFMDataReader() {
    }

    public static ArrayList<GeometryObject> getGeometryObjects(){
        return geometryObjects;
    }

    public static ArrayList<JsonNode> getProperties(){
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

        double[] coordHolder;
        ArrayList<double[]> crd = null;


        for (JsonNode n : fileNodes.findValue("features")) {
            JsonNode n1 = n.get("geometry").get("coordinates");
            String geoType = n.get("geometry").get("type").asText();
            String identifier = n.get("properties").get("id").asText();

            properties.add( n.get("properties") );

            GeometryObject g = geoObjectBuilder.getGeometry(geoType, identifier);
            crd = new ArrayList<>();
            coordHolder = new double[2];

            coordHolder[0] = n1.get(0).asDouble();
            coordHolder[1] = n1.get(1).asDouble();
            crd.add(coordHolder);
            g.setCoordinates(crd);

            geometryObjects.add(g);
        }
    }


    /**
     * TODO: generalize for multiple command line arguments
     * @param fileName - hazard file location
     * @return ArrayList<HazardField>
     */
    public static ArrayList<HazardField> readHazardFile(String fileName) {

        HazardField hf = hazardObjectBuilder.getHazardField(fileName);

        hazardMap.put(hf.getFileName(), hf.getField());
        hazardFields.add(hf);

        return hazardFields;
    }

}
