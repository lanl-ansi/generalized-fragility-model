package gov.lanl.nisac.fragility.responseEstimators;


import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.GFMEngine;
import gov.lanl.nisac.fragility.core.ResponseEstimator;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;

public class PowerPoleWindIceStress implements ResponseEstimator {

    private GFMEngine gfmBroker;
    private HashMap<String, Double> responses;
    private ArrayList<JsonNode> assets;
    private String fileOutputPath;


    /**
     * Response Estimator for power pole fragility with wind and ice stresses
     *
     * @param broker
     * @param fileOutput
     */
    public PowerPoleWindIceStress(GFMEngine broker, String fileOutput) {
        gfmBroker = broker;
        assets = broker.getAssetProperties();
        fileOutputPath = fileOutput;

        // calculate fragility in this method
        calcFragility();
    }

    /**
     * Do not change this method
     */
    public void writeResults() {
        gfmBroker.storeResults(responses, fileOutputPath);
    }

    /**
     * General method place for fragility calculations
     */
    public void calcFragility() {
        System.out.println("calculating . . . ");

        // getting all exposures
        HashMap<String, HashMap<String, Double>> exposures = gfmBroker.getExposures();

        // data structure for stored fragility calculations
        responses = new HashMap<>();

        double failure;

        /*
         ********  Calculate fragility here ********
         */
        for (JsonNode n : assets) {

            String id = n.get("id").asText();
            Double dw = exposures.get("wind").get(id);
            Double di = exposures.get("ice").get(id);

            failure = new FragilityWindIce(n, dw, di).getFailureProbability();
            responses.put(id, failure);
        }
    }
}

/**
 *  General class to calculate physical stresses due to wind and ice
 */
class FragilityWindIce {

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
    private double iceExposure;

    private double failureProbability;

    private static final double ICE_DENSITY = 900.0; // (kg / m^3)
    private static final double AIR_DENSITY = 1.225; // (kg / m^3)
    private static final double GRAVITY = 9.80665;   // (m / s^2)
    private static final double PI = Math.PI;
    private static final double TO_METERS_PER_SECOND = 0.514444; // conversion from knots
    private NormalDistribution nd = null;


    FragilityWindIce(JsonNode n, double wind, double ice) {
        windExposure = wind;
        iceExposure = ice;
        baseDiameter = n.get("baseDiameter").asDouble();
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

    public void calculate() {

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


