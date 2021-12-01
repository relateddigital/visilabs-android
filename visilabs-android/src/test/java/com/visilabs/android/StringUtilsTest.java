package com.visilabs.android;

import com.visilabs.util.StringUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StringUtilsTest {

    @Test
    public void testIsNullOrWhiteSpace() {
        String input1 = null;
        String input2 = "";
        String input3 = " ";
        String input4 = "  ";
        String input5 = "test string";
        String input6 = "&";
        boolean result = StringUtils.isNullOrWhiteSpace(input1) &&
                StringUtils.isNullOrWhiteSpace(input2) &&
                StringUtils.isNullOrWhiteSpace(input3) &&
                StringUtils.isNullOrWhiteSpace(input4) &&
                !StringUtils.isNullOrWhiteSpace(input5) &&
                !StringUtils.isNullOrWhiteSpace(input6);
        assert(result);
    }

    @Test
    public void testSplitRGBA() {
        String input = "rgba(145),rgba(137),rgba(243)";
        String[] res;
        boolean result;

        res = StringUtils.splitRGBA(input);

        result = (res.length == 3) && res[0].equals("145") && res[1].equals("137") &&
                res[2].equals("243");

        assert(result);
    }

    @Test
    public void testValidateHexColor() {
        String input1 = "#ffffff";
        String input2 = "test";

        boolean result;

        result = StringUtils.validateHexColor(input1) && !StringUtils.validateHexColor(input2);

        assert(result);
    }
}
