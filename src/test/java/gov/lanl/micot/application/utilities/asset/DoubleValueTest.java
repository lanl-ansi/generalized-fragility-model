package gov.lanl.micot.application.utilities.asset;

import org.junit.Test;

import static org.junit.Assert.*;

public class DoubleValueTest {

    @Test
    public void intValue() {
        NumericTypes vt = new DoubleValue(1.23456);
        int dv = vt.asInt();

        assertEquals(1, dv);
    }

    @Test
    public void doubleValue() {
        NumericTypes vt = new DoubleValue(1.23456);
        double dv = vt.asDouble();

        assertEquals(vt.asDouble(), dv, 0.00001);
    }

    @Test
    public void isInt() {
        NumericTypes vt = new DoubleValue(2.23456);
        assertFalse(vt.isInt());

    }

    @Test
    public void isDouble() {
        NumericTypes vt = new DoubleValue(2.23456);
        assertTrue(vt.isDouble());
    }

    @Test
    public void isString() {
        NumericTypes vt = new DoubleValue(2.23456);
        assertFalse(vt.isString());
    }

    @Test
    public void stringValue() {
        NumericTypes vt = new DoubleValue(2.23456);
        String val = vt.stringValue();

        assertEquals(val, "2.23456");
    }

    @Test
    public void asInt() {
        NumericTypes vt = new DoubleValue(2.23456);
        int val = vt.asInt();

        assertEquals(val, 2);
    }

    @Test
    public void asDouble() {
        NumericTypes vt = new DoubleValue(2.23456);

        assertEquals(vt.asDouble(), 2.23456, 0.00001);
    }

    @Test
    public void asString() {
        NumericTypes vt = new DoubleValue(2.23456);
        String val = vt.asString();

        assertEquals(val, "2.23456");
    }


}