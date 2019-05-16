package com.jetec.wicloud.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jetec.wicloud.Listener.GetSpinner;
import com.jetec.wicloud.Listener.SpinnerListener;
import com.jetec.wicloud.Post_GET.GetSensorValue;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.ShowMessage;
import com.jetec.wicloud.SpinnerList.ValueSpinner1;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NewValueActivity extends AppCompatActivity implements SpinnerListener {

    private String TAG = "NewValueActivity";
    private Vibrator vibrator;
    private Spinner spinner2;
    private JSONObject responseJson;
    private List<String> value_getsp2;
    private String model, sensor;
    private ShowMessage showMessage = new ShowMessage(this);
    private DeviceList deviceList = new DeviceList(this);
    private GetSpinner getSpinner = new GetSpinner();

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

        new_value();
    }

    private void new_value() {

        setContentView(R.layout.new_value);

        List<String> value_sp1 = new ArrayList<>();
        List<String> value_sp2 = new ArrayList<>();
        value_getsp2 = new ArrayList<>();
        value_sp1.clear();
        value_sp2.clear();
        value_getsp2.clear();

        ValueSpinner1 valueSpinner1 = new ValueSpinner1(this);
        GetSensorValue getSensorValue = new GetSensorValue(this);
        getSpinner.setListener(this);
        value_sp1.addAll(valueSpinner1.getmodel());
        value_sp2.add(getString(R.string.add_sensor));

        Spinner spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_style, value_sp1);
        spinner.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_style, value_sp2);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NewValueActivity.this, R.layout.spinner_style, value_sp2);
                    spinner2.setAdapter(adapter);
                    spinner2.setEnabled(false);
                    model = "";
                    sensor = "";
                }
                if (position > 0) {
                    model = value_sp1.get(position);
                    String modelId = value_sp1.get(position);
                    value_getsp2 = getSensorValue.getValue(modelId, getSpinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NewValueActivity.this, R.layout.spinner_style, value_getsp2);
                    spinner2.setAdapter(adapter);
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
                    ArrayList<String> sql = new ArrayList<>();
                    sql.clear();

                    sql.add("Value");
                    sql.add(edit);
                    sql.add(model);
                    sql.add(sensor);

                    JSONArray sql_json = new JSONArray(sql.toString());
                    deviceList.insert(sql_json.toString());
                    showList();
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

    @Override
    public void setspinner() {
        spinner2.setEnabled(true);
    }
}
