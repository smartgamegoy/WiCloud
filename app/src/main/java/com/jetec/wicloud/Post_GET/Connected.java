package com.jetec.wicloud.Post_GET;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jetec.wicloud.Listener.GetConnect;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.UserAccount;
import com.jetec.wicloud.ShowMessage;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Connected {

    private static final String TAG = "Connected";
    private Context context;
    private JSONObject responseJson;
    private UserAccount userAccount;
    private ShowMessage showMessage;

    public Connected(Context context) {
        this.context = context;
    }

    public void setConnect(String account, String password, GetConnect getConnect) {
        userAccount = new UserAccount(context);
        showMessage = new ShowMessage(context);

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        Map<String, String> map = new HashMap<>();
        map.put("projectId", "1010");
        map.put("appId", "webapp");
        map.put("email", account);  //tohsakarc@gmail.com
        map.put("password", password);  //jetec0000

        String url = "https://api.tinkermode.com/auth/user";
        GetObjectRequest getObjectRequest = new GetObjectRequest(Request.Method.POST, url, map,
                response -> {
                    if (response != null) {
                        responseJson = response;
                        if (userAccount.getCount() != 0) {
                            userAccount.update(account, password);
                        } else {
                            userAccount.insert(account, password);
                        }
                        userAccount.close();
                        getConnect.connected(responseJson);
                    }
                },
                error -> {
                    Log.d(TAG, "VolleyError = " + error.networkResponse);
                    showMessage.show(context.getString(R.string.loginerror));
                });
        getObjectRequest.setTag("post");
        requestQueue.add(getObjectRequest);
    }
}
