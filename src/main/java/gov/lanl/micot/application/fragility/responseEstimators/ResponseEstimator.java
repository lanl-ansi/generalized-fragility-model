package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.utilities.asset.PropertyData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Response estimator interface
 */
public abstract class ResponseEstimator {

    GFMEngine gfmBroker;
    HashMap<String, Double> responses;
    List<Map<String, PropertyData>> assets;
    String fileOutputPath;

    /**
     * Generic method to run fragility calculations.
     */
    public void calcFragility() {
    }


    /**
     * Generic method to write JSON results.
     */
    public void writeResults() {
        gfmBroker.writeJSONOutputs(this.responses, fileOutputPath);
    }

    /**
     * The fragility response of an asset referenced by unique identifier.
     *
     * @param id unique identifier
     * @return fragility value between zero and one.
     */
    public double getResponse(String id) {
        return responses.get(id);
    }
}
