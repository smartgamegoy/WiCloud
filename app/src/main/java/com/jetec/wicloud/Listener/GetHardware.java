package com.jetec.wicloud.Listener;

import java.util.List;

public class GetHardware {

    private HardwareListener hardwareListener;

    public void setListener(HardwareListener mHardwareListener){
        hardwareListener = mHardwareListener;
    }

    public void startshow(List<String> modelList, List<String> deviceList){
        if(hardwareListener != null){
            hardwareListener.showlist(modelList, deviceList);
        }
    }

    public void resethome(){
        if(hardwareListener != null){
            hardwareListener.regethomeid();
        }
    }
}
