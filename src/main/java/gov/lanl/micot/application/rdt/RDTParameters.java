package gov.lanl.micot.application.rdt;


import gov.lanl.micot.application.CommandLineParameters;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.json.simple.parser.ParseException;

import java.io.File;

public class RDTParameters extends CommandLineParameters {

    private static Options rdtOptions = defineOptions();
    public static CommandLineParser parser;
    private String rdtInputPath;

    private boolean hasNumberOfScenarios;
    private int numberOfScenarios;

    private boolean hasScenarioOutput;
    private String scenarioOutput;

    private String rdtPolesOutput;
    private boolean hasRDTPoles;

    private boolean hasWindField;
    private boolean hasPrintHelp;


    /**
     * Constructor
     *
     * @param args - command line arguments
     */
    public RDTParameters(String[] args) {
        super();

        try {
            parse(args);
            parseRDT(args);
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

        rdtOptions = CommandLineParameters.getCommandLineOptions();

        rdtOptions.addOption(Option.builder("h")
                .desc("Fragility RDT help")
                .longOpt("help")
                .build()
        );

        rdtOptions.addOption(Option.builder("wf")
                .hasArgs()
                .valueSeparator(' ')
                .desc("wind field raster")
                .longOpt("windField")
                .build()
        );

        rdtOptions.addOption(Option.builder("r")
                .desc("RDT processing")
                .hasArg()
                .longOpt("rdt")
                .build()
        );

        rdtOptions.addOption(Option.builder("o")
                .desc("response estimator output path")
                .hasArg()
                .longOpt("output")
                .build()
        );

        rdtOptions.addOption(Option.builder("num")
                .desc("number of scenarios to generate for RDT processing")
                .hasArg()
                .longOpt("numberScenarios")
                .build()
        );

        rdtOptions.addOption(Option.builder("so")
                .desc("RDT scenario block output path")
                .hasArg()
                .longOpt("RDTScenarioPath")
                .build()
        );

        rdtOptions.addOption(Option.builder("ro")
                .desc("RDT-to-Poles output path")
                .hasArg()
                .longOpt("RDTToPoles")
                .build()
        );

        return rdtOptions;
    }

    /**
     * General method that checks rdtOptions and sets field values
     *
     * @param args
     * @throws ParseException
     */
    private void parseRDT(String[] args) throws org.apache.commons.cli.ParseException {
        CommandLine commandLine;
        parser = CommandLineParameters.getCommandLineParser();

        // Parse the rdtOptions and return the command line object.
        commandLine = parser.parse(rdtOptions, args);

        // Check to see if only help is requested.
        if (commandLine.hasOption("help")) {
            setHasPrintHelp(true);
            printRDTHelp();
            System.exit(0);
        }

        if (commandLine.hasOption("rdt")) {
            rdtInputPath = commandLine.getOptionValue("rdt");
            checkFile(rdtInputPath);
        }

        // This option sets multiple flags
        if (commandLine.hasOption("wf")) {
            this.hasWindField = true;

            // set hazard field
            String[] windFieldInput = commandLine.getOptionValues("windField");
            checkFiles(windFieldInput);
            setHazardInputPaths(windFieldInput);


            // set identifier
            String[] identifier = {"wind"};
            setIdentifiers(identifier);

            // set estimator
            String estimatorRoutine = "PowerPoleWindStress";
            setResponseEstimator(estimatorRoutine);

        }

        if (commandLine.hasOption("numberScenarios")) {
            hasNumberOfScenarios = true;
            String num = commandLine.getOptionValue("numberScenarios");
            numberOfScenarios = Integer.parseInt(num);
        }

        if (commandLine.hasOption("RDTScenarioPath")) {
            hasScenarioOutput = true;
            scenarioOutput = commandLine.getOptionValue("RDTScenarioPath");
        }

        if (commandLine.hasOption("RDTToPoles")) {
            hasRDTPoles = true;
            rdtPolesOutput = commandLine.getOptionValue("RDTToPoles");
        }
    }

    /**
     * helper method that shows rdtOptions
     */
    public static void printRDTHelp() {
        String header = "fragility  [OPTIONS]\n rdtOptions:\n" +
                "-a               asset data\n" +
                "-hf              hazard field files \n" +
                "-i               identifiers\n" +
                "-e               estimator identifier\n" +
                "-wf (optional)   legacy wind field file input - do not run with -hf \n" +
                "-o  (optional)   output file name\n" +
                "-r  (optional)   RDT processing \n" +
                "-ro (optional)   generated poles output path \n" +
                "-so (optional)   use with -a (pole data) to produce RDT scenario block \n" +
                "-num (optional)  for RDT processing - number of scenarios to generate \n";

        String footer =
                "\nExample syntax:\nFragility.jar  -a <GeoJSON data> -hf <hazard fields>" +
                        " -i <identifiers> -e <estimator name>\\n" +
                        "\n\nFragility.jar  -a <GeoJSON data> -hf <hazard fields> " +
                        "-i <identifiers> -e <estimator name> -so " +
                        "<filename> -num <number>\\n" +
                        "\n\nFragility.jar  -r <RDT data> -ro <RDTpoleData.json> -hf <hazard fields> " +
                        "-i <identifiers> -e <estimator name> -so " +
                        "<filename> -num <number>\\n";


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
     * Checks if a file exists
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

    public boolean isHasNumberOfScenarios() {
        return hasNumberOfScenarios;
    }

    public int getNumberOfScenarios() {
        return numberOfScenarios;
    }

    public boolean isHasScenarioOutput() {
        return hasScenarioOutput;
    }

    public String getScenarioOutput() {
        return scenarioOutput;
    }

    public String getRdtPolesOutput() {
        return rdtPolesOutput;
    }

    public boolean isHasRDTPoles() {
        return hasRDTPoles;
    }

    public String getRdtInputPath(){
        return rdtInputPath;
    }

    public boolean isHasWindField() {
        return hasWindField;
    }

    public void setHasPrintHelp(boolean hasPrintHelp) {
        this.hasPrintHelp = hasPrintHelp;
    }

    public boolean getHasPrintHelp(){
        return this.hasPrintHelp;
    }


}
