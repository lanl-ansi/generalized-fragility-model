package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.fragility.responseEstimators.AssetStaticStress;
import gov.lanl.micot.application.fragility.responseEstimators.PowerPoleWindIceStress;
import gov.lanl.micot.application.fragility.responseEstimators.PowerPoleWindStress;
import gov.lanl.micot.application.fragility.responseEstimators.ResponseEstimator;

/**
 * This class implements the response estimator factory.
 */
public class ResponseEstimatorFactory {

    /**
     * This constructor instantiates the appropriate response estimator object
     *
     * @param estimatorId estimator identification
     * @param broker mediator reference to exposure data structures for fragility computations
     * @param fileOutputPath file output path
     * @return a ResponseEstimator Object specific to the estimator identifier.
     */
    public ResponseEstimator runResponseEstimator(String estimatorId, GFMEngine broker, String fileOutputPath) {

        if (estimatorId.equalsIgnoreCase("flood")) {
            return null;

        } else if (estimatorId.equalsIgnoreCase("wind")) {
            return new PowerPoleWindStress(broker, fileOutputPath);

        } else if (estimatorId.equalsIgnoreCase("windIce")) {
            return new PowerPoleWindIceStress(broker, fileOutputPath);

        } else if (estimatorId.equalsIgnoreCase("static")) {
            return new AssetStaticStress(broker, fileOutputPath);

        } else {
            System.out.println("Didn't recognize response estimator option -e: \"" + estimatorId + "\"");
            return null;
        }
    }
}
