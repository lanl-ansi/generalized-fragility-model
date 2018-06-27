package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.utilities.asset.PropertyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThresholdFlood extends ResponseEstimator {


    public ThresholdFlood(GFMEngine broker, String fileOutput) {
        gfmBroker = broker;
        assets = broker.getAssetProperties();
        fileOutputPath = fileOutput;

        // calculate fragility in this method
        calcFragility();

    }

    public void calcFragility() {

        System.out.println("Calculating . . . ");

        // getting all exposures
        Map<String, HashMap<String, ArrayList<Double>>> exposures = gfmBroker.getExposures();

        // data structure for stored fragility calculations
        responses = new HashMap<>();

        double failure;

        /*
         ********  Calculate fragility here ********
         */

        for (Map<String, PropertyData> asset : assets) {
            failure=0.0;


            String id = asset.get("id").asString();
            double fv = asset.get("floodThresholdValue").asDouble();

            Double dw = exposures.get("flood").get(id).get(0);

            if (fv < dw){
                failure=1.0;
            }else{
                failure=0.0;
            }

            responses.put(id, failure);
        }
    }
}
