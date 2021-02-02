package com.visilabs.util;

import android.graphics.Color;

import java.util.HashMap;

public class ColorUtils {

    public static HashMap<Integer, int[]> calculateGradientColors(int rateCount, int[] colors) {
        HashMap<Integer, int[]> cells = new HashMap<>();
        if (colors.length == 2) {
            int red1 = Color.red(colors[0]);
            int red2 = Color.red(colors[1]);
            int green1 = Color.green(colors[0]);
            int green2 = Color.green(colors[1]);
            int blue1 = Color.blue(colors[0]);
            int blue2 = Color.blue(colors[1]);

            int redInterval = Math.abs(red1 - red2) / (rateCount + 1) * (red1 < red2 ? 1 : -1);
            int greenInterval = Math.abs(green1 - green2) / (rateCount + 1) * (green1 < green2 ? 1 : -1);
            int blueInterval = Math.abs(blue1 - blue2) / (rateCount + 1) * (blue1 < blue2 ? 1 : -1);

            for(int i = 1; i < rateCount + 1 ; i++) {
                int firstColor = Color.argb(255,
                        red1 + (i - 1) * redInterval,
                        green1 + (i - 1) * greenInterval,
                        blue1 + (i - 1) * blueInterval);
                int secondColor = Color.argb(255,
                        red1 + i * redInterval,
                        green1 + i * greenInterval,
                        blue1 + i * blueInterval);
                cells.put(i, new int[] { firstColor, secondColor });
            }
        }  else if (colors.length == 3) {
            int red1 = Color.red(colors[0]);
            int red2 = Color.red(colors[1]);
            int red3 = Color.red(colors[2]);
            int green1 = Color.green(colors[0]);
            int green2 = Color.green(colors[1]);
            int green3 = Color.green(colors[2]);
            int blue1 = Color.blue(colors[0]);
            int blue2 = Color.blue(colors[1]);
            int blue3 = Color.blue(colors[2]);

            int redInterval1 = Math.abs(red1 - red2) / ( (rateCount / 2) + 1) * (red1 < red2 ? 1 : -1);
            int greenInterval1 = Math.abs(green1 - green2) / ( (rateCount / 2) + 1) * (green1 < green2 ? 1 : -1);
            int blueInterval1 = Math.abs(blue1 - blue2) / ( (rateCount / 2) + 1) * (blue1 < blue2 ? 1 : -1);

            int redInterval2 = Math.abs(red2 - red3) / ( (rateCount / 2) + 1) * (red2 < red3 ? 1 : -1);
            int greenInterval2 = Math.abs(green2 - green3) / ( (rateCount / 2) + 1) * (green2 < green3 ? 1 : -1);
            int blueInterval2 = Math.abs(blue2 - blue3) / ( (rateCount / 2) + 1) * (blue2 < blue3 ? 1 : -1);

            for(int i = 1; i < (rateCount / 2) + 1 ; i++) {
                int firstColor = Color.argb(255,
                        red1 + (i - 1) * redInterval1,
                        green1 + (i - 1) * greenInterval1,
                        blue1 + (i - 1) * blueInterval1);
                int secondColor = Color.argb(255,
                        red1 + i * redInterval1,
                        green1 + i * greenInterval1,
                        blue1 + i * blueInterval1);
                cells.put(i, new int[] { firstColor, secondColor });
            }

            for(int i = (rateCount / 2) + 1; i < rateCount + 1  ; i++) {
                int firstColor = Color.argb(255,
                        red2 + (i - (rateCount / 2) - 1) * redInterval2,
                        green2 + (i - (rateCount / 2) - 1) * greenInterval2,
                        blue2 + (i - (rateCount / 2) - 1) * blueInterval2);
                int secondColor = Color.argb(255,
                        red2 + (i - (rateCount / 2)) * redInterval2,
                        green2 + (i - (rateCount / 2)) * greenInterval2,
                        blue2 + (i - (rateCount / 2)) * blueInterval2);
                cells.put(i, new int[] { firstColor, secondColor });
            }

        } else {
            for(int i = 1; i < rateCount + 1 ; i++) {
                cells.put(i, new int[] { colors[0] });
            }
        }

        return cells;
    }
}
