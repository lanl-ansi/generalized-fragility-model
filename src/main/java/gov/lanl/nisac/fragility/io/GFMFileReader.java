package gov.lanl.nisac.fragility.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.lanl.nisac.fragility.GFMcore.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public final class GFMFileReader {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode fileNodes;
    private static GeometryObjectFactory geoObjectBuilder = new GeometryObjectFactory();
    private static HazardFieldFactory hazObjectBuilder = new HazardFieldFactory();

    private GFMFileReader() {}

    public static ArrayList<GeometryObject> readGeoJsonFile(String FileLocation, GFMEngine broker) {

        InputStream inStream;

        try {
            inStream = new FileInputStream(FileLocation);
            setObjectMapper(objectMapper.readTree(inStream));

        } catch (IOException e) {
            e.printStackTrace();
        }

        double[] coordHolder;
        ArrayList<GeometryObject> geoObj = new ArrayList<>();
        ArrayList<HazardField> hazObj = new ArrayList<>();
        ArrayList<double[]> crd = null;

        for (JsonNode n : fileNodes.findValue("features")) {
            JsonNode n1 = n.get("geometry").get("coordinates");
            String geoType = n.get("geometry").get("type").asText();
//            String identifier = n.get("properties").get("id").asText("-99999");
            String identifier = n.get("id").asText("-99999");

            GeometryObject g = geoObjectBuilder.getGeometry(geoType, identifier, broker);
            crd = new ArrayList<>();
            coordHolder = new double[2];

            coordHolder[0] = n1.get(0).asDouble();
            coordHolder[1] = n1.get(1).asDouble();
            crd.add(coordHolder);
            g.setCoordinates(crd);

            geoObj.add(g);
        }

        return geoObj;
    }

    private static void setObjectMapper(JsonNode jsonNodes) {
        fileNodes = jsonNodes;
    }


    public static ArrayList<HazardField> readHazardFile(String fileName, GFMEngine broker){
        ArrayList<HazardField> hazObj = new ArrayList<>();
        hazObjectBuilder = new HazardFieldFactory();

        HazardField hazfld  = hazObjectBuilder.getHazardField(fileName, broker);
        hazObj.add(hazfld);

        return hazObj;
    }

}
