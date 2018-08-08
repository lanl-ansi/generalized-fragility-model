package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.utilities.json.AssetDataFromJackson;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.gis.HazardField;
import gov.lanl.micot.application.utilities.json.JsonDataFromJackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * This class manages and mediates functionality between geometry objects, hazard fields, and response estimators
 */
public class GFMEngine {

    private ArrayList<HazardField> hazardFields;
    private ArrayList<GeometryObject> geometryObjects;
    private List<Map<String, PropertyData>> assetProperties;
    private Map<String, HashMap<String, ArrayList<Double>>> exposures = new HashMap<>();
    
    private AssetDataFromJackson jsonData = new JsonDataFromJackson();
    private int outsideExtentCount;
    private HashMap<String, Double> assetResponses;

    /**
     * Method that extracts exposure values from hazard fields to geometry object identifiers
     */
    public void produceExposures() {

        // hazard field string identifier
        String hazardName = null;

        for (HazardField h : hazardFields) {

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
                } else if (g instanceof GeometryMultiPoint) {
                    addGeometryMultiPoint(g, h, hazardName);
                }
            }

            // get number of assets outside given raster extent
            outsideExtentCount = h.getOutsideExtentCount();

            if (outsideExtentCount > 0) {
                System.out.println(" -- > WARNING - " + outsideExtentCount +
                        " objects outside of hazard extent: " + h.getIdentifier());
                System.out.println("      Used default exposure value of 0.0");
            }
        }
    }

    /**
     * This method associates hazard field values to assets.
     *
     * @param g <tt>addGeometryMultiPoint</tt> MultiPoint object
     * @param h <tt>HazardField</tt> object
     * @param hazardName String hazard field identifier
     */
    private void addGeometryMultiPoint(GeometryObject g, HazardField h, String hazardName) {
        // get raster value at latitude/longitude coordinates
        List<Double> rasterValues = h.evaluatePoints(g.getCoordinates());

        // initialize new array list
        exposures.get(hazardName).put(g.getIdentifier(), new ArrayList<>());

        for (Double rv : rasterValues) {
            // add point exposure to collection
            exposures.get(hazardName).get(g.getIdentifier()).add(rv);
        }

    }

    /**
     * This method associates hazard field values to assets.
     *
     * @param g <tt>GeometryPoint</tt> Point object
     * @param h <tt>HazardField</tt> object
     * @param hazardName String hazard field identifier
     */
    private void addGeometryPoint(GeometryObject g, HazardField h, String hazardName) {

        // initialize new array list
        exposures.get(hazardName).put(g.getIdentifier(), new ArrayList<>());

        // get raster value at latitude/longitude coordinates
        double rasterValue = h.evaluatePoint(g.getCoordinates());

        // add point to exposures collection
        exposures.get(hazardName).get(g.getIdentifier()).add(rasterValue);

    }

    /**
     * This method associates hazard field values to assets.
     *
     * @param g <tt>LineString</tt> Point object
     * @param h <tt>HazardField</tt> object
     * @param hazardName String hazard field identifier
     */
    private void addGeometryLineString(GeometryObject g, HazardField h, String hazardName) {

        // get raster value at latitude/longitude coordinates
        List<Double> rasterValues = h.evaluatePoints(g.getCoordinates());

        // initialize new array list
        exposures.get(hazardName).put(g.getIdentifier(), new ArrayList<>());

        for (Double rv : rasterValues) {
            // add point exposure to collection
            exposures.get(hazardName).get(g.getIdentifier()).add(rv);
        }


    }


    /**
     * Method to set a collection geometry objects
     *
     * @param geometryObjects - list of geometry objects
     */
    public void setGeometryObjects(ArrayList<GeometryObject> geometryObjects) {
        this.geometryObjects = geometryObjects;
    }

    /**
     * Method to get a collection geometry objects
     * @return geometryObjects;
     */
    public ArrayList<GeometryObject> getGeometryObjects() {
        return this.geometryObjects;
    }

    /**
     * Method to set a collection of hazard fields
     *
     * @param hazardfields list of hazard fields
     */
    public void setHazardfields(ArrayList<HazardField> hazardfields) {
        System.out.println(hazardfields.size() + " hazard fields read");
        this.hazardFields = hazardfields;
    }

    /**
     * Method to get the collection of hazard fields
     *
     * @return hazardfields list of hazard fields
     */
    public ArrayList<HazardField> getHazardfields() {

        return this.hazardFields;
    }

    /**
     * Method to set asset properties
     *
     * @param assetProperties list of asset properties
     */
    public void setAssetProperties(List<Map<String, PropertyData>> assetProperties) {
        System.out.println(assetProperties.size() + " assets read");
        this.assetProperties = assetProperties;
    }

    /**
     * Method to access asset properties
     * @return array list of type JsonNode
     */
    public List<Map<String, PropertyData>> getAssetProperties() {
        return assetProperties;
    }

    /**
     * Method  to access an hashmap of exposure values
     * @return hashmap
     */
    public Map<String, HashMap<String, ArrayList<Double>>> getExposures() {
        return exposures;
    }

    /**
     * Method to pass results into a JSON Writer
     *
     * @param responses      - a response hashmap of recorded response estimations
     * @param fileOutputPath - absolute file location for output data
     */
    public void writeJSONOutputs(HashMap<String, Double> responses, String fileOutputPath) {
        assetResponses = responses;
        jsonData.writeJSON(responses, fileOutputPath);
    }

    public void writeCSVOutputs(HashMap<String, Double> responses, String fileOutputPath) {
        assetResponses = responses;

        if (fileOutputPath == null){
            fileOutputPath = "fragility_output.csv";
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileOutputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.write("AssetID,DamageProbability\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (Map.Entry<String,Double> entry : responses.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            try {
                writer.write(entry.getKey());
                writer.write(",");
                writer.write(entry.getValue().toString());
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to access an arrayNode of responses
     * @return arrayNode
     */
    public HashMap<String, Double> getResponsesArray() {
        return this.assetResponses;
    }
}
