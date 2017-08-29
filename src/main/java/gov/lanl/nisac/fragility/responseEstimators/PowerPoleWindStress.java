package gov.lanl.nisac.fragility.responseEstimators;

import com.google.common.collect.Multimap;
import gov.lanl.nisac.fragility.core.GFMEngine;

public class PowerPoleWindStress {

    private GFMEngine broker;
    private Multimap<String, String> responses;

    public PowerPoleWindStress(GFMEngine broker){
        this.broker = broker;
        setResponses();
        calcFragility();
    }

    public String getExposure(String id) {
        return broker.getExposures(id);
    }

    private void calcFragility(){
        System.out.println("made it here");
        System.out.println(responses.get("8"));
    }


    private void setResponses() {
        this.responses = broker.getExposures();
    }
}
