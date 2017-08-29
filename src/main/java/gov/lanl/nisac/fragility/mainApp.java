package gov.lanl.nisac.fragility;

import gov.lanl.nisac.fragility.GFMcore.*;
import gov.lanl.nisac.fragility.io.GFMFileReader;

import java.util.ArrayList;


public class mainApp {

    public static void main(String[] args) {
        System.out.println("It has begun...");
        String abspath = "RDT-to-Poles.json";

        GFMEngine broker = new GFMEngine();

        ArrayList<GeometryObject> dataAssets = GFMFileReader.readGeoJsonFile(abspath, broker);
        System.out.println(dataAssets.get(0).getIdentifier());
        System.out.println(dataAssets.size());

        String hazabspath = "windField_example.asc";
//        String hazabspath = "C:\\Users\\301338\\Desktop\\PROJECTS\\code_development\\micot-general-fragility\\windField_example.asc";
        ArrayList<HazardField> hazObj = GFMFileReader.readHazardFile(hazabspath, broker);
        System.out.println(hazObj.get(0).getFileLocation());

        broker.setHazardfields(hazObj);
        broker.setGeometryObjects(dataAssets);

        ResponseEstimatorFactory ref = new ResponseEstimatorFactory();
        ResponseEstimator responses = ref.getResponseEstimator("PowerPoleWindStressEstimator",broker);
        responses.getBroker().produceExposures();

        System.out.println(responses.getExposure("1462")); //58.4585
        System.out.println(responses.getExposure("8"));    //36.8623
        System.out.println(responses.getExposure("912"));  //37.4214
    }

}
