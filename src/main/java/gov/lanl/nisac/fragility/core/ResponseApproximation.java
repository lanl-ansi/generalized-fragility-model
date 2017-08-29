package gov.lanl.nisac.fragility.core;

public class ResponseApproximation {

    private String assetId;
    private String geometryObject;
    private double value;

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setGeometryObject(String geometryObject) {
        this.geometryObject = geometryObject;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
