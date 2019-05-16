package com.jetec.wicloud.Post_GET;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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

    public GetSensorValue(Context context){
        this.context = context;
    }

    public ArrayList<String> getValue(String modelId){

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
                        JSONObject responseObj2 = responseObj.getJSONObject("value");
                        JSONArray responseArr = responseObj2.getJSONArray("sensors");
                        for(int i = 0; i < responseArr.length(); i++){
                            String text = responseArr.get(i).toString()
                                    .substring(0, responseArr.get(i).toString().indexOf(":"));
                            if(text.matches("TEMPERATURE")){
                                function.add(context.getString(R.string.sensor_temperature));
                            }
                            else if(text.matches("HUMIDITY")){
                                function.add(context.getString(R.string.sensor_humidity));
                            }
                            else if(text.matches("WIND_SPEED")){
                                function.add(context.getString(R.string.sensor_wind_speed));
                            }
                            else if(text.matches("WIND_DIRECTION")){
                                function.add(context.getString(R.string.sensor_wind_direction));
                            }
                            else if(text.matches("PRECIPITATION")){
                                function.add(context.getString(R.string.sensor_precipitation));
                            }
                            else if(text.matches("VALUE")){
                                function.add(context.getString(R.string.sensor_value));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, error -> Log.d(TAG,"VolleyError = " + error.networkResponse)){
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