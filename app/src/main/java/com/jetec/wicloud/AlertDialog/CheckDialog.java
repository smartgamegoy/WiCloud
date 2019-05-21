package com.jetec.wicloud.AlertDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jetec.wicloud.ListView.DataList;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.Screen;
import com.jetec.wicloud.Value;
import org.json.JSONArray;
import org.json.JSONException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CheckDialog {

    private static final String TAG = "CheckDialog";
    private Context context;
    private String title, message, sure, del, cancel;
    private int position;
    private DeviceList deviceList;
    private Vibrator vibrator;
    private LayoutInflater inflater;
    private ListView listView;
    private TextView textView;
    private Parcelable state;
    private DataList dataList;

    public CheckDialog(Context context, String title, String message, String sure, String del,
                       String cancel, int position, Vibrator vibrator, ListView listView,
                       TextView textView, DataList dataList) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.sure = sure;
        this.del = del;
        this.cancel = cancel;
        this.position = position;
        this.vibrator = vibrator;
        this.listView = listView;
        this.textView = textView;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
        deviceList = new DeviceList(context);
    }

    public void show_Dialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton(del, (arg0, arg1) -> {
            // TODO Auto-generated method stub
            vibrator.vibrate(100);
            AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
            dialog2.setTitle(context.getString(R.string.dialog_del));
            dialog2.setMessage(context.getString(R.string.del_message));
            String key = deviceList.getJSON().get(position).get("id");
            assert key != null;
            deviceList.delete(Integer.valueOf(key));
            if(deviceList.getCount() != 0) {
                Value.dataList = new DataList(context, deviceList.getJSON());
                relocate();
                listView.setAdapter(Value.dataList);
                if (state != null)
                    listView.onRestoreInstanceState(state);
            }
            else {
                textView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });
        dialog.setPositiveButton(sure, (arg0, arg1) -> {
            // TODO Auto-generated method stub
            vibrator.vibrate(100);
            try {
                String json = deviceList.getJSON().get(position).get("json");
                JSONArray jsonArray = new JSONArray(json);
                if(jsonArray.get(0).toString().matches("Value")) {
                    modify();
                }
                else if(jsonArray.get(0).toString().matches("Image")){
                    modify2();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        dialog.setNeutralButton(cancel, (arg0, arg1) -> {
            // TODO Auto-generated method stub
            vibrator.vibrate(100);
        });
        dialog.show();
    }

    private void modify() {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        try {
            String json = deviceList.getJSON().get(position).get("json");
            JSONArray jsonArray = new JSONArray(json);
            String name = jsonArray.get(3).toString();
            String model = jsonArray.get(2).toString();
            String key = deviceList.getJSON().get(position).get("id");
            Log.d(TAG, "key = " + key + "\t" + "json = " + json);
            ArrayList<String> newjson = new ArrayList<>();
            newjson.clear();

            @SuppressLint("InflateParams")
            View v = inflater.inflate(R.layout.modify, null);
            ConstraintLayout constraint = v.findViewById(R.id.constraint);
            TextView textView2 = v.findViewById(R.id.textView2);
            TextView textView3 = v.findViewById(R.id.textView3);
            EditText editText = v.findViewById(R.id.editText);
            Button cancel = v.findViewById(R.id.button);
            Button button = v.findViewById(R.id.button2);

            textView2.setText(model);
            textView3.setText(name);

            Screen screen = new Screen();
            DisplayMetrics dm = screen.getScreen(context);
            int x = (dm.widthPixels * 3) / 4;
            int y = (dm.heightPixels * 3) / 4;

            cancel.setOnClickListener(v1 -> {
                vibrator.vibrate(100);
                progressDialog.dismiss();
            });

            button.setOnClickListener(v12 -> {
                vibrator.vibrate(100);
                String str = editText.getText().toString().trim();
                if (!str.matches("")) {
                    try {
                        newjson.add(jsonArray.get(0).toString());
                        newjson.add(str);
                        newjson.add(jsonArray.get(2).toString());
                        newjson.add(jsonArray.get(3).toString());
                        JSONArray newArray = new JSONArray(newjson);
                        assert key != null;
                        deviceList.update(Integer.valueOf(key), newArray.toString());
                        dataList.setnewjson(deviceList.getJSON());
                        progressDialog.dismiss();
                        deviceList.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
            });

            progressDialog.setContentView(constraint, new LinearLayout.LayoutParams(x,
                    y));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void modify2(){
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        try {
            String json = deviceList.getJSON().get(position).get("json");
            JSONArray jsonArray = new JSONArray(json);
            String name = jsonArray.get(3).toString();
            String model = jsonArray.get(2).toString();
            String key = deviceList.getJSON().get(position).get("id");
            Log.d(TAG, "key = " + key + "\t" + "json = " + json);
            ArrayList<String> newjson = new ArrayList<>();
            newjson.clear();

            @SuppressLint("InflateParams")
            View v = inflater.inflate(R.layout.modify2, null);
            ConstraintLayout constraint = v.findViewById(R.id.constraint);
            TextView textView2 = v.findViewById(R.id.textView2);
            TextView textView3 = v.findViewById(R.id.textView3);
            EditText editText = v.findViewById(R.id.editText);
            EditText editText2 = v.findViewById(R.id.editText2);
            EditText editText3 = v.findViewById(R.id.editText3);
            Button cancel = v.findViewById(R.id.button);
            Button button = v.findViewById(R.id.button2);

            editText2.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                    InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText3.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                    InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            textView2.setText(model);
            textView3.setText(name);

            Screen screen = new Screen();
            DisplayMetrics dm = screen.getScreen(context);
            int x = (dm.widthPixels * 3) / 4;
            int y = (dm.heightPixels * 3) / 4;

            cancel.setOnClickListener(v1 -> {
                vibrator.vibrate(100);
                progressDialog.dismiss();
            });

            button.setOnClickListener(v12 -> {
                vibrator.vibrate(100);
                String str = editText.getText().toString().trim();
                if(str.matches("")){
                    str = name;
                }
                String str2 = editText2.getText().toString().trim();
                String str3 = editText3.getText().toString().trim();

                if (!str2.matches("") && !str3.matches("")) {
                    try {
                        if(str2.contains(".") || str3.contains(".")) {
                            @SuppressLint("DrawAllocation")
                            BigDecimal num = new BigDecimal(str2);
                            str2 = num.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                            @SuppressLint("DrawAllocation")
                            BigDecimal num2 = new BigDecimal(str3);
                            str3 = num2.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        }

                        newjson.add(jsonArray.get(0).toString());
                        newjson.add(str);
                        newjson.add(jsonArray.get(2).toString());
                        newjson.add(jsonArray.get(3).toString());
                        newjson.add(str2);
                        newjson.add(str3);

                        JSONArray newArray = new JSONArray(newjson);
                        assert key != null;
                        deviceList.update(Integer.valueOf(key), newArray.toString());
                        dataList.setnewjson(deviceList.getJSON());
                        progressDialog.dismiss();
                        deviceList.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
            });

            progressDialog.setContentView(constraint, new LinearLayout.LayoutParams(x,
                    y));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relocate() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    state = listView.onSaveInstanceState();
                    Log.d(TAG, "state = " + state);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
}