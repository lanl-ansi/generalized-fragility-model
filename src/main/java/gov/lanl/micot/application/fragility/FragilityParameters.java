package gov.lanl.micot.application.fragility;

import gov.lanl.micot.application.CommandLineParameters;
import org.apache.commons.cli.*;

import java.io.File;

public class FragilityParameters extends CommandLineParameters{

    private static Options fragilityOptions = defineOptions();

    public FragilityParameters(String args[]){
        super();

        try {
            parse(args);
            parseOptions(args);
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
        }

    }
    /**
     * Defines details for each option
     *
     * @return option value object
     */
    private static Options defineOptions() {

        fragilityOptions = CommandLineParameters.getCommandLineOptions();

        fragilityOptions.addOption(Option.builder("h")
                .desc("Fragility options help ")
                .longOpt("help")
                .build()
        );

        return fragilityOptions;
    }

    /**
     * General method that checks fragilityOptions and sets field values
     *
     * @param args - commandline arguments
     * @throws ParseException
     */
    public void parseOptions(String[] args) throws ParseException {
        CommandLine commandLine;
        parser = CommandLineParameters.getCommandLineParser();

        // Parse the fragilityOptions and return the command line object.
        commandLine = parser.parse(fragilityOptions, args);

        // Check to see if only help is requested.
        if (commandLine.hasOption("help")) {
            System.out.println("Fragility help:");
            printHelp();
            System.exit(0);
        }
    }

    /**
     * helper method that shows fragilityOptions
     */
    public static void printHelp() {
        String header = "fragility  [OPTIONS]\n fragilityOptions:\n" +
                "-a               asset data\n" +
                "-hf              hazard field files \n" +
                "-i               identifiers\n" +
                "-e               estimator identifier\n" +
                "-o  (optional)   output file name\n";

        String footer =
                "\nExample syntax:\nFragility.jar  -a <GeoJSON data> -hf <hazard fields>" +
                        " -i <identifiers> -e <estimator name>\\n" +
                        "\n\nFragility.jar  -a <GeoJSON data> -hf <hazard fields> " +
                        "-i <identifiers> -e <estimator name> -so " +
                        "<filename> -num <number>\\n" +
                        "\n\nFragility.jar  -r <RDT data> -ro <RDTpoleData.json> -hf <hazard fields> " +
                        "-i <identifiers> -e <estimator name> -so " +
                        "<filename> -num <number>\\n\n";


        System.out.println(header + footer);
    }

    public static Options getCommandLineOptions(){
        return fragilityOptions;
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


}
