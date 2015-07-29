package com.haphazrd.movblox.Utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by brittanystubbs on 5/13/15.
 */
public class ColorChanger {

    private static String[] mColors = {
            "#FF6666", //red
            "#0099FF", //blue
            "#66CC66", //green
            "#9933CC" //purple
    };

    public static int getColor() {
        String color = "";
        Random random = new Random();
        int randomNumber = random.nextInt(mColors.length);

        color = mColors[randomNumber];
        int colorAsInt = Color.parseColor(color);

        return colorAsInt;
    }
}
