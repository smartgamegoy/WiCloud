package com.jetec.wicloud.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.jetec.wicloud.AlertDialog.CheckDialog;
import com.jetec.wicloud.GetTimeZone;
import com.jetec.wicloud.ListView.DataList;
import com.jetec.wicloud.Listener.GetSocket;
import com.jetec.wicloud.Listener.SocketListener;
import com.jetec.wicloud.LoadHandler;
import com.jetec.wicloud.Loading;
import com.jetec.wicloud.Post_GET.*;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.SQL.UserAccount;
import com.jetec.wicloud.Value;
import com.jetec.wicloud.WebSocket.*;
import com.jetec.wicloud.WebSocket.SocketHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

public class ViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SocketListener {

    private String TAG = "ViewActivity";
    private Vibrator vibrator;
    private UserAccount userAccount = new UserAccount(this);
    private Loading loading = new Loading(this);
    private LoadHandler loadHandler = new LoadHandler(this);
    private DeviceList deviceList = new DeviceList(this);
    private SocketHandler socketHandler = new SocketHandler();
    private UserId userId = new UserId(this);
    private Socket socket = new Socket();
    private JSONObject responseJson;
    private ListView listView;
    private DataList dataList;
    private GetSocket getSocket = new GetSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        Value.actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        getAccount();
    }

    private void getAccount() {

        try {
            Intent intent = getIntent();
            responseJson = new JSONObject(intent.getStringExtra("responseJson"));
            Log.d(TAG, "responseJson = " + responseJson.toString());

            loadHandler.setload();
            loadHandler.startload(getString(R.string.process));
            userId.getValue(responseJson, loadHandler);

            home();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void home() {
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_dashboard).setEnabled(false);
        getSocket.setListener(this);

        listView = findViewById(R.id.listview);
        listView.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.nodevice);

        if (deviceList.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);

            Log.d(TAG, "deviceList.getJSON() = " + deviceList.getJSON());
            dataList = new DataList(this, deviceList.getJSON());
            listView.setAdapter(dataList);
            socket.getWebSocket(socketHandler.startHandler(listView, textView, deviceList, socket, getSocket));

            listView.setOnItemLongClickListener((parent, view, position, id) -> {
                vibrator.vibrate(100);
                try {
                    String json = deviceList.getJSON().get(position).get("json");
                    JSONArray jsonArray = new JSONArray(json);
                    String name = jsonArray.get(1).toString();
                    CheckDialog checkDialog = new CheckDialog
                            (ViewActivity.this,
                                    getString(R.string.dialog_title) + ":" + name,
                                    getString(R.string.dialog_messgae),
                                    getString(R.string.dialog_sure),
                                    getString(R.string.dialog_del),
                                    getString(R.string.app_message_b2),
                                    position,
                                    vibrator,
                                    listView,
                                    textView,
                                    dataList);
                    checkDialog.show_Dialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            });

            listView.setOnItemClickListener(itemOnClick);
        }
    }

    private AdapterView.OnItemClickListener itemOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            socketHandler.stopHandler();
            if (socket.states())
                socket.closeConnect();
            getRecord(position);
        }
    };

    private void getRecord(int position) {
        try {
            GetTimeZone getTimeZone = new GetTimeZone();
            String day = getTimeZone.getTime_date();
            String nowdate = getTimeZone.getTime_Day(day);
            String olddate = getTimeZone.setTime(day);

            JSONArray jsonArray = new JSONArray(deviceList.getJSON().get(position).get("json"));
            String select = jsonArray.get(3).toString();

            ShowChart(nowdate, olddate, select);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ShowChart(String nowdate, String olddate, String select) {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("nowdate", nowdate);
        intent.putExtra("olddate", olddate);
        intent.putExtra("select", select);
        intent.putExtra("response", responseJson.toString());
        startActivity(intent);
        finish();
    }

    private void adddevice() {
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            vibrator.vibrate(100);
            socketHandler.stopHandler();
            if (socket.states())
                socket.closeConnect();
            adddevice();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            vibrator.vibrate(100);
        } else if (id == R.id.nav_hardware) {
            vibrator.vibrate(100);
            socketHandler.stopHandler();
            if (socket.states())
                socket.closeConnect();
            Intent intent = new Intent(ViewActivity.this, HardWareActivity.class);
            intent.putExtra("responseJson", responseJson.toString());
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logout) {
            vibrator.vibrate(100);
            if (userAccount.getCount() > 1) {
                userAccount.delete();
            }
            Intent intent = new Intent(ViewActivity.this, LoginActivity.class);
            startActivity(intent);
            socketHandler.stopHandler();
            if (socket.states())
                socket.closeConnect();
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
        loading.dismiss();
        deviceList.close();
        userAccount.close();
        socketHandler.stopHandler();
        if (socket.states())
            socket.closeConnect();
    }

    @Override
    public void getMessage() {
        //listView.setAdapter(dataList);
        dataList.notifyDataSetChanged();
    }
}