package com.jetec.wicloud;

import android.content.Context;
import android.widget.Toast;

public class ShowMessage {

    private Context context;

    public ShowMessage(Context context){
        this.context = context;
    }

    public void show(String str){
        Toast.makeText(this.context, str, Toast.LENGTH_SHORT).show();
    }
}
