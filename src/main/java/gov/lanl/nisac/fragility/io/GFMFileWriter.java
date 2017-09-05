package gov.lanl.nisac.fragility.io;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class GFMFileWriter {

    private GFMFileWriter() {
    }

    public static void writeSomething(ArrayNode exposures, String filePath){

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File(filePath), exposures);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
