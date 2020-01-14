package com.hawla.flib.helpers;

public class Util {

    public static String fillIntegerTwoDigits(int i){
        if (i < 10){
            return "0" + i;
        }
        return Integer.toString(i);
    }
}
