package gov.lanl.micot.application.fragility;

import junit.framework.TestCase;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;

public class FragilityParametersTest extends TestCase {
    private FragilityParameters parser;
    String[] cmds;

    @Before
    public void setUp() throws Exception {

        cmds = new String[10];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/windField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "wind";
        cmds[6] = "-e";
        cmds[7] = "PowerPoleWindStress";
        cmds[8] = "-o";
        cmds[9] = "fragility_output.json";

        this.parser = new FragilityParameters(cmds);

    }

    public void testParseOptions() {

        FragilityParameters fp = new FragilityParameters(cmds);

        String pp = fp.getAssetInputPath();
        String re = fp.getResponseEstimator();
        String[] hp = fp.getHazardInputPaths();

        assertEquals(pp, "test_data/inputs/example_poles.json");
        assertEquals(re, "PowerPoleWindStress");
        assertEquals(hp[0], "test_data/fields/windField_example.asc");


    }

    public void testGetCommandLineOptions() throws ParseException {

        FragilityParameters fp = new FragilityParameters(cmds);

        Options clo = fp.getCommandLineOptions();

        assertTrue(clo.hasOption("a"));
        assertTrue(clo.hasOption("hf"));
        assertTrue(clo.hasOption("i"));
        assertTrue(clo.hasOption("e"));
        assertTrue(clo.hasOption("o"));

    }

    public void testGetCommandLineParser() {

        FragilityParameters fp = new FragilityParameters(cmds);

        CommandLineParser clp = FragilityParameters.getCommandLineParser();

        assertTrue(clp instanceof CommandLineParser);


    }
}