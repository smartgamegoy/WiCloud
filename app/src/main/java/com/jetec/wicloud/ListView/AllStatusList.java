package com.jetec.wicloud.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jetec.wicloud.R;
import com.jetec.wicloud.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AllStatusList extends BaseAdapter {

    private String TAG = "AllStatusList";
    private List<String> countlist;
    private Context context;
    private List<View> viewList;

    @SuppressLint("InflateParams")
    public AllStatusList(Context context, List<String> deviceList, int getcount) throws JSONException {
        this.context = context;
        viewList = new ArrayList<>();
        countlist = new ArrayList<>();
        viewList.clear();
        countlist.clear();
        String getCount = deviceList.get(getcount);
        JSONArray countjson = new JSONArray(getCount);
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

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        view = viewList.get(position);
        try {
            TextView textView1 = view.findViewById(R.id.textView1);
            TextView textView2 = view.findViewById(R.id.textView2);
            TextView textView3 = view.findViewById(R.id.textView3);
            TextView textView4 = view.findViewById(R.id.textView4);
            TextView textView5 = view.findViewById(R.id.textView5);
            TextView textView6 = view.findViewById(R.id.textView6);
            TextView textView7 = view.findViewById(R.id.textView7);
            TextView textView8 = view.findViewById(R.id.textView8);
            TextView textView9 = view.findViewById(R.id.textView9);
            TextView textView10 = view.findViewById(R.id.textView10);
            LinearLayout linearLayout = view.findViewById(R.id.linearLayout3);

            String getlist = countlist.get(position);
            JSONObject getcount = new JSONObject(getlist);
            JSONObject getvalue = new JSONObject(getcount.get("value").toString());
            JSONArray getsensors = new JSONArray(getvalue.get("sensors").toString());
            String gatewayId = getvalue.getString("gatewayId");
            String interval = getvalue.getString("interval");
            textView1.setText(context.getString(R.string.setinformation) + getvalue.get("name"));
            textView2.setText(context.getString(R.string.nowdata) + getvalue.get("name"));
            textView3.setText(context.getString(R.string.connectstatus));
            textView5.setText(context.getString(R.string.sensors));
            textView7.setText(context.getString(R.string.interval));
            textView9.setText(context.getString(R.string.intervals));
            for (int i = 0; i < Value.model.length(); i++) {
                JSONObject modelson = new JSONObject(Value.model.get(i).toString());
                if (gatewayId.matches(modelson.get("id").toString())) {
                    if (modelson.get("isConnected").toString().matches("true")) {
                        textView4.setText(context.getString(R.string.checkconnect));
                    } else {
                        textView4.setText(context.getString(R.string.checkconnects));
                    }
                }
            }
            Log.d(TAG, "getsensors.length() = " + getsensors.length());
            textView6.setText(String.valueOf(getsensors.length()));
            textView8.setText(interval + context.getString(R.string.seconds));
            textView10.setText(24 + context.getString(R.string.hour));

            linearLayout.removeAllViews();
            if (gatewayId.matches(Value.socketvalue.get("originDeviceId").toString())) {
                JSONObject eventData = new JSONObject(Value.socketvalue.get("eventData").toString());
                JSONArray timeSeriesData = new JSONArray(eventData.get("timeSeriesData").toString());
                for (int i = 0; i < timeSeriesData.length(); i++) {
                    JSONObject obj = new JSONObject(timeSeriesData.get(i).toString());
                    if(!obj.get("seriesId").toString().contains("wind_direction_x") &&
                            !obj.get("seriesId").toString().contains("wind_direction_y")) {
                        View view2;
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        view2 = layoutInflater.inflate(R.layout.statuslist, null);
                        TextView t1 = view2.findViewById(R.id.textView1);
                        TextView t2 = view2.findViewById(R.id.textView2);
                        String unit = getsensorvalue(obj.get("seriesId").toString());
                        t1.setText(unit);
                        double value = Double.valueOf(obj.get("value").toString());
                        BigDecimal num = new BigDecimal(value);
                        value = num.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        t2.setText(String.valueOf(value));
                        linearLayout.addView(view2);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;

    }

    private String getsensorvalue(String sensor) {
        if (sensor.contains("temperature")) {
            sensor = context.getString(R.string.sensor_temperature);
        } else if (sensor.contains("humidity")) {
            sensor = context.getString(R.string.sensor_humidity);
        } else if (sensor.contains("wind_speed")) {
            sensor = context.getString(R.string.sensor_wind_speed);
        } else if (sensor.contains("wind_direction")) {
            sensor = context.getString(R.string.sensor_wind_direction);
        } else if (sensor.contains("precipitation")) {
            sensor = context.getString(R.string.sensor_precipitation);
        } else if (sensor.contains("value")) {
            sensor = context.getString(R.string.sensor_value);
        } else {
            sensor = "unknown";
        }

        return sensor;
    }
}
