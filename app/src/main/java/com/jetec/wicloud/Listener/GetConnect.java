package com.jetec.wicloud.Listener;

import org.json.JSONObject;

public class GetConnect {

    private ConnectListener connectListener;

    public void setListener(ConnectListener mConnectListener){
        connectListener = mConnectListener;
    }

    public void connected(JSONObject responseJson){
        if(connectListener != null && responseJson != null){
            connectListener.isConnected(responseJson);
        }
    }
}
