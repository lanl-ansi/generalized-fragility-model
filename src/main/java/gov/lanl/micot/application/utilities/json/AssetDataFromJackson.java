package gov.lanl.micot.application.utilities.json;

import gov.lanl.micot.application.fragility.core.GeometryObject;
import gov.lanl.micot.application.utilities.asset.PropertyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Trevor Crawford
 */
public interface AssetDataFromJackson {

    public void readGeoJsonFile(String fileLocation);
    public ArrayList<GeometryObject> getGeometryObjects();
    public List<Map<String, PropertyData>> getPropertyObjects();
    public void writeJSON(HashMap<String, Double> responses, String fileOutputPath);
    public void readWriteRDTJSON(String filePath, String polesOutputPath);
    public void writeRDTScenarios(Map<Integer, List<String>> lineScenarios, String scenarioFilePath);

}
