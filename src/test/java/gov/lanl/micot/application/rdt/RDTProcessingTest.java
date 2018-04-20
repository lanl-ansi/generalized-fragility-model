package gov.lanl.micot.application.rdt;

import junit.framework.TestCase;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class RDTProcessingTest extends TestCase {
    private RDTParameters parser;
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
        cmds[13] = "PowerPoleWindStress";
        cmds[14] = "-hf";
        cmds[15] = "test_data/fields/windField_example.asc";

        parser = new RDTParameters(cmds);

    }

    public void testRDT(){
        String path = parser.getRdtInputPath();
        RDTProcessing.inferPoles(path, parser);

        RDTProcessing.setNumberOfScenarios(5);
        RDTProcessing.setScenarioOutputPath("scenarioBlock.json");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // testing RDT option flags
        assertTrue(parser.isHasRDTPoles());
        assertTrue(parser.isHasNumberOfScenarios());
        assertTrue(parser.isHasScenarioOutput());

        System.out.println("-- > option assertions complete.");

    }

    public void testOutputFiles() throws InterruptedException {

        String path = parser.getRdtInputPath();
        RDTProcessing.inferPoles(path, parser);

        RDTProcessing.setNumberOfScenarios(5);
        RDTProcessing.setScenarioOutputPath("scenarioBlock.json");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // testing RDT option flags
        assertTrue(parser.isHasRDTPoles());
        assertTrue(parser.isHasNumberOfScenarios());
        assertTrue(parser.isHasScenarioOutput());

        System.out.println("-- > option assertions complete.");

        File frdt = new File("fragility_output_from_rdt.json");
        System.out.println(frdt.exists());
        assertTrue(frdt.exists());
        frdt.delete();

        File fpoles = new File("rdt_gfm_output_poles.json");
        System.out.println(fpoles.exists());
        assertTrue(fpoles.exists());
        fpoles.delete();

        File fscenario = new File("scenarioBlock.json");
        System.out.println(fscenario.exists());
        assertTrue(fscenario.exists());
        fscenario.delete();
        assertTrue(!fscenario.exists());

    }

}