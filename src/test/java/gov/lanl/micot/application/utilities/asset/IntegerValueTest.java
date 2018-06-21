package gov.lanl.micot.application.utilities.asset;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegerValueTest {

    @Test
    public void intValue() {
        NumericTypes vt = new IntegerValue(112);
        int dv = vt.asInt();

        assertEquals(112, dv);
    }

    @Test
    public void doubleValue() {
    }

    @Test
    public void stringValue() {
    }

    @Test
    public void isInt() {
    }

    @Test
    public void isDouble() {
    }

    @Test
    public void isString() {
    }
}