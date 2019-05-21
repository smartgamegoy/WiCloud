package com.jetec.wicloud.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jetec.wicloud.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class HardwareList extends BaseAdapter {

    private List<String> modelList, deviceList;
    private LayoutInflater inflater;
    private Context context;

    public HardwareList(Context context, List<String> modelList, List<String> deviceList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.modelList = modelList;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.homepage, null);
        }

        try {
            TextView t1 = view.findViewById(R.id.textView1);
            LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
            TextView t3 = view.findViewById(R.id.textView3);

            String device = modelList.get(position);
            JSONObject devicejson = new JSONObject(device);
            String status = devicejson.get("deviceClass").toString();
            status = status.toUpperCase();
            String id = devicejson.get("id").toString();
            String connect = devicejson.get("isConnected").toString();
            if (connect.contains("true")) {
                t1.setText(status + id);
            }
            else {
                t1.setText(status + id + " " + context.getString(R.string.connect));
            }

            String getcount = deviceList.get(position);
            JSONArray countjson = new JSONArray(getcount);
            int count = countjson.length();
            t3.setText(count + context.getString(R.string.count));

            LinearLayout linearLayout1 = new LinearLayout(context);
            linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < countjson.length(); i++) {
                TextView textView = new TextView(context);
                JSONObject getcountList = new JSONObject(countjson.get(i).toString());
                JSONObject getgate = new JSONObject(getcountList.get("value").toString());
                String getid = getgate.get("name").toString();
                textView.setText(getid);
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setSingleLine(true);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) context.getResources().getDimension(R.dimen.textviewheight)));
                linearLayout1.addView(textView);
            }
            linearLayout.removeAllViews();
            linearLayout.addView(linearLayout1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
