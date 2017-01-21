package com.taihuoniao.fineix.utils;

import java.text.DecimalFormat;

/**
 * Created by Stephen on 2017/1/21 11:04
 * Email: 895745843@qq.com
 */

public class StringFormatUtils {

    public static String formatMoney(double d){
       return new DecimalFormat("######0.00").format(d);
    }

    public static String formatMoney(String s){
        double d;
        try {
            d = Double.parseDouble(s);
            return String.format("¥ %.2f", d);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "¥ 0.00";
    }
}
