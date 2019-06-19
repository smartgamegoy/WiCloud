package com.jetec.wicloud;

public class GetUnit {

    private String TAG = "GetUnit";

    public GetUnit() {
        super();
    }

    public String unit(String sensor) {
        String unit = "";

        if (sensor.contains("temperature")) {
            unit = " " + (char) (186) + "C";
        } else if (sensor.contains("humidity")) {
            unit = " " + "%";
        } else if (sensor.contains("wind_speed")) {
            unit = " " + "m/s";
        } else if (sensor.contains("wind_direction")) {
            unit = " " + (char) (186);
        } else if (sensor.contains("precipitation")) {
            unit = " " + "mm";
        } else if (sensor.contains("value")) {
            unit = "";
        } else {
            unit = "";
        }

        return unit;
    }
}
