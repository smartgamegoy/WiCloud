package com.jetec.wicloud;

import android.annotation.SuppressLint;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Direction {

    private double cal;

    public Direction() {
        super();
    }

    public ArrayList<String> caculator(ArrayList<String> XList, ArrayList<String> YList) {
        ArrayList<String> direction = new ArrayList<>();
        direction.clear();

        for (int i = 0; i < XList.size(); i++) {
            double u = Double.valueOf(XList.get(i));
            double v = Double.valueOf(YList.get(i));
            if (u > 0 & v > 0) {
                cal = Math.atan(v / u) * 180.0 / Math.PI;
            } else if (u < 0 & v > 0) {
                cal = 180.0 + Math.atan(v / u) * 180.0 / Math.PI;
            } else if (u < 0 & v < 0) {
                cal = 180.0 + Math.atan(v / u) * 180.0 / Math.PI;
            } else if (u > 0 & v < 0) {
                cal = 360.0 + Math.atan(v / u) * 180.0 / Math.PI;
            } else if (u == 0 & v > 0) {
                cal = 0;
            } else if (u == 0 & v < 0) {
                cal = 180;
            } else if (u > 0 & v == 0) {
                cal = 90;
            } else if (u < 0 & v == 0) {
                cal = 270;
            } else if (u == 0 & v == 0) {
                cal = Double.NaN;
            }

            @SuppressLint("DrawAllocation")
            BigDecimal num = new BigDecimal(cal);
            cal = num.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            direction.add(String.valueOf(cal));
        }
        return direction;
    }
}