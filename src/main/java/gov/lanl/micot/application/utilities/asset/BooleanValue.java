package gov.lanl.micot.application.utilities.asset;

public class BooleanValue extends ValueTypes {

    private boolean assetValue;

    public BooleanValue(boolean value){
        this.assetValue = value;
    }

    @Override
    public boolean booleanValue() {
        return this.assetValue;
    }

    @Override
    public String StringValue() {
        return "";
    }

    @Override
    public boolean isString() {
        return false;
    }
}
