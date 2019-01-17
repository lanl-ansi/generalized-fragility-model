package gov.lanl.micot.application;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.ArrayList;


/**
 * Class that defines command line options
 */
public class CommandLineParameters {

    private static Options options = defineOptions();
    public static CommandLineParser parser;

    private boolean hasAssets;
    private boolean hasIdentifiers;
    private boolean hasHazards;
    private boolean hasOutput;


    private String assetInputPath;
    private String[] hazardInputPaths;
    private String[] identifiers;
    private String outputFilePath;
    private String responseEstimator;


    public CommandLineParameters() {
    }


    /**
     * Defines details for each option
     *
     * @return option value object
     */
    private static Options defineOptions() {
        options = new Options();

        options.addOption(Option.builder("a")
                .desc("assets GeoJSON input")
                .hasArg()
                .longOpt("assets")
                .build()
        );

        options.addOption(Option.builder("e")
                .desc("response responseEstimator identifier")
                .hasArg()
                .longOpt("responseEstimator")
                .build()
        );

        options.addOption(Option.builder("i")
                .desc("responseEstimator identifier - same order as hazard file names")
                .hasArgs()
                .valueSeparator(' ')
                .longOpt("identifiers")
                .build()
        );

        options.addOption(Option.builder("hf")
                .hasArgs()
                .valueSeparator(' ')
                .desc("hazard input file(s)")
                .longOpt("hazardFields")
                .build()
        );

        options.addOption(Option.builder("o")
                .desc("response responseEstimator output path")
                .hasArg()
                .longOpt("output")
                .build()
        );

        return options;
    }

    /**
     * General method that checks options and sets field values
     *
     * @param args
     * @throws ParseException
     */
    protected void parse(String[] args) throws ParseException {
        CommandLine commandLine;
        parser = new DefaultParser();

        // Parse the options and return the command line object.
        commandLine = parser.parse(options, args);

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
            responseEstimator = commandLine.getOptionValue("responseEstimator");
        }

        if (commandLine.hasOption("hazardFields")) {
            hasHazards = true;
            hazardInputPaths = commandLine.getOptionValues("hazardFields");
            hazardInputPaths = postProcess(hazardInputPaths); // getOptionValues doesn't like spaces in the file names
            checkFiles(hazardInputPaths);
        }
    }

    private String[] postProcess(String[] hazardInputPaths) {
    	ArrayList<String> temp = new ArrayList<String>();
    	
    	String str = "";
    	for (int i = 0; i < hazardInputPaths.length; ++i) {
    		String s = hazardInputPaths[i];
    		if (str.length() == 0) {
				str = s;
			}
			else {
				str += " " + s;
			}
    		
    		if (s.contains(".")) {// hack
    			temp.add(str);
    			str = "";
    		}
    	}
    	
    	
		// TODO Auto-generated method stub
		return temp.toArray(new String[0]);
	}


	/**
     * helper method that shows options
     */
    protected static void printHelp() {
        String header = "fragility  [OPTIONS]\n options:\n" +
                "-a               asset data\n" +
                "-hf              hazard field files \n" +
                "-i               identifiers\n" +
                "-e               responseEstimator identifier\n" +
                "-o  (optional)   output file name\n";

        String footer =
                "\nExample syntax:\nFragility.jar  -a <GeoJSON data> -hf <hazard fields>" +
                        " -i <identifiers> -e <responseEstimator name>\\n" +
                "\n\nFragility.jar  -a <GeoJSON data> -hf <hazard fields> " +
                        "-i <identifiers> -e <responseEstimator name> -so " +
                        "<filename> -num <number>\\n" +
                "\n\nFragility.jar  -r <RDT data> -ro <RDTpoleData.json> -hf <hazard fields> " +
                        "-i <identifiers> -e <responseEstimator name> -so " +
                        "<filename> -num <number>\\n\n";


        System.out.println(header + footer);
    }

    public static Options getCommandLineOptions(){
        return options;
    }

    public static CommandLineParser getCommandLineParser(){
        return parser;
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

    public boolean hasAssets() {
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

    public String getResponseEstimator() {
        return responseEstimator;
    }

    protected void setHazardInputPaths(String[] hazardInputPaths) {
        this.hazardInputPaths = hazardInputPaths;
    }

    protected void setIdentifiers(String[] identifiers) {
        this.identifiers = identifiers;
    }

    protected void setResponseEstimator(String estimator) {
        this.responseEstimator = estimator;
    }

}
