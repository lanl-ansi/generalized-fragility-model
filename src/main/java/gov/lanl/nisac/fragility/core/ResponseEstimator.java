package gov.lanl.nisac.fragility.core;

/**
 * Response estimator interface
 */
public interface ResponseEstimator {

    public void calcFragility();

    public void writeResults();

    /**
     * The the fragility response of an asset
     * @param id
     * @return
     */
    public double getResponse(String id);
}
