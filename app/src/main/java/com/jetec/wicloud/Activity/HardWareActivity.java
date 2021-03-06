package com.jetec.wicloud.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.jetec.wicloud.ListView.HardwareList;
import com.jetec.wicloud.Listener.GetHardware;
import com.jetec.wicloud.Listener.HardwareListener;
import com.jetec.wicloud.LoadHandler;
import com.jetec.wicloud.Post_GET.HomeId;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.UserAccount;
import com.jetec.wicloud.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HardWareActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HardwareListener {

    private String TAG = "ViewActivity";
    private Vibrator vibrator;
    private JSONObject responseJson;
    private LoadHandler loadHandler = new LoadHandler(this);
    private HomeId homeId = new HomeId(this);
    private UserAccount userAccount = new UserAccount(this);
    private GetHardware getHardware = new GetHardware();
    private ListView listView;
    private TextView textView;
    private ArrayList<String> modelList, deviceList;
    private Handler mHandler;
    private HardwareList hardwareList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        Value.actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        hardware();
    }

    private void hardware() {
        try {
            Intent intent = getIntent();
            responseJson = new JSONObject(intent.getStringExtra("responseJson"));
            Log.d(TAG, "responseJson = " + responseJson.toString());

            home();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void home() {
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        title.setText(getString(R.string.bar_hardware));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_hardware).setEnabled(false);

        listView = findViewById(R.id.listview);
        listView.setVisibility(View.GONE);
        textView = findViewById(R.id.nodevice);

        loadHandler.setload();
        loadHandler.startload(getString(R.string.process));
        mHandler = new Handler();
        getHardware.setListener(this);
        homeId.gethomeList(loadHandler, getHardware);
    }

    private void listfuction(int position) {
        Intent intent = new Intent(this, ListFunctionActivity.class);
        intent.putStringArrayListExtra("deviceList", deviceList);
        intent.putExtra("position", position);
        intent.putExtra("responseJson", responseJson.toString());
        startActivity(intent);
        finish();
    }

    private AdapterView.OnItemClickListener itemOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                String device = modelList.get(position);
                JSONObject devicejson = new JSONObject(device);
                String connect = devicejson.get("isConnected").toString();
                if (connect.contains("true")) {
                    vibrator.vibrate(100);
                    listfuction(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void goback() {
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            vibrator.vibrate(100);
            goback();
        } else if (id == R.id.nav_hardware) {
            vibrator.vibrate(100);
        } else if (id == R.id.nav_logout) {
            vibrator.vibrate(100);
            if (userAccount.getCount() > 1) {
                userAccount.delete();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.url_phonecall) {
            vibrator.vibrate(100);
            Uri uri = Uri.parse("http://www.jetec.com.tw/wicloud/support.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showlist(List<String> modelList, List<String> deviceList) {
        listView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);

        this.modelList = new ArrayList<>();
        this.deviceList = new ArrayList<>();
        this.modelList.clear();
        this.deviceList.clear();
        this.modelList.addAll(modelList);
        this.deviceList.addAll(deviceList);

        if (listView.getAdapter() == null) {
            hardwareList = new HardwareList(this, modelList, deviceList);
            listView.setAdapter(hardwareList);
            listView.setOnItemClickListener(itemOnClick);
        } else {
            hardwareList.resetList(modelList, deviceList);
            hardwareList.notifyDataSetChanged();
        }
        getHardware.resethome();
    }

    @Override
    public void regethomeid() {
        mHandler.postDelayed(() -> {
            mHandler.removeCallbacksAndMessages(null);
            homeId.gethomeList(loadHandler, getHardware);
        }, 5000);
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
        userAccount.close();
    }
}
