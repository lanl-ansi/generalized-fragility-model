package gov.lanl.nisac.fragility.core;

import gov.lanl.nisac.fragility.responseEstimators.PowerPoleWindIceStress;
import gov.lanl.nisac.fragility.responseEstimators.PowerPoleWindStress;

public class ResponseEstimatorFactory {

    public ResponseEstimator runResponseEstimator(String estimatorId, GFMEngine broker, String fileOutputPath) {

        if (estimatorId.equalsIgnoreCase("flood")) {
            return null;

        } else if (estimatorId.equalsIgnoreCase("wind")) {
            return new PowerPoleWindStress(broker, fileOutputPath);

        } else if (estimatorId.equalsIgnoreCase("windIce")) {
            return new PowerPoleWindIceStress(broker, fileOutputPath);

        } else {
            System.out.println("Didn't recognize response estimator option -e: \"" + estimatorId + "\"");
            System.exit(0);
        }

        return null;
    }
}
