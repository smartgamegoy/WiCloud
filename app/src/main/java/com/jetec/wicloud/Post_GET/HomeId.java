package com.jetec.wicloud.Post_GET;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jetec.wicloud.LoadHandler;
import com.jetec.wicloud.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

class HomeId {

    private static final String TAG = "HomeId";
    private Context context;
    private LoadHandler loadHandler;

    HomeId(Context context){
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
}
