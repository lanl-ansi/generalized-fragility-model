package gov.lanl.micot.application.fragility.core;


import java.util.List;

/**
 * Hazard field interface methods
 */
public interface HazardField {

    /**
     * @return unique identifier for a Hazard field
     */
    String getIdentifier();

    /**
     * @param crd
     * @return
     */
    double evaluatePoint(List<double[]> crd);

    /**
     * @param crd
     * @return List of double values
     */
    List<Double> evaluatePoints(List<double[]> crd);

    /**
     * @return
     */
    int getOutsideExtentCount();


}
