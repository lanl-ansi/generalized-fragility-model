package gov.lanl.nisac.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.GFMEngine;
import gov.lanl.nisac.fragility.core.ResponseEstimator;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;

public class ResponseEstimatorSpectralDisplacement implements ResponseEstimator {

    private GFMEngine gfmBroker;
    private HashMap<String, Double> responses;
    private ArrayList<JsonNode> assets;
    private String fileOutputPath;

    /**
     * Response Estimator for power pole fragility with wind and ice stresses
     * <p>
     * Do not change this method.
     *
     * @param broker
     * @param fileOutput
     */
    public ResponseEstimatorSpectralDisplacement(GFMEngine broker, String fileOutput) {
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

        double failure = 0.0;
        NormalDistribution nd = null;

        /*
         ********  Calculate fragility here ********
         */
        for (JsonNode n : assets) {

            // getting asset identifier
            String id = n.get("id").asText();
            // getting median spectral value
            Double msd = n.get("MSD").asDouble();
            // getting standard deviation of spectral displacement
            Double stdDev = n.get("LogNormStdDev").asDouble();
            // gettting spectral displacement exposure
            Double exposureValue = exposures.get("eqd").get(id);

            // conditional probability of being in, or exceeding, a particular damage state,
            // given the spectral displacement
            Double dv = (1.0/stdDev)*Math.log(exposureValue/msd);
            nd = new NormalDistribution();
            failure = nd.cumulativeProbability(dv);

            // store responses
            responses.put(id, failure);
        }
    }
}