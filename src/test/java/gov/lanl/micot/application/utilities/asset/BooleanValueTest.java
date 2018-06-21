package gov.lanl.micot.application.utilities.asset;

import junit.framework.TestCase;
import org.junit.Test;

public class BooleanValueTest extends TestCase {

    @Test
    public void testBooleanValues() throws Exception {
        ValueTypes vt = new BooleanValue(true);

        assertTrue(vt.booleanValue());
    }

    @Test
    public void testBooleanStringValue() throws Exception {
        ValueTypes vt1 = new BooleanValue(true);

        String str1 = new String(vt1.StringValue());
        assertTrue(str1.equals("true"));

        ValueTypes vt2 = new BooleanValue(false);

        String str2 = new String(vt2.StringValue());
        assertTrue(str2.equals("false"));

    }

    @Test
    public void testisString() throws Exception {
        ValueTypes vt1 = new BooleanValue(true);
        assertFalse(vt1.isString());
    }

    @Test
    public void testIsBoolean() throws Exception {
        ValueTypes vt = new BooleanValue(true);

        assertTrue(vt.isBoolean());
    }


}