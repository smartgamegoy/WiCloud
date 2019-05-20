package com.jetec.wicloud.Listener;

import org.json.JSONArray;

public class GetHardware {

    private HardwareListener hardwareListener;

    public void setListener(HardwareListener mHardwareListener){
        hardwareListener = mHardwareListener;
    }

    public void startshow(JSONArray homevalue){
        if(hardwareListener != null){
            hardwareListener.showlist(homevalue);
        }
    }
}
