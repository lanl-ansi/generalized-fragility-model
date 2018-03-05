package gov.lanl.micot.application.utilities.json;

import gov.lanl.micot.application.fragility.core.GeometryObject;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.json.javax.JacksonJSONOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Trevor Crawford
 */
public class JsonDataFromJackson implements AssetDataFromJackson {

    private JacksonJSONOperations jsonOps = new JacksonJSONOperations();

    public JsonDataFromJackson(){
    }

    @Override
    public void readGeoJsonFile(String fileLocation){
        jsonOps.readJsonFile(fileLocation);
    }

    @Override
    public ArrayList<GeometryObject> getGeometryObjects(){
        return jsonOps.getGeometryData();
    }

    @Override
    public List<Map<String, PropertyData>> getPropertyObjects(){
        return jsonOps.getPropertyObjects();
    }

    @Override
    public void writeJSON(HashMap<String, Double> responses, String fileOutputPath){
        jsonOps.writeJSON(responses, fileOutputPath);
    }

    @Override
    public void readWriteRDTJSON(String filePath, String polesOutputPath) {
        jsonOps.readWriteRDTJSON(filePath, polesOutputPath);
    }

    @Override
    public void writeRDTScenarios(Map<Integer, List<String>> lineScenarios, String scenarioFilePath) {
        jsonOps.writeRDTScenarios(lineScenarios, scenarioFilePath);
    }

}
