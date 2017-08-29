package gov.lanl.nisac.fragility;

import gov.lanl.nisac.fragility.core.*;
import gov.lanl.nisac.fragility.io.GFMFileReader;
import gov.lanl.nisac.fragility.responseEstimators.PowerPoleWindStress;

import java.util.ArrayList;


public class mainApp {

    public static void main(String[] args) {

        System.out.println("It has begun...");
        String abspath = "RDT-to-Poles.json";

        GFMEngine broker = new GFMEngine();

        ArrayList<GeometryObject> dataAssets = GFMFileReader.readGeoJsonFile(abspath, broker);
        String hazabspath = "windField_example.asc";
        ArrayList<HazardField> hazObj = GFMFileReader.readHazardFile(hazabspath, broker);
        System.out.println(hazObj.get(0).getFileLocation());

        broker.setHazardfields(hazObj);
        broker.setGeometryObjects(dataAssets);
        broker.produceExposures();

        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();

        PowerPoleWindStress pp = new PowerPoleWindStress(broker);

//        System.out.println(responses.getExposure("1462")); //58.4585
//        System.out.println(responses.getExposure("8"));    //36.8623
//        System.out.println(responses.getExposure("912"));  //37.4214



    }

}
