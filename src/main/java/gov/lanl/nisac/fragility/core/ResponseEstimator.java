package gov.lanl.nisac.fragility.core;

/**
 * Response estimator interface
 */
public interface ResponseEstimator {

    void calcFragility();

    void writeResults();

}
