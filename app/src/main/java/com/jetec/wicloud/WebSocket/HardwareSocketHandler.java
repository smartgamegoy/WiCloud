package com.jetec.wicloud.WebSocket;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.jetec.wicloud.Listener.GetSocket;
import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.Value;

import org.json.JSONException;
import org.json.JSONObject;

public class HardwareSocketHandler extends Handler {

    private static final String TAG = "HardwareSocketHandler";
    private ListView listView;
    private TextView textView;
    private DeviceList deviceList;
    private Socket socket;
    private GetSocket getSocket;

    public HardwareSocketHandler() {
        super();
    }

    public Handler startHandler(ListView listView, TextView textView, DeviceList deviceList, Socket socket, GetSocket getSocket) {
        this.listView = listView;
        this.textView = textView;
        this.deviceList = deviceList;
        this.socket = socket;
        this.getSocket = getSocket;

        return new Handler(msg -> {
            String message = msg.obj.toString();
            try {
                Value.socketvalue = new JSONObject(message);
                getSocket.getNewmsg();
                Log.d(TAG, "成功連線");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        });
    }

    public void stopHandler() {
        startHandler(listView, textView, deviceList, socket, getSocket).removeCallbacksAndMessages(null);
    }
}
