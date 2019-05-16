package com.jetec.wicloud.Activity;

import android.annotation.SuppressLint;
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
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.jetec.wicloud.AlertDialog.CheckDialog;
import com.jetec.wicloud.Canvas.ChoseImageView;
import com.jetec.wicloud.Canvas.ChoseValueView;
import com.jetec.wicloud.GetTimeZone;
import com.jetec.wicloud.ListView.DataList;
import com.jetec.wicloud.LoadHandler;
import com.jetec.wicloud.Loading;
import com.jetec.wicloud.Post_GET.*;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.DeviceList;
import com.jetec.wicloud.ShowMessage;
import com.jetec.wicloud.SpinnerList.*;
import com.jetec.wicloud.Value;
import com.jetec.wicloud.WebSocket.*;
import com.jetec.wicloud.WebSocket.SocketHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class ViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "ViewActivity";
    private Vibrator vibrator;
    private Loading loading = new Loading(this);
    private LoadHandler loadHandler = new LoadHandler(this);
    private DeviceList deviceList = new DeviceList(this);
    private SocketHandler socketHandler = new SocketHandler();
    private UserId userId = new UserId(this);
    private Socket socket = new Socket();
    private ShowMessage showMessage = new ShowMessage(this);
    private int page = 1;
    private ArrayList<String> value_sp1, value_sp2, value_getsp2;
    private String model, sensor;
    private JSONObject responseJson;
    private ListView listView;

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

        listView = findViewById(R.id.listview);
        listView.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.nodevice);

        if (deviceList.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);

            Value.dataList = new DataList(this, deviceList.getJSON());

            socket.getWebSocket(socketHandler.startHandler(listView, textView, deviceList, socket));
            listView.setAdapter(Value.dataList);

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
                                    name,
                                    position,
                                    vibrator,
                                    listView,
                                    textView);
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
            getRecode(position);
        }
    };

    private void getRecode(int position) {
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

    private void ShowChart(String nowdate, String olddate, String select){
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("nowdate",nowdate);
        intent.putExtra("olddate",olddate);
        intent.putExtra("select",select);
        intent.putExtra("response", responseJson.toString());
        startActivity(intent);
        finish();
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
            page = 5;
            new_value();
        });

        linearLayout2.setOnClickListener(v -> {
            vibrator.vibrate(100);
            page = 5;
            new_image();
        });
    }

    private void new_value() {

        setContentView(R.layout.new_value);

        value_sp1 = new ArrayList<>();
        value_sp2 = new ArrayList<>();
        value_getsp2 = new ArrayList<>();
        value_sp1.clear();
        value_sp2.clear();
        value_getsp2.clear();

        ValueSpinner1 valueSpinner1 = new ValueSpinner1(this);
        GetSensorValue getSensorValue = new GetSensorValue(ViewActivity.this);
        value_sp1.addAll(valueSpinner1.getmodel());
        value_sp2.add(getString(R.string.add_sensor));

        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinner2 = findViewById(R.id.spinner2);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewActivity.this, R.layout.spinner_style, value_sp2);
                    spinner2.setAdapter(adapter);
                    model = "";
                    sensor = "";
                }
                if (position > 0) {
                    model = value_sp1.get(position);
                    String modelId = value_sp1.get(position);
                    value_getsp2 = getSensorValue.getValue(modelId);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewActivity.this, R.layout.spinner_style, value_getsp2);
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
                    model = "";
                    sensor = "";
                    home();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void new_image() {
        setContentView(R.layout.new_image);

        value_sp1 = new ArrayList<>();
        value_sp2 = new ArrayList<>();
        value_getsp2 = new ArrayList<>();
        value_sp1.clear();
        value_sp2.clear();
        value_getsp2.clear();

        ValueSpinner1 valueSpinner1 = new ValueSpinner1(this);
        GetSensorValue getSensorValue = new GetSensorValue(ViewActivity.this);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewActivity.this, R.layout.spinner_style, value_sp2);
                    spinner2.setAdapter(adapter);
                    model = "";
                    sensor = "";
                }
                if (position > 0) {
                    model = value_sp1.get(position);
                    String modelId = value_sp1.get(position);
                    value_getsp2 = getSensorValue.getValue(modelId);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewActivity.this, R.layout.spinner_style, value_getsp2);
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
                        model = "";
                        sensor = "";
                        home();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                vibrator.vibrate(100);
                if (page == 1) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.app_name)
                            .setIcon(R.drawable.icon_wicloud)
                            .setMessage(R.string.app_message)
                            .setPositiveButton(R.string.app_message_b1, (dialog, which) -> finish())
                            .setNegativeButton(R.string.app_message_b2, (dialog, which) -> {
                                // TODO Auto-generated method stub
                            }).show();
                } else if (page == 4) {
                    page = 1;
                    home();
                } else if (page == 5) {
                    page = 4;
                    model = "";
                    sensor = "";
                    adddevice();
                }
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
            page = 4;
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
            page = 1;
            socketHandler.stopHandler();
            if (socket.states())
                socket.closeConnect();
            home();
            return true;
        } else if (id == R.id.nav_hardware) {
            vibrator.vibrate(100);
            page = 2;
//            socketHandler.stopHandler();
//            if (socket.states())
//                socket.closeConnect();
            Log.d(TAG, "硬體資訊");
        } else if (id == R.id.nav_alert) {
            vibrator.vibrate(100);
            page = 3;
//            socketHandler.stopHandler();
//            if (socket.states())
//                socket.closeConnect();
            Log.d(TAG, "警報");
        } else if (id == R.id.nav_logout) {
            vibrator.vibrate(100);
            Intent intent = new Intent(ViewActivity.this, LoginActivity.class);
            startActivity(intent);
            socketHandler.stopHandler();
            if (socket.states())
                socket.closeConnect();
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
        loading.dismiss();
        deviceList.close();
        socketHandler.stopHandler();
        if (socket.states())
            socket.closeConnect();
    }
}