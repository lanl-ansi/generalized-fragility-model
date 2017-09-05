package gov.lanl.nisac.fragility.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Coordinate;
import gov.lanl.nisac.fragility.io.GFMFileWriter;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.HashMap;


public class GFMEngine {

    private ArrayList<HazardField> hazardFields;
    private ArrayList<GeometryObject> geometryObjects;
    private ArrayList<JsonNode> assetProperties;
    private ArrayList<HashMap<String, HashMap<String, Double>>> exposures = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();
    private ArrayNode array = mapper.createArrayNode();

    public void produceExposures() {

        // create complex hashing scheme - general way to associate hazard keys with arbitrarily many values.

        CoordinateReferenceSystem crs;
        double x;       // longitude
        double y;       // latitude
        double[] r;     // exposures value
        int index = 0;  // index for each hazard field


        for (HazardField h : hazardFields) {
            // getting coordinate reference system
            crs = h.getField().getCoordinateReferenceSystem2D();

            exposures.add(new HashMap<>());
            exposures.get(index).put(h.getFileLocation(), new HashMap<>());

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
                    r = new double[1];
                    r[0] = -9999.0;
                }

                // add to exposures collection
                exposures.get(index).get(h.getFileLocation()).put(g.getIdentifier(), r[0]);
            }
            index = index + 1;
        }
    }


    public void setGeometryObjects(ArrayList<GeometryObject> geometryObjects) {
        this.geometryObjects = geometryObjects;
    }

    public void setHazardfields(ArrayList<HazardField> hazardfields) {
        this.hazardFields = hazardfields;
    }

    public void setAssetProperties(ArrayList<JsonNode> assetProperties) {
        this.assetProperties = assetProperties;
    }

    public ArrayList<JsonNode> getAssetProperties() {
        return assetProperties;
    }

    public ArrayList<HashMap<String, HashMap<String, Double>>> getExposures(){
        return exposures;
    }

    public void writeResults(HashMap<String, Double> responses) {

        responses.forEach((k,v)->{
            ObjectNode singleNode = mapper.createObjectNode().put("id", k)
                    .put("value", v);
            array.add(singleNode);
        });

        System.out.println("-->> " + array);

        GFMFileWriter.writeSomething(array,
                "C:\\Users\\301338\\Desktop\\PROJECTS\\code_development\\micot-GFM\\f_OUTPUT.json");

    }

}
