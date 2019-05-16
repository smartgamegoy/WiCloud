package com.jetec.wicloud.Listener;

public class GetSocket {

    private SocketListener socketListener;

    public void setListener(SocketListener mSocketListener){
        socketListener = mSocketListener;
    }

    public void getNewmsg(){
        if(socketListener != null){
            socketListener.getMessage();
        }
    }
}
