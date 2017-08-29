package gov.lanl.nisac.fragility.GFMcore;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;


public class GFMEngine {

    private ArrayList<HazardField> hazardFields;
    private ArrayList<GeometryObject> geometryObjects;

    private Multimap<String, String> responses;

    public void produceExposures() {

        responses = ArrayListMultimap.create();
        CoordinateReferenceSystem crs;// = grid.getCoordinateReferenceSystem2D();
        crs = hazardFields.get(0).getField().getCoordinateReferenceSystem2D();
        double x;
        double y;
        String value;
        double[] r;


        for (HazardField h : hazardFields) {

            for(GeometryObject g :geometryObjects){

                ArrayList<double[]> crd = g.getCoordinates();
                x = crd.get(0)[0];
                y = crd.get(0)[1];
                Coordinate crds = new Coordinate(x,y);
                DirectPosition p = JTS.toDirectPosition(crds, crs);
                r = h.getField().evaluate(p, new double[1]);
//                value = r[0];
                value = Double.toString(r[0]);

                responses.put(g.getIdentifier(), String.valueOf(value));
            }
        }

//        responses.forEach((k, v) ->
//                System.out.println(k + " - - " + v)
//        );

    }

    public String getExposures(String id){
        return responses.get(id).toString();
    }

    public void setGeometryObjects(ArrayList<GeometryObject> geometryObjects) {
        this.geometryObjects = geometryObjects;
    }

    public void setHazardfields(ArrayList<HazardField> hazardfields) {
        this.hazardFields = hazardfields;
    }
}
