package gov.lanl.nisac.fragility.GFMcore;

public class PowerPoleWindStress implements ResponseEstimator {

    private GFMEngine broker;

    public PowerPoleWindStress(GFMEngine broker){
        this.broker = broker;
    }

    public void defaultImplementation(){
    }

    @Override
    public String getExposure(String id) {
        return broker.getExposures(id);
    }

    public GFMEngine getBroker() {
        return broker;
    }
}
