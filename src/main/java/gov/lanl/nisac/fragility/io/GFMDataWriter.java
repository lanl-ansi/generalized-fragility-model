package gov.lanl.nisac.fragility.io;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class GFMDataWriter {

    private static String fileOutputPath = "fragility_output.json";

    private GFMDataWriter() {
    }

    /**
     * General routine that writes a JSON array
     *
     * @param responsesApproximations
     * @param filePath
     */
    public static void writeResults(ArrayNode responsesApproximations, String filePath){

        if (!(filePath == null)){
            fileOutputPath = filePath;
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try {
            writer.writeValue(new File(fileOutputPath), responsesApproximations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeResults(ObjectNode objs, String filePath){

        if (!(filePath == null)){
            fileOutputPath = filePath;
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try {
            writer.writeValue(new File(fileOutputPath), objs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
