package gov.lanl.micot.application.utilities.asset;

public class StringValue extends ValueTypes {

    private String assetValue;

    public StringValue(String value){
        this.assetValue = value;
    }

    @Override
    public String StringValue() {
        return this.assetValue;
    }

    @Override
    public boolean booleanValue() {
        return false;
    }

    @Override
    public boolean isString(){
        return true;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }
}
