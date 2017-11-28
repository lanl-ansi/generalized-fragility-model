package gov.lanl.micot.application.utilities.asset;

public class DoubleValue extends NumericTypes {

    private double assetValue;

    public DoubleValue(double value) {
        this.assetValue =  value;
    }

    @Override
    public int intValue() {
        return (int) this.assetValue;
    }

    @Override
    public double doubleValue() {
        return this.assetValue;
    }

    @Override
    public boolean isInt() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String stringValue() {
        return null;
    }

}
