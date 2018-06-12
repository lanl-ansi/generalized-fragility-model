package gov.lanl.micot.application;

import gov.lanl.micot.application.fragility.FragilityParameters;
import junit.framework.TestCase;

public class CommandLineParametersTest extends TestCase {
    private CommandLineParameters parser;

    public void setUp() throws Exception {
        super.setUp();

        String[] cmds = new String[8];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/iceField_example.asc test_data/fields/windField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "ice wind";
        cmds[6] = "-e";
        cmds[7] = "PowerPoleWindIceStress";

        parser = new FragilityParameters(cmds);

    }

    public void testHasAssets() throws Exception {
        assertTrue(parser.hasAssets());
    }

    public void testGetAssetInputPath() throws Exception {
        assertEquals(parser.getAssetInputPath(), "test_data/inputs/example_poles.json");
    }

    public void testGetHazardInputPaths() throws Exception {
        // String array !!
        assertEquals(parser.getHazardInputPaths()[0], "test_data/fields/iceField_example.asc");
        assertEquals(parser.getHazardInputPaths()[1], "test_data/fields/windField_example.asc");
    }

    public void testHasIdentifiers() throws Exception {
        assertTrue(parser.hasIdentifiers());
    }

    public void testHasHazards() throws Exception {
        assertTrue(parser.hasHazards());
    }

    public void testGetIdentifiers() throws Exception {
        assertEquals(parser.getIdentifiers()[0], "ice");
    }

    public void testIsHasOutput() throws Exception {
        assertFalse(parser.isHasOutput());
    }

    public void testGetOutputFilePath() throws Exception {
        assertEquals(parser.getOutputFilePath(), null);
    }

    public void testGetEstimator() throws Exception {
        assertEquals(parser.getResponseEstimator(), "PowerPoleWindIceStress");
    }

    public void testGetHelp() throws Exception{
        CommandLineParameters.printHelp();
    }


}