package gov.lanl.nisac.fragility.responseEstimators;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.GFMEngine;
import gov.lanl.nisac.fragility.core.ResponseEstimator;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.HashMap;

public class PowerPoleWindStress implements ResponseEstimator {

    private GFMEngine broker;
    private HashMap<String, Double> responses;
    private ArrayList<JsonNode> assets;

    public PowerPoleWindStress(GFMEngine broker) {
        this.broker = broker;
        this.assets = broker.getAssetProperties();

        // calculate fragility in this method
        calcFragility();

        // send back to GFM Engine to write results
        writeResults();
    }

    /**
     * Do not change this method
     */
    public void writeResults() {
        broker.writeResults(this.responses);
    }

    public void calcFragility() {
        System.out.println("made it here");

        // getting all exposures
        HashMap<String, HashMap<String, Double>> exposures = broker.getExposures();

        // data structure for stored fragility calculations
        responses = new HashMap<>();

        /*
        ******  Calculate fragility here ********
         */
        for (JsonNode n : assets) {
            String id = n.get("lineId").asText();
            Double dv = exposures.get("windField_example.asc").get("0");
            calcFailures(n, dv);
            responses.put(id, dv);
        }
    }

    private static void calcFailures(JsonNode n, Double dv) {

        double baseDiameter = n.get("baseDiameter").asDouble();
        System.out.println(baseDiameter);
    }

}

final class Property {

    double baseDiameter;
    double cableSpan;
    double commAttachmentHeight;
    double commCableDiameter;
    double commCableNumber;
    double commCableWireDensity;
    double height;
    double meanPoleStrength;
    double powerAttachmentHeight;
    double powerCableDiameter;
    double powerCableNumber;
    double powerCableWireDensity;
    double stdDevPoleStrength;
    double topDiameter;
    double woodDensity;

    private static final double PI_O_4 = Math.PI / 4.0;
    private static final double _32_0 = 32.0;
    private NormalDistribution nd;


    public Property(JsonNode n) {

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

    }

    private void calc() {

        double baseArea = PI_O_4 * SQ(baseDiameter);
        double z_bott = Math.PI * Math.pow(baseDiameter, 3) / _32_0;
        double mass = PI_O_4 * (SQ(baseDiameter) + SQ(topDiameter)) / 2 * height * woodDensity;

        nd = new NormalDistribution(meanPoleStrength, stdDevPoleStrength);
//        nd.cumulativeProbability(stress);

    }

    private static double SQ(double d) {
        return d * d;
    }

}
