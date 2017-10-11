package gov.lanl.nisac.lpnorm;

import gov.lanl.nisac.CommandLineOptions;
import junit.framework.TestCase;

import java.io.File;

public class RDTProcessingTest extends TestCase {
    private CommandLineOptions parser;
    public void setUp() throws Exception {

        String[] cmds = new String[16];

        cmds[0] = "-r";
        cmds[1] = "test_data/inputs/example_rdt.json";
        cmds[2] = "-num";
        cmds[3] = "3";
        cmds[4] = "-ro";
        cmds[5] = "rdt_gfm_output_poles.json";
        cmds[6] = "-so";
        cmds[7] = "scenarioBlock.json";
        cmds[8] = "-o";
        cmds[9] = "fragility_output_from_rdt.json";
        cmds[10] = "-i";
        cmds[11] = "wind";
        cmds[12] = "-e";
        cmds[13] = "wind";
        cmds[14] = "-hf";
        cmds[15] = "test_data/fields/windField_example.asc";

        parser = new CommandLineOptions(cmds);

    }

    public void testRDT(){
        String path = parser.getRdtInputPath();
        RDTProcessing.inferPoles(path, parser);

        // testing RDT option flags
        assertTrue(parser.hasRDT());
        assertTrue(parser.isHasRDTPoles());
        assertTrue(parser.isHasNumberOfScenarios());
        assertTrue(parser.isHasScenarioOutput());

        System.out.println("-- > option assertions complete.");

    }

    public void testOutputFiles(){

        File f1 = new File("fragility_output_from_rdt.json");
        System.out.println(f1.exists());
        assertTrue(f1.exists());
        f1.delete();

        File f2 = new File("rdt_gfm_output_poles.json");
        System.out.println(f2.exists());
        assertTrue(f2.exists());
        f2.delete();

        File f3 = new File("scenarioBlock.json");
        System.out.println(f3.exists());
        assertTrue(f3.exists());
        f3.delete();

    }

}