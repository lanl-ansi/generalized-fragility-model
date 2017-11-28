package gov.lanl.micot.application.utilities.asset;

public abstract class PropertyData {

    protected PropertyData() {
    }

    public int asInt() {
        return this.asInt(0);
    }

    public int asInt(int defaultValue) {
        return defaultValue;
    }

    public double asDouble() {
        return this.asDouble(0);
    }

    public double asDouble(int defaultValue) {
        return defaultValue;
    }

    public String asString() {
        return this.asString("");
    }

    public String asString(String defaultValue) {
        return defaultValue;
    }

    public boolean asBoolean() {
        return this.asBoolean(false);
    }

    public boolean asBoolean(boolean defaultValue) {
        return defaultValue;
    }

    public boolean isDouble() {
        return this.isDouble(false);
    }

    public boolean isInt(boolean defaultValue) {
        return defaultValue;
    }

    public boolean isInt() {
        return this.isInt(false);
    }

    public boolean isDouble(boolean defaultValue) {
        return defaultValue;
    }

    public boolean isString() {
        return this.isString(false);
    }

    public boolean isString(boolean defaultValue) {
        return defaultValue;
    }
}
