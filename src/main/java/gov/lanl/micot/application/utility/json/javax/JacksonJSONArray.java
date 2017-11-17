package gov.lanl.micot.application.utility.json.javax;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JacksonJSONArray implements JacksonJSON {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode fileNodes = null;
    private ArrayList<JsonNode> properties = new ArrayList<>();

    public List<HashMap> readJsonFile(String FileLocation) {

        InputStream inStream;
        HashMap<String, String> assetData = null;
        List<HashMap> dataList = new ArrayList<>();

        try {
            inStream = new FileInputStream(FileLocation);
            this.fileNodes = this.objectMapper.readTree(inStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (JsonNode n : this.fileNodes.findValue("features")) {
            JsonNode props = n.get("properties");

            Iterator<String> fieldNames = props.fieldNames();

            while (fieldNames.hasNext()) {
                String field = fieldNames.next();
                assetData = new HashMap<>();
                assetData.put(field, props.get(field).asText());
                dataList.add(assetData);
            }

        }

        return dataList;
    }
}
