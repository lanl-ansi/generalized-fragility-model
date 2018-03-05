package gov.lanl.micot.application.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.fragility.core.ResponseEstimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a template class to build a fragility routine
 *
 * @author Trevor Crawford
 */
public class ResponseEstimatorTemplate extends ResponseEstimator {


    /**
     * Response Estimator for new fragility routines
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
     * General method place for fragility calculations
     */
    public void calcFragility() {
        System.out.println("Calculating . . . ");

        // getting all exposures
        Map<String, HashMap<String, ArrayList<Double>>> exposures = gfmBroker.getExposures();

        // data structure to store fragility calculations/responses
        responses = new HashMap<>();

        double failure=0.0;

        /*
         ********  Calculate fragility here ********
         */

        for (Map<String, PropertyData> asset : assets) {

            // grab asset id value
            String id = asset.get("id").asString();

            // grab exposure values
            // EXAMPLE:
            // Double dw = exposures.get("hazardFieldName").get(id).get(0);

            // failure probability - example based on using internal class FragilityExample
            //
            failure = new FragilityExample().getFailureProbability();
            responses.put(id, failure);

        }

    }


}

/**
 *  Basic class outline to a single fragilitliy calculation.  This only a simple example class.
 */
class FragilityExample{

    FragilityExample(){

        getFailureProbability()
    }

    double getFailureProbability(){
        return 0.0;
    }

}