package com.jetec.wicloud.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jetec.wicloud.Canvas.HandlerValue;
import com.jetec.wicloud.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllStatusList extends BaseAdapter {

    private String TAG = "AllStatusList";
    private List<String> deviceList, countlist;
    private Context context;
    private LayoutInflater inflater;
    private List<View> viewList;
    private int position;
    private JSONArray countjson;

    @SuppressLint("InflateParams")
    public AllStatusList(Context context, List<String> deviceList, int position) throws JSONException {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.deviceList = deviceList;
        this.position = position;
        viewList = new ArrayList<>();
        countlist = new ArrayList<>();
        viewList.clear();
        countlist.clear();
        String getcount = deviceList.get(position);
        countjson = new JSONArray(getcount);
        Log.d(TAG, "countjson = " + countjson);
        for (int i = 0; i < countjson.length(); i++) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.liststatus, null);
            viewList.add(view);
            countlist.add(countjson.get(i).toString());
        }
    }

    @Override
    public int getCount() {
        return countlist.size();
    }

    @Override
    public Object getItem(int position) {
        return countlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        view = viewList.get(position);
        try {
            TextView textView1 = view.findViewById(R.id.textView1);
            TextView textView2 = view.findViewById(R.id.textView2);
            ListView listView1 = view.findViewById(R.id.listview1);
            ListView listView2 = view.findViewById(R.id.listview2);

            String getlist = countlist.get(position);
            JSONObject getcount = new JSONObject(getlist);
            JSONObject getvalue = new JSONObject(getcount.get("value").toString());
            textView1.setText(context.getString(R.string.setinformation) + getvalue.get("name"));
            textView2.setText(context.getString(R.string.nowdata) + getvalue.get("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;

    }
}
