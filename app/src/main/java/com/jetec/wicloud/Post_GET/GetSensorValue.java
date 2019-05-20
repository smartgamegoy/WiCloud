package com.jetec.wicloud.Post_GET;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jetec.wicloud.Listener.GetSpinner;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetSensorValue {

    private static final String TAG = "GetSensorValue";
    private Context context;
    private ArrayList<String> function;
    private String url;
    private GetSpinner getSpinner;

    public GetSensorValue(Context context) {
        this.context = context;
    }

    public ArrayList<String> getValue(String modelId, GetSpinner getSpinner) {

        this.getSpinner = getSpinner;
        url = "https://api.tinkermode.com/devices/" + modelId + "/kv";

        function = new ArrayList<>();
        function.clear();

        function.add(context.getString(R.string.add_sensor));

        new Thread(getSensors).start();

        return function;
    }

    private Runnable getSensors = new Runnable() {
        @Override
        public void run() {
            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            GetArrayRequest getArrayRequest = new GetArrayRequest(url, response -> {
                if (response != null) {
                    try {
                        JSONObject responseObj = response.getJSONObject(0);
                        Log.d(TAG, "responseObj = " + responseObj);
                        JSONObject responseObj2 = responseObj.getJSONObject("value");
                        Log.d(TAG, "responseObj2 = " + responseObj2);
                        JSONArray responseArr = responseObj2.getJSONArray("sensors");
                        Log.d(TAG, "responseArr = " + responseArr);
                        for (int i = 0; i < responseArr.length(); i++) {
                            String text = responseArr.get(i).toString()
                                    .substring(0, responseArr.get(i).toString().indexOf(":"));
                            String text2 = responseArr.get(i).toString()
                                    .substring(responseArr.get(i).toString().indexOf(":") + 1);
                            Log.d(TAG, "text2 = " + text2);
                            if (text.matches("TEMPERATURE")) {
                                function.add(context.getString(R.string.sensor_temperature) + text2);
                            } else if (text.matches("HUMIDITY")) {
                                function.add(context.getString(R.string.sensor_humidity) + text2);
                            } else if (text.matches("WIND_SPEED")) {
                                function.add(context.getString(R.string.sensor_wind_speed) + text2);
                            } else if (text.matches("WIND_DIRECTION")) {
                                function.add(context.getString(R.string.sensor_wind_direction) + text2);
                            } else if (text.matches("PRECIPITATION")) {
                                function.add(context.getString(R.string.sensor_precipitation) + text2);
                            } else if (text.matches("VALUE")) {
                                function.add(context.getString(R.string.sensor_value) + text2);
                            } else {
                                function.add("Unknown" + text2);
                            }
                        }
                        getSpinner.isGetspinner();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, error -> Log.d(TAG, "VolleyError = " + error.networkResponse)) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "ModeCloud " + Value.token);
                    return headers;
                }
            };
            requestQueue.add(getArrayRequest);
        }
    };
}