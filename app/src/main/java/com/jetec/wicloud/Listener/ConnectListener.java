package com.jetec.wicloud.Listener;

import org.json.JSONObject;

public interface ConnectListener {
    void isConnected(JSONObject responseJson);
}
