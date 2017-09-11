package gov.lanl.nisac.fragility.core;

import gov.lanl.nisac.fragility.responseEstimators.PowerPoleWindStress;

public class ResponseEstimatorFactory {

    public ResponseEstimator runResponseEstimator(String name, GFMEngine broker ){

        if (name == null) {
            System.out.println("null estimator");
            System.exit(0);
            return null;
        }

        if (name.equalsIgnoreCase("ThresholdFloodResponseEstimator")) {
            return null;
        } else if (name.equalsIgnoreCase("PowerPoleWindStressEstimator")) {
            return new PowerPoleWindStress(broker);
        }else{
            System.out.println("Didn't recognize response estimator - - quiting ...");
            System.exit(0);
        }

        return null;
    }
}
