package gov.lanl.nisac.fragility;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMDataReader;

import java.util.ArrayList;


public class mainApp {

    public static void main(String[] args) {

        String abspath = "..\\new_sd_poles.json";
        GFMDataReader.readGeoJsonFile(abspath);
        ArrayList<GeometryObject> dataAssets = GFMDataReader.getGeometryObjects();
        ArrayList<JsonNode> props = GFMDataReader.getProperties();

        String hazabspath = "..\\san_diego_hurr_ciclops_input_extrap_interp_hfhr_2.asc";
        ArrayList<HazardField> hazObj = GFMDataReader.readHazardFile(hazabspath);

        GFMEngine broker = new GFMEngine();
        broker.setHazardfields(hazObj);

        broker.setGeometryObjects(dataAssets);
        broker.setAssetProperties(props);
        broker.produceExposures();

        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.runResponseEstimator("PowerPoleWindStressEstimator", broker);
        r1.writeResults();

    }

}
