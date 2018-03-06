package gov.lanl.micot.application;

import gov.lanl.micot.application.fragility.Fragility;
import junit.framework.TestCase;

public class FragilityTest extends TestCase {

    String[] cmds;

    public void setUp() throws Exception {
        super.setUp();

        cmds = new String[8];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/iceField_example.asc test_data/fields/windField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "ice wind";
        cmds[6] = "-e";
        cmds[7] = "PowerPoleWindIceStress";

    }

    public void testMainRoutine(){

        Fragility.main(cmds);

    }

}