package com.jetec.wicloud.ListView;

import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jetec.wicloud.Listener.GetHardware;
import com.jetec.wicloud.Value;

public class HardwareList extends BaseAdapter {

    private String TAG = "HardwareList";
    private Vibrator vibrator;
    private GetHardware getHardware;

    public HardwareList(Vibrator vibrator, GetHardware getHardware){
        this.vibrator = vibrator;
        this.getHardware = getHardware;
    }

    @Override
    public int getCount() {
        return Value.model.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
