package com.visilabs.android;

import com.visilabs.util.AppUtils;
import com.visilabs.util.UtilResultModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AppUtilsTest {

    @Test
    public void testGetNumberFromText() {
        boolean result;
        String input1 = "";
        String input2 = null;
        String input3 = "<COUNT>150</COUNT> people are viewing this product.";
        String input4 = "People : <COUNT>150</COUNT> are viewing this product.";
        String input5 = "The number of people who are viewing this product is <COUNT>150</COUNT>";
        String input6 = "<COUNT>150</COUNT> people have viewed this product in 2021";
        String input7 = "<COUNT>150</COUNT> people (<COUNT>150</COUNT>) have viewed this product in the last 24 hours.";
        boolean res1;
        boolean res2;
        boolean res3;
        boolean res4;
        boolean res5;
        boolean res6;
        boolean res7;
        UtilResultModel model3;
        UtilResultModel model4;
        UtilResultModel model5;
        UtilResultModel model6;
        UtilResultModel model7;

        res1 = AppUtils.getNumberFromText(input1) == null;
        res2 = AppUtils.getNumberFromText(input2) == null;

        model3 = AppUtils.getNumberFromText(input3);
        res3 = model3.getIsTag() && model3.getNumbers().size() == 1 &&
                model3.getNumbers().get(0) == 150 &&
                model3.getMessage().equals("150 people are viewing this product.");

        model4 = AppUtils.getNumberFromText(input4);
        res4 = model4.getIsTag() && model4.getNumbers().size() == 1 &&
                model4.getNumbers().get(0) == 150 &&
                model4.getMessage().equals("People : 150 are viewing this product.");

        model5 = AppUtils.getNumberFromText(input5);
        res5 = model5.getIsTag() && model5.getNumbers().size() == 1 &&
                model5.getNumbers().get(0) == 150 &&
                model5.getMessage().equals("The number of people who are viewing this product is 150");

        model6 = AppUtils.getNumberFromText(input6);
        res6 = model6.getIsTag() && model6.getNumbers().size() == 1 &&
                model6.getNumbers().get(0) == 150 &&
                model6.getMessage().equals("150 people have viewed this product in 2021");

        model7 = AppUtils.getNumberFromText(input7);
        res7 = model7.getIsTag() && model7.getNumbers().size() == 2 &&
                model7.getNumbers().get(0) == 150 && model7.getNumbers().get(1) == 150 &&
                model7.getMessage().equals("150 people (150) have viewed this product in the last 24 hours.");

        result = res1 && res2 && res3 && res4 && res5 && res6 && res7;

        assert(result);

    }
}
