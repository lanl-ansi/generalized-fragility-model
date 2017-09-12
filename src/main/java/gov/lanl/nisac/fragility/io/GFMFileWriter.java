package gov.lanl.nisac.fragility.io;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;

public class GFMFileWriter {

    private static String fileOutputPath = "fragility_output.json";

    private GFMFileWriter() {
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
}
