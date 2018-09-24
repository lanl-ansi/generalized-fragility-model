package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransmissionTowerWindStress extends ResponseEstimator {

    /**
     * Response Estimator for Transmission tower fragility with winds
     *
     * @param broker
     * @param fileOutput
     * @author Art Barnes
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
        System.out.println("Calculating Transmission Tower Fragility Routine. . . ");

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

    /**
     * Override default JSON output
     */
    public void writeResults() {
        writeCSVOutputs(this.responses, fileOutputPath);

    }

    public void writeCSVOutputs(HashMap<String, Double> responses, String fileOutputPath) {

        if (fileOutputPath == null){
            fileOutputPath = "fragility_output.csv";
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileOutputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.write("AssetID,DamageProbability\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String,Double> entry : responses.entrySet()) {
            try {
                writer.write(entry.getKey());
                writer.write(",");
                writer.write(entry.getValue().toString());
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


/**
 * General class to calculate physical stresses due to wind
 */
class TowerFragilityWind {
    private double windExposure;
    private double failureProbability;

    private static final double TO_METERS_PER_SECOND = 0.514444; // conversion from knots
    private NormalDistribution nd = null;

    /**
     * Fragility computations for wind induced probability of pole failure.
     *
     * @param n        JsonNode that contains needed properties
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
        }
    }

    double getFailureProbability() {
        return failureProbability;
    }
}


