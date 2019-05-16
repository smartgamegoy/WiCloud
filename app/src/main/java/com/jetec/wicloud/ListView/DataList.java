package com.jetec.wicloud.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.jetec.wicloud.Canvas.HandlerValue;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Screen;
import com.jetec.wicloud.Value;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataList extends BaseAdapter {

    private ArrayList<HashMap<String, String>> getJSON;
    private Context context;
    private LayoutInflater inflater;

    public DataList(Context context, ArrayList<HashMap<String, String>> getJSON){
        this.context = context;
        this.getJSON = getJSON;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return getJSON.size();
    }

    @Override
    public Object getItem(int position) {
        return getJSON.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "WrongConstant"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.socketlist, null);
        }

        Screen screen = new Screen();
        DisplayMetrics dm = screen.getScreen(context);
        int height = dm.heightPixels - Value.actionBarHeight;

        HandlerValue handlerValue = new HandlerValue(context);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
        linearLayout2.setVisibility(View.GONE);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, height / 2));

        String json = getJSON.get(position).get("json");

        try {
            JSONArray jsonArray = new JSONArray(json);
            handlerValue.set_context(context);
            handlerValue.setValue(jsonArray, Value.socketvalue);

            if(handlerValue.getParent() != null) {
                ((ViewGroup) handlerValue.getParent()).removeView(handlerValue);
            }

            linearLayout.addView(handlerValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}