package com.jetec.wicloud.Post_GET;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jetec.wicloud.Listener.GetHardware;
import com.jetec.wicloud.LoadHandler;
import com.jetec.wicloud.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class HomeId {

    private static final String TAG = "HomeId";
    private Context context;
    private LoadHandler loadHandler;

    public HomeId(Context context){
        this.context = context;
    }

    void getValue(JSONArray HomeRequest, LoadHandler loadHandler){
        try {
            this.loadHandler = loadHandler;
            JSONObject HomeObject = HomeRequest.getJSONObject(0);
            Value.homeid = HomeObject.get("id").toString();
            setHomeId();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setHomeId(){

        String url = "https://api.tinkermode.com/devices?homeId=" + Value.homeid;

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        GetArrayRequest getArrayRequest = new GetArrayRequest(url,
                response -> {
                    if (response != null) {
                        Value.model = response;
                        loadHandler.loadover();
                    }
                },
                error -> Log.d(TAG, "VolleyError = " + error.networkResponse))
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "ModeCloud " + Value.token);
                return headers;
            }
        };
        getArrayRequest.setTag("start");
        requestQueue.add(getArrayRequest);
    }

    public void gethomeList(LoadHandler loadHandler, GetHardware getHardware){
        String url = "https://api.tinkermode.com/devices?homeId=" + Value.homeid;

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        GetArrayRequest getArrayRequest = new GetArrayRequest(url,
                response -> {
                    if (response != null) {
                        Value.model = response;
                        getStatus(loadHandler, getHardware);
                    }
                },
                error -> Log.d(TAG, "VolleyError = " + error.networkResponse))
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "ModeCloud " + Value.token);
                return headers;
            }
        };
        getArrayRequest.setTag("start");
        requestQueue.add(getArrayRequest);
    }

    private void getStatus(LoadHandler loadHandler, GetHardware getHardware){
        String url = "https://api.tinkermode.com/homes/" + Value.homeid + "/kv?keyPrefix=sensorModule";

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        GetArrayRequest getArrayRequest = new GetArrayRequest(url,
                response -> {
                    if (response != null) {
                        Value.homevalue = response;
                        Log.d(TAG, "response = " + response);
                        loadHandler.loadover();
                        getHardware.startshow(response);
                    }
                },
                error -> Log.d(TAG, "VolleyError = " + error.networkResponse))
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "ModeCloud " + Value.token);
                return headers;
            }
        };
        getArrayRequest.setTag("start");
        requestQueue.add(getArrayRequest);
    }
}
