package com.jetec.wicloud.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jetec.wicloud.R;

import java.util.ArrayList;

public class ViewList extends BaseAdapter {

    private Context context;
    private ArrayList<String> timeList, valueList;
    private LayoutInflater inflater;

    public ViewList(Context context, ArrayList<String> timeList, ArrayList<String> valueList){
        this.context = context;
        this.timeList = timeList;
        this.valueList = valueList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return valueList.size();
    }

    @Override
    public Object getItem(int position){
        return valueList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.viewlist, null);
        }

        TextView textView = view.findViewById(R.id.textview);
        TextView textView2 = view.findViewById(R.id.textview2);
        String time = timeList.get(position);
        String value = valueList.get(position);

        textView.setText(time);
        textView2.setText(value);
        return view;
    }
}
