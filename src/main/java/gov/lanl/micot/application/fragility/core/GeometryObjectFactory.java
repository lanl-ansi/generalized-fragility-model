package gov.lanl.micot.application.fragility.core;

/**
 * This class defines a GeoJSON Factory
 *
 * @author TRevor Crawford
 */
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

        } else if (shapeType.equalsIgnoreCase("MultiPoint")) {
            return new GeometryMultiPoint(AssetId);

        } else {
            System.out.println(shapeType + " <-- Geometry type not recognized for " + AssetId);
            return null;
        }
    }
}
