package com.jetec.wicloud.WebSocket;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.Value;
import org.json.JSONException;
import org.json.JSONObject;

public class SocketHandler extends Handler {

    private static final String TAG = "SocketHandler";
    private ListView listView;
    private Parcelable state;
    private TextView textView;
    private DeviceList deviceList;
    private Socket socket;

    public SocketHandler() {
        super();
    }

    public Handler startHandler(ListView listView, TextView textView, DeviceList deviceList, Socket socket) {
        this.listView = listView;
        this.textView = textView;
        this.deviceList = deviceList;
        this.socket = socket;
        //websocket handler
        return new Handler(msg -> {
            String message = msg.obj.toString();
            try {
                Value.socketvalue = new JSONObject(message);
                if(deviceList.getCount() != 0) {
                    relocate();
                    listView.setAdapter(Value.dataList);
                    if (state != null)
                        listView.onRestoreInstanceState(state);
                }
                else {
                    stopHandler();
                    socket.closeConnect();
                    textView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    Log.d(TAG,"stop");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        });
    }

    public void stopHandler() {
        startHandler(listView, textView, deviceList, socket).removeCallbacksAndMessages(null);
    }

    private void relocate() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    state = listView.onSaveInstanceState();
                    Log.d(TAG, "state = " + state);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}
