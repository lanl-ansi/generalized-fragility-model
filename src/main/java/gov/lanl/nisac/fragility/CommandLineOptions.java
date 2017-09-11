package gov.lanl.nisac.fragility;

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommandLineOptions {

    private static Options options = defineOptions();

    private boolean hasAssets;
    private String assetInputPath;
    private String[] hazardInputPaths;

    public CommandLineOptions(String[] args){

        try {
            parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    private static Options defineOptions() {
        options = new Options();

        options.addOption(Option.builder("h")
                .desc("help")
                .longOpt("assets")
                .build()
        );

        options.addOption(Option.builder("a")
                .desc("assets GeoJSON input")
                .hasArg()
                .longOpt("assets")
                .build()
        );

        options.addOption(Option.builder("hf")
                .hasArgs()
                .valueSeparator(';')
                .desc("hazard input file(s)")
                .longOpt("hazardField")
                .build()
        );

        return options;
    }

    private void parse(String[] args) throws ParseException {
        CommandLine commandLine;
        CommandLineParser parser = new DefaultParser();

        // Parse the options and return the command line object.
        commandLine = parser.parse(options, args);

        // Check to see if only help is requested.
        if (commandLine.hasOption("help")) {
            System.out.println("Fragility help:");
            printUsage();
            System.exit(0);
        }

        if (commandLine.hasOption("assets")) {
            hasAssets = true;
            assetInputPath = commandLine.getOptionValue("assets");
        }

        if (commandLine.hasOption("hazardField")) {
            hasAssets = true;
            hazardInputPaths = commandLine.getOptionValues("hazardField");
        }

    }

    public static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter swriter = new StringWriter();
        PrintWriter writer = new PrintWriter(swriter);
        String header = "fragility  [OPTIONS]\n options:\n";
        String footer =
                "\nExamples:\n\nFragility.jar  -r <RDT_path.json> -o <output.json>\n\n" +
                        "Fragility.jar -p <Poles_input> -o <output> --schema http://org.lanl.fragility/schemas/fragilitySchema.json \n\n" +
                        "Fragility.jar -r <RDT_path> -wf <windHazard_path> -o <RDT_output> \n\n";
    }

    public boolean hasAssets() {
        return hasAssets;
    }

    public String getAssetInputPath() {
        return assetInputPath;
    }

    public String[] getHazardInputPaths() {
        return hazardInputPaths;
    }
}
