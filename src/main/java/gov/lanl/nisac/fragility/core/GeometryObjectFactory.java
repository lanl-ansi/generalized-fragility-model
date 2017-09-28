package gov.lanl.nisac.fragility.core;

public class GeometryObjectFactory {
    /**
     * geometry object factory
     *
     * @param shapeType - specified shape type
     * @param AssetId   - asset identification
     * @return returns geometry object instance
     */
    public GeometryObject getGeometry(String shapeType, String AssetId) {

        if (shapeType == null) {
            return null;
        }

        if (shapeType.equalsIgnoreCase("LineString")) {
            return new GeometryLineString(AssetId);

        } else if (shapeType.equalsIgnoreCase("Point")) {
            return new GeometryPoint(AssetId);
        }

        return null;
    }
}
