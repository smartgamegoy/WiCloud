package com.jetec.wicloud.Listener;

public class GetSpinner {

    private SpinnerListener spinnerListener;

    public void setListener(SpinnerListener mSpinnerListener){
        spinnerListener = mSpinnerListener;
    }

    public void isGetspinner(){
        if(spinnerListener != null){
            spinnerListener.setspinner();
        }
    }
}
