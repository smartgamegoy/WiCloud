package com.jetec.wicloud;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Screen {

    public DisplayMetrics getScreen(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
    /*
     *         screen = new Screen();
     *         DisplayMetrics dm = screen.getScreen(this);
     *         double all_Width = dm.widthPixels;
     *         double all_Height = dm.heightPixels;
     *         Log.d(TAG, "Screen : height : " + dm.heightPixels + "dp" + " width : " + dm.widthPixels + "dp");
     */
}
