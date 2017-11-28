package gov.lanl.micot.application.utilities.json;

import gov.lanl.micot.application.fragility.core.GeometryObject;
import gov.lanl.micot.application.utilities.AssetData;
import gov.lanl.micot.application.utilities.asset.PropertyData;
import gov.lanl.micot.application.utilities.json.javax.JacksonJSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Trevor Crawford
 */
public class JacksonJsonData implements AssetData {

    private JacksonJSONArray jsonArray = new JacksonJSONArray();

    public JacksonJsonData(){
    }

    public void setAllValues(String fileLocation){
        jsonArray.readJsonFile(fileLocation);
    }

    public ArrayList<GeometryObject> getGeometryObjects(){
        return jsonArray.getGeometryData();
    }

    public List<Map<String, PropertyData>> getPropertyObjects(){
        return jsonArray.getPropertyObjects();
    }

}
