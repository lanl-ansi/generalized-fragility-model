package gov.lanl.nisac.fragility;

public class Fragility {

    private static CommandLineOptions parser;

    public static void main(String[] args) {
        checkOptions(args);
    }

    private static void checkOptions(String[] args) {
        parser = new CommandLineOptions(args);
        System.out.println(parser.getHazardInputPaths()[0]);
        System.out.println(parser.getHazardInputPaths()[1]);


    }
}
