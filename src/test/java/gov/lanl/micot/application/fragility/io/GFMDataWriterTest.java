package gov.lanl.micot.application.fragility.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junit.framework.TestCase;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class GFMDataWriterTest extends TestCase {

    private static ObjectMapper mapper = new ObjectMapper();

    public void testGFMDataWriterArray() throws InterruptedException {

        // create geoJSON Feature Collection
        ArrayNode featureNodeArr = mapper.createObjectNode().putArray("good")
                .add(34.56)
                .add(67.89);

        GFMDataWriter.writeResults(featureNodeArr, "testA.json");
        File f = new File("testA.json");
        TimeUnit.MILLISECONDS.sleep(10);
        assertTrue(f.exists());
        f.delete();

    }

    public void testGFMDataWriterObject() throws InterruptedException {

        // create geoJSON Feature Collection
        ObjectNode featureNodeObj = mapper.createObjectNode()
                .put("type", "FeatureCollection")
                .putPOJO("features", null);

        GFMDataWriter.writeResults(featureNodeObj, "testO.json");
        File f = new File("testO.json");
        TimeUnit.MILLISECONDS.sleep(10);
        assertTrue(f.exists());
        f.delete();

    }

    public void testGFMDataWriterJsonNode() throws InterruptedException {

        // create geoJSON Feature Collection
        JsonNode featureNodeObj = mapper.createObjectNode()
                .put("type", "FeatureCollection")
                .putPOJO("features", null);

        GFMDataWriter.writeResults(featureNodeObj, "testJ.json");
        File f = new File("testJ.json");
        TimeUnit.MILLISECONDS.sleep(10);
        assertTrue(f.exists());
        f.delete();

    }

}