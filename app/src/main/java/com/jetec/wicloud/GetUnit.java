package com.jetec.wicloud;

public class GetUnit {

    private String TAG = "GetUnit";

    public GetUnit() {
        super();
    }

    public String unit(String sensor) {
        String unit = "";

        if (sensor.matches("temperature")) {
            unit = " " + (char) (186) + "C";
        } else if (sensor.matches("humidity")) {
            unit = " " + "%";
        } else if (sensor.matches("wind_speed")) {
            unit = " " + "m/s";
        } else if (sensor.matches("wind_direction")) {
            unit = " " + (char) (186);
        } else if (sensor.matches("precipitation")) {
            unit = " " + "mm";
        } else if (sensor.matches("value")) {
            unit = "";
        } else {
            unit = "";
        }

        return unit;
    }
}
