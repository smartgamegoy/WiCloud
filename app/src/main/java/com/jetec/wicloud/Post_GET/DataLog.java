package com.jetec.wicloud.Post_GET;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataLog {

    private static final String TAG = "DataLog";
    private Context context;
    private String seriesId;

    public DataLog(Context context) {
        this.context = context;
    }

    public String getdatalog(String select) {

        try {
            if (select.matches(context.getString(R.string.sensor_temperature))) {
                select = "temperature";
            } else if (select.matches(context.getString(R.string.sensor_humidity))) {
                select = "humidity";
            } else if (select.matches(context.getString(R.string.sensor_wind_speed))) {
                select = "wind_speed";
            } else if (select.matches(context.getString(R.string.sensor_wind_direction))) {
                select = "wind_direction";
            } else if (select.matches(context.getString(R.string.sensor_precipitation))) {
                select = "precipitation";
            } else if (select.matches(context.getString(R.string.sensor_value))) {
                select = "value";
            }

            @SuppressLint("DrawAllocation")
            JSONObject eventData = new JSONObject(Value.socketvalue.get("eventData").toString());
            @SuppressLint("DrawAllocation")
            JSONArray timeSeriesData = new JSONArray(eventData.get("timeSeriesData").toString());
            for (int i = 0; i < timeSeriesData.length(); i++) {
                @SuppressLint("DrawAllocation")
                JSONObject obj = new JSONObject(timeSeriesData.get(i).toString());
                if (obj.get("seriesId").toString().contains(select)) {
                    seriesId = obj.get("seriesId").toString();
                    Log.d(TAG,"seriesId = " + seriesId);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return seriesId;
    }
}