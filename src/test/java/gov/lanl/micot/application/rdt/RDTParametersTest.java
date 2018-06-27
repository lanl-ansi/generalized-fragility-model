package gov.lanl.micot.application.rdt;

import junit.framework.TestCase;
import org.junit.Test;


public class RDTParametersTest extends TestCase {

    private RDTParameters parser;

    public void setUp() throws Exception {
        super.setUp();
        String[] cmds = new String[16];

        cmds[0] = "-r";
        cmds[1] = "test_data/inputs/example_rdt.json";
        cmds[2] = "-i";
        cmds[3] = "wind";
        cmds[4] = "-e";
        cmds[5] = "wind";
        cmds[6] = "-hf";
        cmds[7] = "test_data/fields/windField_example.asc";
        cmds[8] = "-ro";
        cmds[9] = "RDT_Poles.json";
        cmds[10] = "-o";
        cmds[11] = "repsonses.json";
        cmds[12] = "-so";
        cmds[13] = "SCENARIOS.json";
        cmds[14] = "-num";
        cmds[15] = "13";

        parser = new RDTParameters(cmds);

    }

    @Test
    public void testisHasNumberOfScenarios() throws Exception {

        assertTrue(parser.isHasNumberOfScenarios());

    }

    @Test
    public void testgetNumberOfScenarios() throws Exception {

        assertEquals(parser.getNumberOfScenarios(), 13);
    }

    @Test
    public void testisHasScenarioOutput() throws Exception {


        assertTrue(parser.isHasScenarioOutput());
    }

    @Test
    public void testgetScenarioOutput() throws Exception {


        assertEquals(parser.getScenarioOutput(), "SCENARIOS.json");
    }

    @Test
    public void testgetRdtPolesOutput() throws Exception {
    }

    @Test
    public void testisHasRDTPoles() throws Exception {
    }

    @Test
    public void testgetRdtInputPath() throws Exception {
    }

}