package gov.lanl.micot.application.rdt;

/**
 * Default constant values for distribution pole.
 *
 */
public final class PoleConstants {

    private PoleConstants() {
    }

    // Generic Asset Attributes
    public static final float BASE_DIAMETER = 0.2222f;
    public static final float CABLE_SPAN = 90.0f;
    public static final float COMM_ATTACHMENT_HEIGHT = 4.7244f;
    public static final float COMM_CABLE_DIAMETER = 0.04f;
    public static final int COMM_CABLE_NUMBER = 2;
    public static final float COMM_CABLE_WIRE_DENSITY = 2700.0f;
    public static final float HEIGHT = 9.144f;
    public static final double MEAN_POLE_STRENGTH = 3.96e7;
    public static final float POWER_ATTACHMENT_HEIGHT = 5.6388f;
    public static final float POWER_CABLE_DIAMETER = 0.0094742f;
    public static final int POWER_CABLE_NUMBER = 3;
    public static final String POWER_CIRCUIT_NAME = "GFM-generated";
    public static final float POWER_CABLE_WIRE_DENSITY = 2700.0f;

    public static final double STD_DEV_POLE_STRENGTH = 7700000.0;
    public static final float TOP_DIAMETER = 0.153616f;
    public static final double WOOD_DENSITY = 500.0;

    // pole spacing - meters
    public static final double POLE_SPACING = 91.0;

    // next pole at 91 meters 0.000817463169242 degrees
    public static final double NEXT_DISTANCE = 0.000817463169242;

    // using 111.111 km per degree (or 111,111 meters)
    public static final double DEG_TO_METERS = 111111.0;

}
