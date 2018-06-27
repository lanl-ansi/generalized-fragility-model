package gov.lanl.micot.application.rdt;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

public class FragilityRDTTest extends TestCase {

    @Test
    public void testMainRoutine() throws Exception {
        System.out.println("Test: testMainRoutine");
        System.out.println("Test: testExamplePoles");
        RDTParameters parser;
        String[] cmds = new String[14];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles.json";
        cmds[2] = "-num";
        cmds[3] = "3";
        cmds[4] = "-so";
        cmds[5] = "scenarioBlock.json";
        cmds[6] = "-o";
        cmds[7] = "fragility_output_example_poles.json";
        cmds[8] = "-i";
        cmds[9] = "wind";
        cmds[10] = "-e";
        cmds[11] = "PowerPoleWindStress";
        cmds[12] = "-hf";
        cmds[13] = "test_data/fields/windField_example.asc";

        FragilityRDT.main(cmds);

        File file;
        file = new File("scenarioBlock.json");
        assertTrue(file.exists());
        file.delete();


        file = new File("fragility_output_example_poles.json");
        assertTrue(file.exists());
        file.delete();

    }

}