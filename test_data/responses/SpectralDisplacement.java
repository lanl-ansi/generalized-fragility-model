package gov.lanl.nisac.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.GFMEngine;
import gov.lanl.nisac.fragility.core.ResponseEstimator;

import java.util.ArrayList;
import java.util.HashMap;

public class SpectralDisplacement implements ResponseEstimator {

    private GFMEngine gfmBroker;
    private HashMap<String, Double> responses;
    private ArrayList<JsonNode> assets;
    private String fileOutputPath;

    /**
     * Response Estimator for power pole fragility with wind and ice stresses
     *
     * Do not change this method.
     *
     * @param broker
     * @param fileOutput
     */
    public ResponseEstimatorTemplate(GFMEngine broker, String fileOutput) {
        gfmBroker = broker;
        assets = broker.getAssetProperties();
        fileOutputPath = fileOutput;

        // calculate fragility in this method
        calcFragility();
    }

    /**
     * Do not change this method.
     */
    public void writeResults() {
        gfmBroker.storeResults(this.responses, fileOutputPath);
    }

    /**
     * General method place for fragility calculations
     */
    public void calcFragility() {
        System.out.println("Calculating . . . ");

        // getting all exposures
        HashMap<String, HashMap<String, Double>> exposures = gfmBroker.getExposures();

        // data structure to store fragility calculations/responses
        responses = new HashMap<>();

        double failure=0.0;

        /*
         ********  Calculate fragility here ********
         */
        for (JsonNode n : assets) {

            String id = n.get("id").asText();
            Double dv = exposures.get("wind").get(id);

            // store responses
            responses.put(id, failure);
        }
    }
}