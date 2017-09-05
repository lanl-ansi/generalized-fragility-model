package gov.lanl.nisac.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.GFMEngine;
import gov.lanl.nisac.fragility.core.ResponseEstimator;

import java.util.ArrayList;
import java.util.HashMap;

public class PowerPoleWindStress implements ResponseEstimator {

    private GFMEngine broker;
    private HashMap<String, Double> responses;
    private ArrayList<JsonNode> assets;

    public PowerPoleWindStress(GFMEngine broker) {
        this.broker = broker;
        this.assets = broker.getAssetProperties();

        // calculate fragility in this method
        calcFragility();

        // send back to GFM Engine to write results
        writeResults();
    }

    /**
     * Do not change this method
     */
    public void writeResults() {
        broker.writeResults(this.responses);
    }

    public void calcFragility() {
        System.out.println("made it here");

        // getting all exposures
        HashMap<String, HashMap<String, Double>> exposures = broker.getExposures();

        // data structure for stored fragility calculations
        responses = new HashMap<>();

        /*
        ******  Calculate fragility here
         */
        for(JsonNode n : assets){
            String id = n.get("lineId").asText();
            Double dv = exposures.get("windField_example.asc").get("0");
            calcFailures();
            responses.put(id, dv);
        }
    }

    private static void calcFailures(){}

}
