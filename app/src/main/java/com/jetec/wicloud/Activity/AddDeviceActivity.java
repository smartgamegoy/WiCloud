package com.jetec.wicloud.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.jetec.wicloud.Canvas.ChoseImageView;
import com.jetec.wicloud.Canvas.ChoseValueView;
import com.jetec.wicloud.R;
import org.json.JSONException;
import org.json.JSONObject;

public class AddDeviceActivity extends AppCompatActivity {

    private String TAG = "AddDeviceActivity";
    private Vibrator vibrator;
    private JSONObject responseJson;

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

        adddevice();
    }

    private void adddevice() {
        setContentView(R.layout.choselist);

        ChoseValueView choseValueView = new ChoseValueView(this);
        ChoseImageView choseImageView = new ChoseImageView(this);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        LinearLayout linearLayout2 = findViewById(R.id.linearLayout2);

        linearLayout.addView(choseValueView);
        linearLayout2.addView(choseImageView);

        linearLayout.setOnClickListener(v -> {
            vibrator.vibrate(100);
            new_value();
        });

        linearLayout2.setOnClickListener(v -> {
            vibrator.vibrate(100);
            new_image();
        });
    }

    private void new_value(){
        Intent intent = new Intent(this, NewValueActivity.class);
        intent.putExtra("responseJson", responseJson.toString());
        startActivity(intent);
        finish();
    }

    private void new_image(){
        Intent intent = new Intent(this, NewImageAvtivity.class);
        intent.putExtra("responseJson", responseJson.toString());
        startActivity(intent);
        finish();
    }

    private void goback(){
        Intent intent = new Intent(this, ViewActivity.class);
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
