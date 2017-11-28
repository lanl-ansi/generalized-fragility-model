package gov.lanl.micot.application.utilities.asset;

public class IntegerValue extends NumericTypes {

    private int assetValue;

    public IntegerValue(int value) {
        this.assetValue = value;
    }

    @Override
    public int intValue() {
        return this.assetValue;
    }

    @Override
    public double doubleValue() {
        return (double) this.assetValue;
    }

    public String StringValue(){
        return String.valueOf(this.assetValue);
    }

    @Override
    public boolean isInt() {
        return true;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public String stringValue() {
        return null;
    }

    public boolean isString(){
        return false;
    }
}
