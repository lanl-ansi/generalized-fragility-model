package gov.lanl.micot.application.utilities.asset;

public abstract class NumericTypes extends PropertyData {

    public abstract int intValue();

    public abstract double doubleValue();

    public abstract boolean isInt();

    public abstract boolean isDouble();

    public abstract boolean isString();

    public abstract String stringValue();


    public final int asInt() {
        return this.intValue();
    }

    public final int asInt(int defaultValue) {
        return this.intValue();
    }

    public final double asDouble() {
        return this.doubleValue();
    }

    public final double asDouble(int defaultValue) {
        return defaultValue;
    }

    public final String asString(){
        return this.stringValue();
    }

    public final String asString(int defaultValue){
        return this.stringValue();
    }

    public final String asString(double defaultValue){
        return this.stringValue();
    }


}
