package gov.lanl.micot.application.utilities.asset;

public abstract class NumericTypes extends PropertyData {

    public abstract int intValue();

    public abstract double doubleValue();

    public abstract boolean isInt();

    public abstract boolean isDouble();

    public abstract boolean isString();

    public abstract String stringValue();


    @Override
    public final int asInt() {
        return this.intValue();
    }

    @Override
    public final int asInt(int defaultValue) {
        return this.intValue();
    }

    @Override
    public final double asDouble() {
        return this.doubleValue();
    }

    @Override
    public final double asDouble(int defaultValue) {
        return defaultValue;
    }

    @Override
    public final String asString(){
        return this.stringValue();
    }

}
