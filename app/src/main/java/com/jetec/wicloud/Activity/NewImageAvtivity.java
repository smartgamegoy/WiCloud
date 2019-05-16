package com.jetec.wicloud.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.jetec.wicloud.Post_GET.GetSensorValue;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.ShowMessage;
import com.jetec.wicloud.SpinnerList.ValueSpinner1;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewImageAvtivity extends AppCompatActivity {

    private String TAG = "NewValueActivity";
    private Vibrator vibrator;
    private JSONObject responseJson;
    private List<String> value_getsp2;
    private String model, sensor;
    private ShowMessage showMessage = new ShowMessage(this);
    private DeviceList deviceList = new DeviceList(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        try {
            Intent intent = getIntent();
            responseJson = new JSONObject(intent.getStringExtra("responseJson"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new_image();
    }

    private void new_image() {
        setContentView(R.layout.new_image);

        List<String> value_sp1 = new ArrayList<>();
        List<String> value_sp2 = new ArrayList<>();
        value_getsp2 = new ArrayList<>();
        value_sp1.clear();
        value_sp2.clear();
        value_getsp2.clear();

        ValueSpinner1 valueSpinner1 = new ValueSpinner1(this);
        GetSensorValue getSensorValue = new GetSensorValue(this);
        value_sp1.addAll(valueSpinner1.getmodel());
        value_sp2.add(getString(R.string.add_sensor));

        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinner2 = findViewById(R.id.spinner2);
        EditText editText = findViewById(R.id.editText);    //name
        EditText editText2 = findViewById(R.id.editText2);  //max
        EditText editText3 = findViewById(R.id.editText3);  //min
        Button button = findViewById(R.id.button);

        editText2.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText3.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED |
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_style, value_sp1);
        spinner.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_style, value_sp2);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NewImageAvtivity.this, R.layout.spinner_style, value_sp2);
                    spinner2.setAdapter(adapter);
                    spinner2.setEnabled(false);
                    model = "";
                    sensor = "";
                }
                if (position > 0) {
                    model = value_sp1.get(position);
                    String modelId = value_sp1.get(position);
                    value_getsp2 = getSensorValue.getValue(modelId);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NewImageAvtivity.this, R.layout.spinner_style, value_getsp2);
                    spinner2.setAdapter(adapter);
                    spinner2.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sensor = "";
                }
                if (position > 0) {
                    sensor = value_getsp2.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        button.setOnClickListener(v -> {
            vibrator.vibrate(100);
            String edit;
            if (editText.getText().toString().trim().matches("")) {
                edit = sensor;
            } else {
                edit = editText.getText().toString().trim();
            }

            if (edit.matches("") || model.matches("") || sensor.matches("")) {
                showMessage.show(getString(R.string.value_button));
            } else {
                try {
                    String max = editText2.getText().toString().trim();
                    String min = editText3.getText().toString().trim();

                    @SuppressLint("DrawAllocation")
                    BigDecimal num = new BigDecimal(max);
                    max = num.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    @SuppressLint("DrawAllocation")
                    BigDecimal num2 = new BigDecimal(min);
                    min = num2.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

                    if (max.matches("") || min.matches("")) {
                        showMessage.show(getString(R.string.num_button));
                    } else {
                        ArrayList<String> sql = new ArrayList<>();
                        sql.clear();

                        sql.add("Image");
                        sql.add(edit);
                        sql.add(model);
                        sql.add(sensor);
                        sql.add(max);
                        sql.add(min);

                        JSONArray sql_json = new JSONArray(sql.toString());
                        deviceList.insert(sql_json.toString());
                        showList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showList(){
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("responseJson", responseJson.toString());
        startActivity(intent);
        finish();
    }

    private void goback(){
        Intent intent = new Intent(this, AddDeviceActivity.class);
        intent.putExtra("responseJson", responseJson.toString());
        startActivity(intent);
        finish();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                vibrator.vibrate(100);
                goback();
            }
            break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        deviceList.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
}
