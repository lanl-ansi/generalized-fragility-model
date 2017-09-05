package gov.lanl.nisac.fragility.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class ResponseApproximation {

    ObjectMapper mapper = new ObjectMapper();
    ArrayNode array = mapper.createArrayNode();

    public void buildingJson(){

        ObjectNode singleNode = mapper.createObjectNode().put("id", "1")
                .put("type", "probability")
                .put("hazard", "1")
                .put("value", 0.0062123496);
        array.add(singleNode);

        singleNode = mapper.createObjectNode().put("id", "2")
                .put("type", "probability")
                .put("hazard", "1")
                .put("value", 0.232349611);
        array.add(singleNode);

        System.out.println("-->> "+array);
    }
}
