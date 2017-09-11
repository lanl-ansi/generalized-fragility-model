package gov.lanl.nisac.fragility.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Coordinate;
import gov.lanl.nisac.fragility.io.GFMFileWriter;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GFMEngine {

    private ArrayList<HazardField> hazardFields;
    private ArrayList<GeometryObject> geometryObjects;
    private ArrayList<JsonNode> assetProperties;
    private HashMap<String, HashMap<String, Double>> exposures = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();
    private ArrayNode array = mapper.createArrayNode();

    public void produceExposures() {

        // create complex hashing scheme - general way to associate hazard keys with arbitrarily many values.

        CoordinateReferenceSystem crs;
        double x;       // longitude
        double y;       // latitude
        double[] r;     // exposures value
        int index = 0;  // index for each hazard field

        String hazardName = null;
        exposures.put(hazardName, new HashMap<>());

        for (HazardField h : hazardFields) {
            // getting coordinate reference system
            crs = h.getField().getCoordinateReferenceSystem2D();

            // get hazard field identifier - currently using the file name
            hazardName = h.getFileName().toString();
            exposures.put(hazardName, new HashMap<>());
            int count = 0;

            for (GeometryObject g : geometryObjects) {

                // getting coordinate array list: (lon, lat)
                ArrayList<double[]> crd = g.getCoordinates();

                // TODO: generalize for LineStrings, Polygons, etc.
                x = crd.get(0)[0];
                y = crd.get(0)[1];
                Coordinate crds = new Coordinate(x, y);
                DirectPosition p = JTS.toDirectPosition(crds, crs);

                try {
                    r = h.getField().evaluate(p, new double[1]);
                } catch (org.opengis.coverage.PointOutsideCoverageException e) {
                    // if outside coverage, set to default value
                    count = count + 1;
                    r = new double[1];
                    r[0] = 0.0;
                }

                // add to exposures collection
                exposures.get(hazardName).put(g.getIdentifier(), r[0]);
            }
            index = index + 1;

            if (count > 0) {
                System.out.println(" -- > WARNING - " + count + " objects outside of hazard domain " + h.getFileName());
                System.out.println("      Used default exposure value of 0.0");
            }

        }
    }


    public void setGeometryObjects(ArrayList<GeometryObject> geometryObjects) {
        this.geometryObjects = geometryObjects;
    }

    public void setHazardfields(ArrayList<HazardField> hazardfields) {
        System.out.println(hazardfields.size() + " hazard fields read");
        hazardFields = hazardfields;
    }

    public void setAssetProperties(ArrayList<JsonNode> assetProperties) {
        System.out.println(assetProperties.size() + " assets read");
        this.assetProperties = assetProperties;
    }

    public ArrayList<JsonNode> getAssetProperties() {
        return assetProperties;
    }

    public HashMap<String, HashMap<String, Double>> getExposures() {
        return exposures;
    }

    public void writeResults(HashMap<String, Double> responses) {

        responses.forEach((k, v) -> {
            ObjectNode singleNode = mapper.createObjectNode().put("id", k)
                    .put("value", v);
            array.add(singleNode);
        });
        System.out.println("Writing response estimators");

        GFMFileWriter.writeSomething(array,
                "C:\\Users\\301338\\Desktop\\PROJECTS\\code_development\\micot-GFM\\f_OUTPUT.json");
    }

}
