package com.visilabs.inApp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class InAppSafeUtilsTest {

    @Test
    public void isBlank_handlesNullEmptyAndWhitespace() {
        assertTrue(InAppSafeUtils.isBlank(null));
        assertTrue(InAppSafeUtils.isBlank(""));
        assertTrue(InAppSafeUtils.isBlank("   "));
        assertFalse(InAppSafeUtils.isBlank("a"));
    }

    @Test
    public void normalizeColor_acceptsHexAndNamedColors() {
        assertEquals("#FFFFFF", InAppSafeUtils.normalizeColor("#FFFFFF"));
        assertEquals("#80ff0000", InAppSafeUtils.normalizeColor("#80ff0000"));
        assertEquals("#000000", InAppSafeUtils.normalizeColor(" 000000 "));
        assertEquals("red", InAppSafeUtils.normalizeColor("Red"));
        assertEquals("lightgrey", InAppSafeUtils.normalizeColor("lightgrey"));
    }

    @Test
    public void normalizeColor_rejectsInvalidValues() {
        assertNull(InAppSafeUtils.normalizeColor(null));
        assertNull(InAppSafeUtils.normalizeColor(""));
        assertNull(InAppSafeUtils.normalizeColor("#"));
        assertNull(InAppSafeUtils.normalizeColor("#FFF"));
        assertNull(InAppSafeUtils.normalizeColor("#GGGGGG"));
        assertNull(InAppSafeUtils.normalizeColor("rgba(0,0,0,1)"));
        assertNull(InAppSafeUtils.normalizeColor("undefined"));
        assertNull(InAppSafeUtils.normalizeColor("#1234567"));
    }

    @Test
    public void isValidColor_matchesNormalizeColor() {
        assertTrue(InAppSafeUtils.isValidColor("#123456"));
        assertFalse(InAppSafeUtils.isValidColor("#12345"));
        assertFalse(InAppSafeUtils.isValidColor(null));
    }

    @Test
    public void parseColor_returnsFallbackForInvalidValues() {
        assertEquals(42, InAppSafeUtils.parseColor(null, 42));
        assertEquals(42, InAppSafeUtils.parseColor("", 42));
        assertEquals(42, InAppSafeUtils.parseColor("#", 42));
        assertEquals(42, InAppSafeUtils.parseColor("not-a-color", 42));
    }

    @Test
    public void parseFloat_parsesValidNumbersAndFallsBackOtherwise() {
        assertEquals(5.5f, InAppSafeUtils.parseFloat("5.5", 1f), 0f);
        assertEquals(3f, InAppSafeUtils.parseFloat(" 3 ", 1f), 0f);
        assertEquals(1f, InAppSafeUtils.parseFloat(null, 1f), 0f);
        assertEquals(1f, InAppSafeUtils.parseFloat("", 1f), 0f);
        assertEquals(1f, InAppSafeUtils.parseFloat("undefined", 1f), 0f);
        assertEquals(1f, InAppSafeUtils.parseFloat("NaN", 1f), 0f);
        assertEquals(1f, InAppSafeUtils.parseFloat("Infinity", 1f), 0f);
        assertTrue(Float.isNaN(InAppSafeUtils.parseFloat("abc", Float.NaN)));
    }

    @Test
    public void parseInt_parsesIntegersAndDecimalsAndFallsBackOtherwise() {
        assertEquals(12, InAppSafeUtils.parseInt("12", 0));
        assertEquals(5, InAppSafeUtils.parseInt("5.9", 0));
        assertEquals(-3, InAppSafeUtils.parseInt(" -3 ", 0));
        assertEquals(7, InAppSafeUtils.parseInt(null, 7));
        assertEquals(7, InAppSafeUtils.parseInt("", 7));
        assertEquals(7, InAppSafeUtils.parseInt("abc", 7));
    }

    @Test
    public void replaceNewLines_isNullSafe() {
        assertEquals("", InAppSafeUtils.replaceNewLines(null));
        assertEquals("a\nb", InAppSafeUtils.replaceNewLines("a\\nb"));
        assertEquals("plain", InAppSafeUtils.replaceNewLines("plain"));
    }
}
