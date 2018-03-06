package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.fragility.responseEstimators.AssetStaticStress;
import gov.lanl.micot.application.fragility.responseEstimators.PowerPoleWindIceStress;
import gov.lanl.micot.application.fragility.responseEstimators.PowerPoleWindStress;
import gov.lanl.micot.application.fragility.responseEstimators.ResponseEstimator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class implements the response estimator factory.
 */
public class ResponseEstimatorFactory {

    private String responseEstimatorString = "gov.lanl.micot.application.fragility.responseEstimators.";

    /**
     * This constructor instantiates the appropriate response estimator object
     *
     * @param estimatorId    estimator identification
     * @param broker         mediator reference to exposure data structures for fragility computations
     * @param fileOutputPath file output path
     * @return a ResponseEstimator Object specific to the estimator identifier.
     */
    public ResponseEstimator runResponseEstimator(String estimatorId, GFMEngine broker, String fileOutputPath) {

        responseEstimatorString = responseEstimatorString + estimatorId;

        Constructor constructor;
        ResponseEstimator responseEstimator = null;


        try {
            // look for response estimator
            Class<?> responseEstimatorClass = Class.forName(responseEstimatorString);

            // get class constructor
            constructor = responseEstimatorClass.getConstructor(GFMEngine.class, String.class);
            System.out.println("Implementing response estimator: " + responseEstimatorString);

            responseEstimator = (ResponseEstimator) constructor.newInstance(broker, fileOutputPath);

        } catch (ClassNotFoundException e) {
            System.err.println("-- Response Estimator >> " + estimatorId + " << does not exist");
            e.printStackTrace();

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            System.err.println("-- Could not find constructor for Response Estimator: " +
                    estimatorId + ". ");
            e.printStackTrace();
        }

        // return Appropriate response estimator
        return responseEstimator;
    }

}
