package gov.lanl.nisac.fragility.core;

import gov.lanl.nisac.fragility.responseEstimators.PowerPoleWindStress;

public class ResponseEstimatorFactory {

    public ResponseEstimator getResponseEstimator(String name, GFMEngine broker ){

        if (name == null) {
            return null;
        }

        if (name.equalsIgnoreCase("ThresholdFloodResponseEstimator")) {
            return null;
        } else if (name.equalsIgnoreCase("PowerPoleWindStressEstimator")) {
            return new PowerPoleWindStress(broker);
        }

        return null;
    }
}
