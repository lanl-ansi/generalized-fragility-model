package gov.lanl.nisac.fragility.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Coordinate;
import gov.lanl.nisac.fragility.io.GFMDataWriter;
import org.geotools.geometry.jts.JTS;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages and mediates functionality between geometry objects, hazard fields, and response estimators
 */
public class GFMEngine {

    private ArrayList<HazardField> hazardFields;
    private ArrayList<GeometryObject> geometryObjects;
    private ArrayList<JsonNode> assetProperties;

    private Map<String, HashMap<String, ArrayList<Double>>> exposures = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();
    private ArrayNode reponsesArray = mapper.createArrayNode();

    private int outsideExtentCount;
    CoordinateReferenceSystem crs;

    /**
     * method that extracts exposure values from hazard fields to geometry object identifiers
     */
    public void produceExposures() {

        // hazard field string identifier
        String hazardName = null;

        for (HazardField h : hazardFields) {
            // getting coordinate reference system
            crs = h.getField().getCoordinateReferenceSystem2D();

            // get hazard field identifier
            hazardName = h.getIdentifier();

            // create new HashMap for each identifier/hazard field
            exposures.put(hazardName, new HashMap<>());

            // counter for geometry objects within field extent
            outsideExtentCount = 0;

            // for each geometry object, extract field exposure value
            for (GeometryObject g : geometryObjects) {

                if (g instanceof GeometryPoint) {
                    addGeometryPoint(g, h, hazardName);
                } else if (g instanceof GeometryLineString) {
                    addGeometryLineString(g, h, hazardName);
                }
            }

            if (outsideExtentCount > 0) {
                System.out.println(" -- > WARNING - " + outsideExtentCount +
                        " objects outside of hazard domain " + h.getFileName());
                System.out.println("      Used default exposure value of 0.0");
            }
        }
    }

    private void addGeometryPoint(GeometryObject g, HazardField h, String hazardName) {

        double x;       // longitude
        double y;       // latitude
        double[] r;     // exposures value

        // getting coordinate responsesArray list: (lon, lat)
        List<double[]> crd = g.getCoordinates();
        // initialize new array list
        exposures.get(hazardName).put(g.getIdentifier(), new ArrayList<>());

        x = crd.get(0)[0];
        y = crd.get(0)[1];
        Coordinate crds = new Coordinate(x, y);
        DirectPosition p = JTS.toDirectPosition(crds, crs);


        try {
            r = h.getField().evaluate(p, new double[1]);
        } catch (org.opengis.coverage.PointOutsideCoverageException e) {
            // if outside coverage, set to default value of 0.0
            outsideExtentCount += 1;
            r = new double[1];
            r[0] = 0.0;
        }

        // add point to exposures collection
        exposures.get(hazardName).get(g.getIdentifier()).add(r[0]);

    }

    private void addGeometryLineString(GeometryObject g, HazardField h, String hazardName) {

        double x;       // longitude
        double y;       // latitude
        double[] r;     // exposures value

        // getting coordinate responsesArray list: (lon, lat)
        List<double[]> crd = g.getCoordinates();

        // initialize new array list
        exposures.get(hazardName).put(g.getIdentifier(), new ArrayList<>());

        for (double[] aCrd : crd) {

            x = aCrd[0];
            y = aCrd[1];

            Coordinate crds = new Coordinate(x, y);
            DirectPosition p = JTS.toDirectPosition(crds, crs);

            try {
                r = h.getField().evaluate(p, new double[1]);
            } catch (PointOutsideCoverageException e) {
                // if outside coverage, set to default value of 0.0
                outsideExtentCount += 1;
                r = new double[1];
                r[0] = 0.0;
            }
            // add point exposure to collection
            exposures.get(hazardName).get(g.getIdentifier()).add(r[0]);
        }
    }

    /**
     * method to set a collection geometry objects
     *
     * @param geometryObjects - list of geometry objects
     */
    public void setGeometryObjects(ArrayList<GeometryObject> geometryObjects) {
        this.geometryObjects = geometryObjects;
    }

    /**
     * method to set a collection of hazard fields
     *
     * @param hazardfields - list of hazard fields
     */
    public void setHazardfields(ArrayList<HazardField> hazardfields) {
        System.out.println(hazardfields.size() + " hazard fields read");
        hazardFields = hazardfields;
    }

    /**
     * Method to set asset properties
     *
     * @param assetProperties - list of asset properties
     */
    public void setAssetProperties(ArrayList<JsonNode> assetProperties) {
        System.out.println(assetProperties.size() + " assets read");
        this.assetProperties = assetProperties;
    }

    public ArrayList<JsonNode> getAssetProperties() {
        return assetProperties;
    }

    public Map<String, HashMap<String, ArrayList<Double>>> getExposures() {
        return exposures;
    }

    /**
     * Store results into a hashmap, id --> value
     *
     * @param responses      - a response hashmap of recorded response estimations
     * @param fileOutputPath - absolute file location for output data
     */
    public void storeResults(HashMap<String, Double> responses, String fileOutputPath) {

        responses.forEach((k, v) -> {
            ObjectNode singleNode = mapper.createObjectNode().put("id", k)
                    .put("value", v);
            reponsesArray.add(singleNode);
        });

        System.out.println("Writing response estimators");
        GFMDataWriter.writeResults(reponsesArray, fileOutputPath);
    }

    public ArrayNode getResponsesArray() {
        return this.reponsesArray;
    }
}
