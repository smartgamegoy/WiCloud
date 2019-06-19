package com.jetec.wicloud.Post_GET;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jetec.wicloud.LoadHandler;
import com.jetec.wicloud.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UserId {

    private static final String TAG = "UserId";
    private Context context;
    private LoadHandler loadHandler;

    public UserId(Context context){
        this.context = context;

    }

    public void getValue(JSONObject responseJson, LoadHandler loadHandler){
        try {
            this.loadHandler = loadHandler;
            Value.token = responseJson.get("token").toString();
            Value.userId = responseJson.get("userId").toString();
            Log.d(TAG,"token = " + Value.token + "\n" + "userId = " + Value.userId);
            setUserId();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUserId(){

        String url = "https://api.tinkermode.com/homes?userId=" + Value.userId;

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        Map<String, String> map = new HashMap<>();
        map.put("userId", Value.userId);

        GetArrayRequest getArrayRequest = new GetArrayRequest(url, map,
                response -> {
                    if (response != null) {
                        HomeId homeId = new HomeId(context);
                        homeId.getValue(response, loadHandler);
                    }
                },
                error -> {
                    Log.d(TAG, "VolleyError = " + error.networkResponse);
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "ModeCloud " + Value.token);
                return headers;
            }
        };
        getArrayRequest.setTag("GetHomeId");
        requestQueue.add(getArrayRequest);
    }
}
