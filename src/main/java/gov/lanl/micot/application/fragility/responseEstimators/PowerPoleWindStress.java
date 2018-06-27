package gov.lanl.micot.application.fragility.responseEstimators;

import gov.lanl.micot.application.fragility.core.GFMEngine;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PowerPoleWindStress extends ResponseEstimator {

    /**
     * Response Estimator for power pole fragility with wind and ice stresses
     *
     * @param broker
     * @param fileOutput
     */
    public PowerPoleWindStress(GFMEngine broker, String fileOutput) {

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

            failure = new FragilityWind(asset, dv).getFailureProbability();
            responses.put(id, failure);

        }
    }

}


/**
 *  General class to calculate physical stresses due to wind
 */
class FragilityWind {

    private double baseDiameter;
    private double cableSpan;
    private double commAttachmentHeight;
    private double commCableDiameter;
    private double commCableNumber;
    private double commCableWireDensity;
    private double height;
    private double meanPoleStrength;
    private double powerAttachmentHeight;
    private double powerCableDiameter;
    private double powerCableNumber;
    private double powerCableWireDensity;
    private double stdDevPoleStrength;
    private double topDiameter;
    private double woodDensity;
    private double windExposure;

    private double failureProbability;

    //The density of ice is 917 kg/m^3, and the density of sea water is 1025 kg/m^3
    //At sea level and at 15 °C air has a density of approximately 1.225 kg/m^3
    private static final double AIR_DENSITY = 1.00; // (kg / m^3)
    private static final double GRAVITY = 9.81;   // (m / s^2)
    private static final double PI = Math.PI;
    private static final double TO_METERS_PER_SECOND = 0.514444; // conversion from knots
    private NormalDistribution nd = null;

    /**
     * Fragility computations for wind induced probability of pole failure.
     * @param n JsonNode that contains needed properties
     * @param exposure wind exposure value
     */
    FragilityWind(Map<String, PropertyData> n, double exposure) {

        baseDiameter = n.get("baseDiameter").asDouble();
        windExposure = exposure;
        cableSpan = n.get("cableSpan").asDouble();
        cableSpan = n.get("cableSpan").asDouble();
        commAttachmentHeight = n.get("commAttachmentHeight").asDouble();
        commCableDiameter = n.get("commCableDiameter").asDouble();
        commCableNumber = n.get("commCableNumber").asDouble();
        commCableWireDensity = n.get("commCableWireDensity").asDouble();
        height = n.get("height").asDouble();
        meanPoleStrength = n.get("meanPoleStrength").asDouble();
        powerAttachmentHeight = n.get("powerAttachmentHeight").asDouble();
        powerCableDiameter = n.get("powerCableDiameter").asDouble();
        powerCableNumber = n.get("powerCableNumber").asDouble();
        powerCableWireDensity = n.get("powerCableWireDensity").asDouble();
        stdDevPoleStrength = n.get("stdDevPoleStrength").asDouble();
        topDiameter = n.get("topDiameter").asDouble();
        woodDensity = n.get("woodDensity").asDouble();

        calculate();

    }

    private void calculate() {

        // pole weight = mass*G = G * π * D^2 * density / 4.0
        double poleDiameters = (topDiameter * topDiameter + baseDiameter * baseDiameter) / 2.0;
        double poleMass = PI / 4.0 * poleDiameters * height * woodDensity;
        double poleWeight = poleMass * GRAVITY;

        //power cable weight
        double powerCableMass = PI / 4.0 * powerCableDiameter * powerCableDiameter * cableSpan *
                powerCableNumber * powerCableWireDensity;
        double powerCableWeight = powerCableMass * GRAVITY;

        //comms cable weight
        double commsCableMass = PI / 4.0 * commCableDiameter * commCableDiameter * cableSpan *
                commCableNumber * commCableWireDensity;
        double commsCableWeight = commsCableMass * GRAVITY;

        // total dead load force ( N ) and compressive stress ( N/m^2 )
        double totalDeadLoad = poleWeight + powerCableWeight + commsCableWeight;
        double compressiveStress = totalDeadLoad / baseDiameter;

        // cable surface areas (m^2) and dynamic wind pressure (Pa)
        double commsCableArea = commCableDiameter * commCableNumber * cableSpan;
        double powerCableArea = powerCableDiameter * powerCableNumber * cableSpan;
        double windDynamicPressure = 0.5 * AIR_DENSITY * (windExposure * TO_METERS_PER_SECOND)
                * (windExposure * TO_METERS_PER_SECOND);

        // cable wind force ( N ) and moments ( N-m )
        double powerCableWindForce = powerCableArea * windDynamicPressure;
        double commsCableWindForce = commsCableArea * windDynamicPressure;
        double commsCableWindMoment = commsCableWindForce * commAttachmentHeight;
        double powerCableWindMoment = powerCableWindForce * powerAttachmentHeight;

        // tensile stress due to force of wind on cables
        double z_bottom = PI * baseDiameter * baseDiameter * baseDiameter / 32.0;
        double commsCableTensileStress = commsCableWindMoment / z_bottom;
        double powerCableTensileStress = powerCableWindMoment / z_bottom;
        double poleCablesTensileStress = commsCableTensileStress + powerCableTensileStress;

        // total tensile stress ( Pa )
        double poleTensileStress = poleCablesTensileStress - compressiveStress;

        nd = new NormalDistribution(meanPoleStrength, stdDevPoleStrength);
        failureProbability = nd.cumulativeProbability(poleTensileStress);
    }

    double getFailureProbability() {
        return failureProbability;
    }
}


