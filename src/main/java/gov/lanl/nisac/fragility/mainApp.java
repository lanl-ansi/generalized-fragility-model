package gov.lanl.nisac.fragility;

import com.fasterxml.jackson.databind.JsonNode;
import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMDataReader;

import java.util.ArrayList;


public class mainApp {

    public static void main(String[] args) {

        System.out.println("It has begun...");
        String abspath = "RDT-to-Poles.json";
        GFMDataReader.readGeoJsonFile(abspath);
        ArrayList<GeometryObject> dataAssets = GFMDataReader.getGeometryObjects();
        ArrayList<JsonNode> props = GFMDataReader.getProperties();

        String hazabspath = "windField_example.asc";
        ArrayList<HazardField> hazObj = GFMDataReader.readHazardFile(hazabspath);

        GFMEngine broker = new GFMEngine();
        broker.setHazardfields(hazObj);
        broker.setGeometryObjects(dataAssets);
        broker.setAssetProperties(props);
        broker.produceExposures();

        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator r1 = ref.getResponseEstimator("PowerPoleWindStressEstimator", broker);

//        PowerPoleWindStress pp = new PowerPoleWindStress(broker);
//        System.out.println(responses.getExposure("1462")); //58.4585
//        System.out.println(responses.getExposure("8"));    //36.8623
//        System.out.println(responses.getExposure("912"));  //37.4214

    }

}
