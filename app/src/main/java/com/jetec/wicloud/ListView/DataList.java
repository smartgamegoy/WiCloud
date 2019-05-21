package com.jetec.wicloud.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jetec.wicloud.Canvas.HandlerValue;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Screen;
import com.jetec.wicloud.Value;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataList extends BaseAdapter {

    private String TAG = "DataList";
    private ArrayList<HashMap<String, String>> getJSON;
    private Context context;
    private List<HandlerValue> saveView;
    private List<ViewGroup> viewList;

    @SuppressLint("InflateParams")
    public DataList(Context context, ArrayList<HashMap<String, String>> getJSON) {
        saveView = new ArrayList<>();
        viewList = new ArrayList<>();
        saveView.clear();
        viewList.clear();
        for (int i = 0; i < getJSON.size(); i++) {
            ViewGroup view;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = (ViewGroup) inflater.inflate(R.layout.socketlist, null);
            HandlerValue handlerValue = new HandlerValue(context);
            saveView.add(handlerValue);
            viewList.add(view);
        }
        this.context = context;
        this.getJSON = getJSON;
    }

    @Override
    public int getCount() {
        Log.d(TAG, "saveView.size() = " + saveView.size());
        return saveView.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG, "saveView.get(position) = " + saveView.get(position));
        return saveView.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "position = " + position);
        return position;
    }

    @SuppressLint({"InflateParams", "WrongConstant", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        view = viewList.get(position);

        Screen screen = new Screen();
        DisplayMetrics dm = screen.getScreen(context);
        int height = dm.heightPixels - Value.actionBarHeight;

        ImageView imageView = view.findViewById(R.id.imageView);
        LinearLayout linearLayoutview = view.findViewById(R.id.linearLayoutview);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
        linearLayout2.setVisibility(View.GONE);
        linearLayoutview.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ((height - (Value.actionBarHeight / 2)) / 2)));

        String json = getJSON.get(position).get("json");
        Log.d(TAG, "json = " + json);
        HandlerValue handlerValue = saveView.get(position);
        try {
            JSONArray jsonArray = new JSONArray(json);
            handlerValue.set_context(context);
            handlerValue.setValue(jsonArray, Value.socketvalue, imageView);

            if (handlerValue.getParent() != null) {
                ((ViewGroup) handlerValue.getParent()).removeView(handlerValue);
            }
            linearLayout.addView(handlerValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}