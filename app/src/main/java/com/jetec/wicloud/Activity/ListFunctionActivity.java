package com.jetec.wicloud.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.jetec.wicloud.ListView.AllStatusList;
import com.jetec.wicloud.Listener.GetSocket;
import com.jetec.wicloud.Listener.SocketListener;
import com.jetec.wicloud.LoadHandler;
import com.jetec.wicloud.Post_GET.HomeId;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.Value;
import com.jetec.wicloud.WebSocket.HardwareSocketHandler;
import com.jetec.wicloud.WebSocket.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class ListFunctionActivity extends AppCompatActivity implements SocketListener {

    private String TAG = "ViewActivity";
    private Vibrator vibrator;
    private JSONObject responseJson;
    private ArrayList<String> deviceListjson;
    private int position;
    private ListView listView;
    private DeviceList deviceList = new DeviceList(this);
    private HomeId homeId = new HomeId(this);
    private LoadHandler loadHandler = new LoadHandler(this);
    private HardwareSocketHandler hardwareSocketHandler = new HardwareSocketHandler();
    //private SocketHandler socketHandler = new SocketHandler();
    private Socket socket = new Socket();
    private GetSocket getSocket = new GetSocket();
    private AllStatusList allStatusList;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        Value.actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        get_intent();
    }

    private void get_intent() {
        try {
            Intent intent = getIntent();
            responseJson = new JSONObject(intent.getStringExtra("responseJson"));
            deviceListjson = intent.getStringArrayListExtra("deviceList");
            position = intent.getIntExtra("position", position);
            showlist();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showlist() throws JSONException {
        setContentView(R.layout.homeview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        title.setText(getString(R.string.bar_hardware));

        listView = findViewById(R.id.listview);
        TextView textView = findViewById(R.id.nodevice);
        textView.setVisibility(View.GONE);
        getSocket.setListener(this);
        mHandler = new Handler();
        getStatusHandler();
        loadHandler.setload();
        loadHandler.startload(getString(R.string.process));
        Log.d(TAG, "走到這");
        allStatusList = new AllStatusList(this, deviceListjson, position);
        socket.getWebSocket(hardwareSocketHandler.startHandler(listView, textView, deviceList, socket, getSocket));
    }

    private void getStatusHandler() {
        mHandler.postDelayed(() -> {
            mHandler.removeCallbacksAndMessages(null);
            homeId.regethomeid();
        }, 5000);
    }

    private void goback() {
        Intent intent = new Intent(this, HardWareActivity.class);
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
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setIcon(R.drawable.icon_wicloud)
                        .setMessage(R.string.app_message)
                        .setPositiveButton(R.string.app_message_b1, (dialog, which) -> finish())
                        .setNegativeButton(R.string.app_message_b2, (dialog, which) -> {
                            // TODO Auto-generated method stub
                        }).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_goback) {
            vibrator.vibrate(100);
            goback();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void getMessage() {
        if (listView.getAdapter() == null) {
            loadHandler.loadover();
            listView.setAdapter(allStatusList);
        } else {
            allStatusList.notifyDataSetChanged();
        }
    }
}
