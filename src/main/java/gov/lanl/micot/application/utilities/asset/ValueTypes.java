package gov.lanl.micot.application.utilities.asset;

public abstract class ValueTypes extends PropertyData {
    public abstract boolean booleanValue();

    public abstract String StringValue();

    public abstract boolean isString();

    protected ValueTypes() {
    }

    public final boolean asBoolean() {
        return this.booleanValue();
    }

    public final boolean asBoolean(boolean defaultValue) {
        return this.booleanValue();
    }

    public final String asString() {
        return this.StringValue();
    }

}
