package com.jetec.wicloud;

import android.content.Context;

public class LoadHandler {

    private Context context;
    private Loading loading;

    public LoadHandler(Context context){
        this.context = context;
    }

    public void loadover(){
        loading.dismiss();
    }

    public void setload(){
        loading = new Loading(context);
    }

    public void startload(String str){
        loading.show(str);
    }
}
