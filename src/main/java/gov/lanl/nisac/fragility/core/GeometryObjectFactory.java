package gov.lanl.nisac.fragility.core;

public class GeometryObjectFactory {

    public GeometryObject getGeometry(String shapeType, String AssetId, GFMEngine broker) {

        if (shapeType == null) {
            return null;
        }

        if (shapeType.equalsIgnoreCase("LineString")) {
            return new GeometryLineString(AssetId, broker);
        } else if (shapeType.equalsIgnoreCase("Point")) {
            return new GeometryPoint(AssetId, broker);
        }

        return null;
    }
}
