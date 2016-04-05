package ru.binarysimple.ui;

import java.math.*;
import java.util.*;

import static java.lang.String.format;

class CurrOps {

    private static String replace(String str1) {
        return str1.replace(",", ".");
    }


    public static String convertToCurr(String num) {
        BigDecimal d1 = new BigDecimal(num);
        return format("%.2f", d1);//mult(curr, num, "1");
    }

    public static String currRound(String num, Integer precizion){
        num = replace(num);
        BigDecimal d1 = new BigDecimal(num);
        return format("%.2f", d1.setScale(precizion, RoundingMode.HALF_UP));
    }

    public static String mult(Currency curr, String num, String denom) {
        num = replace(num);
        denom = replace(denom);
        BigDecimal d1 = new BigDecimal(num);
        BigDecimal d2 = new BigDecimal(denom);
        BigDecimal d4 = new BigDecimal(100);

        int fracdig = curr.getDefaultFractionDigits();
        //BigDecimal d3 = d1.divide(d2, fracdig, BigDecimal.ROUND_DOWN);
        BigDecimal d3 = d1.multiply(d2);
        d3 = d3.divide(d4, fracdig, BigDecimal.ROUND_DOWN);
        return d3.toString();
    }

    public static String div(Currency curr, String num, String denom) {
        num = replace(num);
        denom = replace(denom);
        BigDecimal d1 = new BigDecimal(num);
        BigDecimal d2 = new BigDecimal(denom);
        BigDecimal d4 = new BigDecimal(100);

        int fracdig = curr.getDefaultFractionDigits();
        BigDecimal d3 = d1.divide(d2, fracdig, BigDecimal.ROUND_DOWN);
        //BigDecimal d3 = d1.multiply(d2);
        d3 = d3.divide(d4, fracdig, BigDecimal.ROUND_DOWN);
        return d3.toString();
    }

    public static String add(Currency curr, String num, String denom) {
        num = replace(num);
        denom = replace(denom);
        BigDecimal d1 = new BigDecimal(num);
        BigDecimal d2 = new BigDecimal(denom);
        //BigDecimal d4 = new BigDecimal(100);

        int fracdig = curr.getDefaultFractionDigits();
        BigDecimal d3 = d1.add(d2);
        //BigDecimal d3 = d1.multiply(d2);
        //d3 = d3.divide(d4, fracdig, BigDecimal.ROUND_DOWN);
        return d3.toString();
    }

    public static String sub(Currency curr, String num, String denom) {
        num = replace(num);
        denom = replace(denom);
        BigDecimal d1 = new BigDecimal(num);
        BigDecimal d2 = new BigDecimal(denom);
//        BigDecimal d4 = new BigDecimal(100);

        int fracdig = curr.getDefaultFractionDigits();
        BigDecimal d3 = d1.subtract(d2);
        //d1.di
        //BigDecimal d3 = d1.multiply(d2);
        //d3 = d3.divide(d4, fracdig, BigDecimal.ROUND_DOWN);
        return d3.toString();
    }

}
