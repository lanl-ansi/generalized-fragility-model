package gov.lanl.nisac.fragility.GFMcore;

public interface ResponseEstimator {

    String getExposure(String id);
    GFMEngine getBroker();

}
