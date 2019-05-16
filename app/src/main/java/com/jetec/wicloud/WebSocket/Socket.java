package com.jetec.wicloud.WebSocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.jetec.wicloud.Value;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.Date;

public class Socket {

    private static final String TAG = "Socket";
    private StringBuilder sb = new StringBuilder();
    private WebSocketClient webSocketClient;
    private Boolean connect = false;

    public Socket(){
        super();
    }

    public void getWebSocket(Handler handler){

        URI serverURI = URI.create("wss://api.tinkermode.com/userSession/websocket?authToken=" + Value.token);
        connect = true;
        webSocketClient = new WebSocketClient(serverURI) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d(TAG,"開始連線...");
            }
            @Override
            public void onMessage(String message) {
                Message handlerMessage = Message.obtain();
                handlerMessage.obj = message;
                handler.sendMessage(handlerMessage);
                Log.d(TAG,"message = " + message);
            }
            @Override
            public void onClose(int code, String reason, boolean remote) {
                sb.append(code);
                sb.append(reason);
                sb.append(remote);
                Log.d(TAG,"關閉連線 = " + sb.toString());
            }
            @Override
            public void onError(Exception ex) {
                sb.append(new Date());
                sb.append(ex);
                Log.d(TAG,"連線異常 = " + sb.toString());
            }
        };
        webSocketClient.connect();
    }

    public void closeConnect(){
        connect = false;
        webSocketClient.close();
    }

    public Boolean states(){
        return connect;
    }
}
