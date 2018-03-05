package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.fragility.core.ResponseEstimator;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;

public class ResponseEstimatorSpectralDisplacement extends ResponseEstimator {

    /**
     * Response Estimator example for Spectral Displacement fragility applied to buildings.
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
     * General method place for fragility calculations
     */
    public void calcFragility() {
        System.out.println("Calculating . . . ");

        // getting all exposures
        Map<String, HashMap<String, ArrayList<Double>>> exposures = gfmBroker.getExposures();

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
            Double exposureValue = exposures.get("eqd").get(id).get(0);

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