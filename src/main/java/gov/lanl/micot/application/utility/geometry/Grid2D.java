package gov.lanl.micot.application.utility.geometry;

public interface Grid2D extends Geometry{

    /**
     * query location value
     * @return
     */
    public double getLocationValue(double[] latlon);
}
