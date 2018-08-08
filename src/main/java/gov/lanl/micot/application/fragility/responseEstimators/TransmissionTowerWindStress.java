package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.lang.Math;

public class TransmissionTowerWindStress extends ResponseEstimator {

    /**
     * Response Estimator for power pole fragility with wind and ice stresses
     *
     * @param broker
     * @param fileOutput
     */
    public TransmissionTowerWindStress(GFMEngine broker, String fileOutput) {

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

        // data structure for stored fragility calculations
        responses = new HashMap<>();

        double failure;

        /*
         ********  Calculate fragility here ********
         */

        for (Map<String, PropertyData> asset : assets) {

            String id = asset.get("id").asString();
            Double dv = exposures.get("wind").get(id).get(0);

            failure = new TowerFragilityWind(asset, dv).getFailureProbability();
            responses.put(id, failure);

        }
    }

}


/**
 *  General class to calculate physical stresses due to wind
 */
class TowerFragilityWind {
    private double windExposure;
    private double failureProbability;

    private static final double TO_METERS_PER_SECOND = 0.514444; // conversion from knots
    private NormalDistribution nd = null;

    /**
     * Fragility computations for wind induced probability of pole failure.
     * @param n JsonNode that contains needed properties
     * @param exposure wind exposure value
     */
    TowerFragilityWind(Map<String, PropertyData> n, double exposure) {
        windExposure = exposure;
        calculate();
    }

    private void calculate() {
        double V = (windExposure * TO_METERS_PER_SECOND);

        if (V <= 1e-6) {
            failureProbability = 0.0;
        } else {
            double Vratio = V / 45.0;
            double w = -3.33 * Math.log(Vratio * Vratio);

            nd = new NormalDistribution(0.01, 1.0);
            failureProbability = nd.cumulativeProbability(-w);
            System.out.print(V);
            System.out.print(" => ");
            System.out.print(w);
            System.out.print(" => ");
            System.out.println(failureProbability);
        }
    }

    double getFailureProbability() {
        return failureProbability;
    }
}


