package gov.lanl.nisac.fragility.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Coordinate;
import gov.lanl.nisac.fragility.io.GFMDataWriter;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages functionality between geometry objects and hazard fields
 */
public class GFMEngine {

    private ArrayList<HazardField> hazardFields;
    private ArrayList<GeometryObject> geometryObjects;
    private ArrayList<JsonNode> assetProperties;
    private HashMap<String, HashMap<String, Double>> exposures = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();
    private ArrayNode reponsesArray = mapper.createArrayNode();

    /**
     * general methods that extracts exposure values from hazard fields to geomtry object identifiers
     */
    public void produceExposures() {

        // hashing scheme - general way to associate hazard keys with arbitrarily many values.
        CoordinateReferenceSystem crs; // coordinate reference system
        double x;       // longitude
        double y;       // latitude
        double[] r;     // exposures value

        String hazardName = null;

        for (HazardField h : hazardFields) {
            // getting coordinate reference system
            crs = h.getField().getCoordinateReferenceSystem2D();

            // get hazard field identifier
            hazardName = h.getIdentifier();

            // create new HashMap for each identifier/hazard field
            exposures.put(hazardName, new HashMap<>());

            // counter for geometry objects within field extent
            int count = 0;

            // for each geometry object, extract field exposure value
            for (GeometryObject g : geometryObjects) {

                // getting coordinate reponsesArray list: (lon, lat)
                ArrayList<double[]> crd = g.getCoordinates();

                // TODO: generalize for LineStrings, Polygons, etc.
                x = crd.get(0)[0];
                y = crd.get(0)[1];
                Coordinate crds = new Coordinate(x, y);
                DirectPosition p = JTS.toDirectPosition(crds, crs);

                try {
                    r = h.getField().evaluate(p, new double[1]);
                } catch (org.opengis.coverage.PointOutsideCoverageException e) {
                    // if outside coverage, set to default value of 0.0
                    count = count + 1;
                    r = new double[1];
                    r[0] = 0.0;
                }

                // add to exposures collection
                exposures.get(hazardName).put(g.getIdentifier(), r[0]);
            }

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

    /**
     * Store results into a hashmap, id --> value
     * @param responses a response hashmap of recorded response estimations
     * @param fileOutputPath absolute file location for output data
     */
    public void storeResults(HashMap<String, Double> responses, String fileOutputPath) {

        responses.forEach((k, v) -> {
            ObjectNode singleNode = mapper.createObjectNode().put("id", k)
                    .put("value", v);
            reponsesArray.add(singleNode);
        });

//        exposures.forEach((id, value) -> {
//            value.forEach((k,v)->{
//                System.out.println(id+" "+k+" " +v);
//            });
//        });

        System.out.println("Writing response estimators");
        GFMDataWriter.writeResults(reponsesArray, fileOutputPath);
    }

    public ArrayNode getResponsesArray(){
        return this.reponsesArray;
    }
}
