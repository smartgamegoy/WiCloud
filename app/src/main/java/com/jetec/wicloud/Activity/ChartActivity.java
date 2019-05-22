package com.jetec.wicloud.Activity;

import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.jetec.wicloud.Canvas.ShowChart;
import com.jetec.wicloud.Direction;
import com.jetec.wicloud.GetTimeZone;
import com.jetec.wicloud.ListView.ViewList;
import com.jetec.wicloud.Post_GET.DataLog;
import com.jetec.wicloud.Post_GET.GetObjectRequest;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Screen;
import com.jetec.wicloud.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ChartActivity";
    private Vibrator vibrator;
    private JSONObject responseJson;
    private ArrayList<String> timeList, valueList;
    private JSONObject X_direction = null, Y_direction = null;
    private int GMT;
    private GetTimeZone getTimeZone = new GetTimeZone();
    private ListView listView;
    private LineChart lineChart;
    private String select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        get_intent();

        GMT = getTimeZone.getTimeZone();
    }

    private void get_intent() {
        try {
            Intent intent = getIntent();
            String nowdate = intent.getStringExtra("nowdate");
            String olddate = intent.getStringExtra("olddate");
            select = intent.getStringExtra("select");
            responseJson = new JSONObject(intent.getStringExtra("response"));

            Log.d(TAG, "nowdate = " + nowdate);
            Log.d(TAG, "olddate = " + olddate);
            Log.d(TAG, "select = " + select);
            Log.d(TAG, "responseJson = " + responseJson);

            DataLog dataLog = new DataLog(this);
            String seriesId = dataLog.getdatalog(select);

            getChart(olddate, nowdate, seriesId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getChart(String olddate, String nowdate, String seriesId) {
        setContentView(R.layout.chartdialog);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = findViewById(R.id.listview);
        lineChart = findViewById(R.id.chart);

        String url;
        if (seriesId.contains("precipitation")) {
            url = "https://api.tinkermode.com/homes/744/smartModules/tsdb/timeSeries/" + seriesId +
                    "/data?begin=" + olddate + "&end=" + nowdate + "&aggregation=sum";
        } else {
            url = "https://api.tinkermode.com/homes/744/smartModules/tsdb/timeSeries/" + seriesId +
                    "/data?begin=" + olddate + "&end=" + nowdate + "&aggregation=avg";
        }

        Log.d(TAG, "url = " + url);
        if (url.contains("wind_direction")) {
            caculator(url);
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            GetObjectRequest getObjectRequest = new GetObjectRequest(url,
                    response -> {
                        if (response != null) {
                            analysisjson(response);
                        }
                    },
                    error -> Log.d(TAG, "VolleyError = " + error.networkResponse)) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "ModeCloud " + Value.token);
                    return headers;
                }
            };
            getObjectRequest.setTag("DataLog");
            requestQueue.add(getObjectRequest);
        }
    }

    private void setPageView() {
        Log.d(TAG, "timeList = " + timeList);
        Log.d(TAG, "valueList = " + valueList);

        Collections.reverse(timeList);
        Collections.reverse(valueList);

        ViewList viewList = new ViewList(this, timeList, valueList);
        listView.setAdapter(viewList);

        Screen screen = new Screen();

        ShowChart showchart = new ShowChart(this, lineChart, screen.getScreen(this));
        showchart.startdraw(select, timeList, valueList);
    }

    private void caculator(String url) {
        String direction_x = url.replace("direction", "direction_x");
        String direction_y = url.replace("direction", "direction_y");

        RequestQueue requestQueueX = Volley.newRequestQueue(getApplicationContext());

        GetObjectRequest getdirection_xRequest = new GetObjectRequest(direction_x,
                response -> {
                    if (response != null) {
                        X_direction = response;

                        RequestQueue requestQueueY = Volley.newRequestQueue(getApplicationContext());

                        GetObjectRequest getdirection_yRequest = new GetObjectRequest(direction_y,
                                response_y -> {
                                    if (response_y != null) {
                                        Y_direction = response_y;
                                        analysisjson_XY();
                                    }
                                },
                                error -> Log.d(TAG, "VolleyError = " + error.networkResponse)) {
                            @Override
                            public Map<String, String> getHeaders() {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "ModeCloud " + Value.token);
                                return headers;
                            }
                        };
                        getdirection_yRequest.setTag("direction_y");
                        requestQueueY.add(getdirection_yRequest);
                    }
                },
                error -> Log.d(TAG, "VolleyError = " + error.networkResponse)) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "ModeCloud " + Value.token);
                return headers;
            }
        };
        getdirection_xRequest.setTag("direction_x");
        requestQueueX.add(getdirection_xRequest);

    }

    private void analysisjson_XY() {

        timeList = new ArrayList<>();
        valueList = new ArrayList<>();
        ArrayList<String> XList = new ArrayList<>();
        ArrayList<String> YList = new ArrayList<>();
        timeList.clear();
        valueList.clear();
        XList.clear();
        YList.clear();

        try {
            JSONArray xArray = new JSONArray(X_direction.get("data").toString());
            for (int i = 0; i < xArray.length(); i++) {
                JSONArray str = new JSONArray(xArray.get(i).toString());
                String time = str.get(0).toString();
                String value = str.get(1).toString();

                time = time.replace("T", " ");
                time = time.replace("Z", "");
                time = time.trim();

                timeList.add(getTimeZone.resetTime(time, GMT));

                double v = Double.valueOf(value);
                @SuppressLint("DrawAllocation")
                BigDecimal num = new BigDecimal(v);
                v = num.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                value = String.valueOf(v);

                XList.add(value);
            }

            JSONArray yArray = new JSONArray(Y_direction.get("data").toString());
            for (int i = 0; i < yArray.length(); i++) {
                JSONArray str = new JSONArray(yArray.get(i).toString());
                String value = str.get(1).toString();

                double v = Double.valueOf(value);
                @SuppressLint("DrawAllocation")
                BigDecimal num = new BigDecimal(v);
                v = num.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                value = String.valueOf(v);

                YList.add(value);
            }

            Direction direction = new Direction();
            valueList = direction.caculator(XList, YList);
            setPageView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void analysisjson(JSONObject response) {

        timeList = new ArrayList<>();
        valueList = new ArrayList<>();
        timeList.clear();
        valueList.clear();

        try {
            JSONArray jsonArray = new JSONArray(response.get("data").toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray str = new JSONArray(jsonArray.get(i).toString());
                String time = str.get(0).toString();
                String value = str.get(1).toString();

                time = time.replace("T", " ");
                time = time.replace("Z", "");
                time = time.trim();

                timeList.add(getTimeZone.resetTime(time, GMT));

                double v = Double.valueOf(value);
                @SuppressLint("DrawAllocation")
                BigDecimal num = new BigDecimal(v);
                v = num.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                value = String.valueOf(v);

                valueList.add(value);
            }
            setPageView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
            Intent intent = new Intent(this, ViewActivity.class);
            intent.putExtra("responseJson", responseJson.toString());
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.nav_hardware) {
            vibrator.vibrate(100);
//            socketHandler.stopHandler();
//            if (socket.states())
//                socket.closeConnect();
            Log.d(TAG, "硬體資訊");
            return true;
        } /*else if (id == R.id.nav_alert) {
            vibrator.vibrate(100);
//            socketHandler.stopHandler();
//            if (socket.states())
//                socket.closeConnect();
            Log.d(TAG, "警報");
            return true;
        }*/ else if (id == R.id.nav_logout) {
            vibrator.vibrate(100);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.url_phonecall) {
            vibrator.vibrate(100);
            Uri uri = Uri.parse("http://www.jetec.com.tw/wicloud/support.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
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
    }
}