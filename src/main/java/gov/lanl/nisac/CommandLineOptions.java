package gov.lanl.nisac;

import org.apache.commons.cli.*;

import java.io.File;


/**
 * Class that defines command line options
 */
public class CommandLineOptions {

    private static Options options = defineOptions();

    private boolean hasAssets;
    private boolean hasIdentifiers;
    private boolean hasHazards;
    private boolean hasOutput;
    private boolean hasRDT;

    private String assetInputPath;
    private String[] hazardInputPaths;
    private String[] identifiers;
    private String outputFilePath;
    private String estimator;
    private String rdtInputPath;


    /**
     * Constructor
     *
     * @param args
     */
    public CommandLineOptions(String[] args) {

        try {
            parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Defines details for each option
     *
     * @return
     */
    private static Options defineOptions() {
        options = new Options();

        options.addOption(Option.builder("h")
                .desc("help text")
                .longOpt("help")
                .build()
        );

        options.addOption(Option.builder("r")
                .desc("RDT processing")
                .hasArg()
                .longOpt("rdt")
                .build()
        );

        options.addOption(Option.builder("a")
                .desc("assets GeoJSON input")
                .hasArg()
                .longOpt("assets")
                .build()
        );

        options.addOption(Option.builder("e")
                .desc("response estimator identifier")
                .hasArg()
                .longOpt("responseEstimator")
                .build()
        );

        options.addOption(Option.builder("i")
                .desc("estimator identifier - same order as hazard file names")
                .hasArgs()
                .valueSeparator(';')
                .longOpt("identifiers")
                .build()
        );

        options.addOption(Option.builder("hf")
                .hasArgs()
                .valueSeparator(';')
                .desc("hazard input file(s)")
                .longOpt("hazardFields")
                .build()
        );

        options.addOption(Option.builder("o")
                .desc("response estimator identifier")
                .hasArg()
                .longOpt("output")
                .build()
        );

        return options;
    }

    /**
     * General method that checks options
     *
     * @param args
     * @throws ParseException
     */
    private void parse(String[] args) throws ParseException {
        CommandLine commandLine;
        CommandLineParser parser = new DefaultParser();

        // Parse the options and return the command line object.
        commandLine = parser.parse(options, args);

        // Check to see if only help is requested.
        if (commandLine.hasOption("help")) {
            System.out.println("Fragility help:");
            printHelp();
            System.exit(0);
        }

        if (commandLine.hasOption("rdt")) {
            hasRDT = true;
            rdtInputPath = commandLine.getOptionValue("rdt");
            checkFile(rdtInputPath);
        }

        if (commandLine.hasOption("assets")) {
            hasAssets = true;
            assetInputPath = commandLine.getOptionValue("assets");
            checkFile(assetInputPath);
        }

        if (commandLine.hasOption("identifiers")) {
            hasIdentifiers = true;
            identifiers = commandLine.getOptionValues("identifiers");
        }

        if (commandLine.hasOption("output")) {
            hasOutput = true;
            outputFilePath = commandLine.getOptionValue("output");
        }

        if (commandLine.hasOption("responseEstimator")) {
            estimator = commandLine.getOptionValue("responseEstimator");
        }

        if (commandLine.hasOption("hazardFields")) {
            hasHazards = true;
            hazardInputPaths = commandLine.getOptionValues("hazardFields");
            checkFiles(hazardInputPaths);
        }
    }

    /**
     * helper method that
     */
    private static void printHelp() {
        String header = "fragility  [OPTIONS]\n options:\n" +
                "-a             asset data\n" +
                "-hf            hazard field files \n" +
                "-i             identifiers\n" +
                "-e             estimator identifier\n" +
                "-o (optional)  output file name\n" +
                "-r (optional)  RDT processing \n";

        String footer =
                "Examples:\nFragility.jar  -a <GeoJSON data> -hf <hazard fields>" +
                        " -i <identifiers> -e <estimator name>\n";

        System.out.println(header + footer);
    }

    private void checkFiles(String[] filePaths) {

        for (String fp : filePaths) {
            File f = new File(fp);
            if (!f.exists() || f.isDirectory()) {
                System.out.println("input file \"" + fp + "\" doesn't exist");
                System.exit(3);
            }
        }
    }

    /**
     * checks if a file exists
     *
     * @param filePath
     */
    private void checkFile(String filePath) {

        File f = new File(filePath);

        if (!f.exists() || f.isDirectory()) {
            System.out.println("input file \"" + filePath + "\" doesn't exist");
            System.out.println(f.getAbsolutePath());
            System.exit(3);
        }
    }

    boolean hasAssets() {
        return hasAssets;
    }

    public String getAssetInputPath() {
        return assetInputPath;
    }

    public String[] getHazardInputPaths() {
        return hazardInputPaths;
    }

    boolean hasIdentifiers() {
        return hasIdentifiers;
    }

    boolean hasHazards() {
        return hasHazards;
    }

    public String[] getIdentifiers() {
        return identifiers;
    }

    boolean isHasOutput() {
        return hasOutput;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getEstimator() {
        return estimator;
    }

    public boolean hasRDT() {
        return hasRDT;
    }

    public String getRdtInputPath(){
        return rdtInputPath;
    }
}
