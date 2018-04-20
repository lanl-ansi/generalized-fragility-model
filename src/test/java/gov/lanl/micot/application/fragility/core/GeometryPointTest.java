package gov.lanl.micot.application.fragility.core;

import gov.lanl.micot.application.fragility.FragilityParameters;
import gov.lanl.micot.application.fragility.io.GFMDataReader;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GeometryPointTest {
    private FragilityParameters parser;
    private ArrayList<GeometryObject> dataAssets;

    @Before
    public void setUp() throws Exception {

        String[] cmds = new String[8];

        cmds[0] = "-a";
        cmds[1] = "test_data/inputs/example_poles_static_fragility.json";
        cmds[2] = "-hf";
        cmds[3] = "test_data/fields/windField_example.asc";
        cmds[4] = "-i";
        cmds[5] = "wind";
        cmds[6] = "-e";
        cmds[7] = "PowerPoleWindStress";

        parser = new FragilityParameters(cmds);

        String assets = parser.getAssetInputPath();

        // assets
        GFMDataReader gfmdr = new GFMDataReader();
        gfmdr.readGeoJsonFile(assets);
        this.dataAssets = gfmdr.getGeometryObjects();

        assertTrue(this.dataAssets.get(0) instanceof GeometryPoint );
    }

    @Test
    public void getIdentifier() {
        assertEquals(this.dataAssets.get(0).getIdentifier(),"0" );
    }

    @Test
    public void setCoordinates() {

        GeometryPoint obj = new GeometryPoint("55");
        double[] coords = new double[] { -73.4, 46.1};
        List crds = new ArrayList<>();
        crds.add(coords);
        obj.setCoordinates(crds);

        this.dataAssets.add(obj);

        assertEquals(this.dataAssets.get(2).getIdentifier(), "55");
        assertEquals(this.dataAssets.get(2).getCoordinates().get(0)[0],-73.4, 0.0001);
        assertEquals(this.dataAssets.get(2).getCoordinates().get(0)[1],46.1, 0.0001);
    }

    @Test
    public void getCoordinates() {

        assertEquals(this.dataAssets.get(1).getCoordinates().get(0)[0],-72.65588665338039, 0.00001);
        assertEquals(this.dataAssets.get(1).getCoordinates().get(0)[1],41.748887093791694, 0.00001);
    }
}