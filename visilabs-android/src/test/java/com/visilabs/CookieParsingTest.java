package com.visilabs;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class CookieParsingTest {

    @Test
    public void emptyValue_returnsNull() {
        assertNull(Visilabs.parseCookieKeyValue("OM.3rd="));
        assertNull(Visilabs.parseCookieKeyValue("OM.3rd=; Max-Age=0; Path=/"));
    }

    @Test
    public void missingSeparator_returnsNull() {
        assertNull(Visilabs.parseCookieKeyValue("OM.3rd"));
        assertNull(Visilabs.parseCookieKeyValue("=value"));
        assertNull(Visilabs.parseCookieKeyValue(""));
        assertNull(Visilabs.parseCookieKeyValue(";"));
        assertNull(Visilabs.parseCookieKeyValue(null));
    }

    @Test
    public void keyValue_isParsedFromFirstField() {
        assertArrayEquals(new String[]{"OM.3rd", "abc"},
                Visilabs.parseCookieKeyValue("OM.3rd=abc; Path=/; HttpOnly"));
        assertArrayEquals(new String[]{"NSC_lgr", "ffffffff09"},
                Visilabs.parseCookieKeyValue("NSC_lgr=ffffffff09;expires=Wed, 14-Oct-2026 10:00:00 GMT;path=/"));
    }

    @Test
    public void valueContainingEquals_isKeptWhole() {
        assertArrayEquals(new String[]{"OM.3rd", "a=b=="},
                Visilabs.parseCookieKeyValue("OM.3rd=a=b==; Path=/"));
    }
}
