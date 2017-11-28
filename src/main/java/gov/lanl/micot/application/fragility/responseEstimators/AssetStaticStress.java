package gov.lanl.micot.application.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.fragility.core.ResponseEstimator;
import gov.lanl.micot.application.utilities.asset.PropertyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class estimates stress based on a static number that is provided
 * @author Russell Bent
 *
 */
public class AssetStaticStress implements ResponseEstimator {

    private GFMEngine gfmBroker;
    private HashMap<String, Double> responses;
    private List<Map<String, PropertyData>> assets;
    private String fileOutputPath;

    private static final double DEFAULT_RESPONSE = 0.5;
    
    /**
     * Response Estimator based on a static number that is provided
     * @param broker
     * @param fileOutput
     */
    public AssetStaticStress(GFMEngine broker, String fileOutput) {
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
     * General method place for static fragility calculations
     */
    public void calcFragility() {
        System.out.println("Calculating . . . ");

        // data structure to store fragility calculations/responses
        responses = new HashMap<>();

        /*
         ********  Calculate static fragility here ********
         */
        for (Map<String, PropertyData> n : assets) {

            String id = n.get("id").asString();
            double failure = DEFAULT_RESPONSE; //n.has("staticFailure") ? n.get("staticFailure").asDouble() : DEFAULT_RESPONSE;
            
            // store responses
            responses.put(id, failure);
        }
    }

	@Override
	public double getResponse(String id) {
		return responses.get(id);
	}
}