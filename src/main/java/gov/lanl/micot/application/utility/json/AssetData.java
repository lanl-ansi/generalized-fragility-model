package gov.lanl.micot.application.utility.json;

import gov.lanl.micot.application.utility.json.javax.JacksonJSON;
import gov.lanl.micot.application.utility.json.javax.JacksonJSONArray;

import java.util.HashMap;
import java.util.List;

/**
 * @author Trevor Crawford
 */
public class AssetData implements JacksonJSON {

    private JacksonJSONArray jsonArray = new JacksonJSONArray();

    public AssetData(){
    }

    public List<HashMap> getAssetProperties(String fileLocation){
        return jsonArray.readJsonFile(fileLocation);
    }
}
