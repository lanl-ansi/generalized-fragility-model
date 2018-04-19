package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.utilities.asset.PropertyData;

import javax.swing.*;
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
        Double depth_value = Double.valueOf(JOptionPane.showInputDialog("Threshold depth value"));


        // getting all exposures
        Map<String, HashMap<String, ArrayList<Double>>> exposures = gfmBroker.getExposures();

        // data structure for stored fragility calculations
        responses = new HashMap<>();

        double failure=0.0;

        /*
         ********  Calculate fragility here ********
         */

        for (Map<String, PropertyData> asset : assets) {
            failure=0.0;

            String id = asset.get("id").asString();
            Double dw = exposures.get("flood").get(id).get(0);

            if (depth_value > dw){
                failure=1.0;
            }

            responses.put(id, failure);
        }
    }
}
