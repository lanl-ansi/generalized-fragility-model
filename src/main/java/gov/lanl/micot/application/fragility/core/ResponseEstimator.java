package gov.lanl.micot.application.fragility.core;

/**
 * Response estimator interface
 */
public interface ResponseEstimator {

    /**
     * Generic method to run fragility calculations.
     */
    void calcFragility();

    /**
     * Generic method to write JSON results.
     */
    void writeResults();

    /**
     * The fragility response of an asset referenced by unique identifier.
     * @param id unique identifier
     * @return fragility value between zero and one.
     */
    double getResponse(String id);
}
