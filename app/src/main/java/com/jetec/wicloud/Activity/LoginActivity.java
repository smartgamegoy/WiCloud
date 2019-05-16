package com.jetec.wicloud.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jetec.wicloud.Listener.ConnectListener;
import com.jetec.wicloud.Listener.GetConnect;
import com.jetec.wicloud.Loading;
import com.jetec.wicloud.Post_GET.Connected;
import com.jetec.wicloud.R;
import com.jetec.wicloud.SQL.UserAccount;
import com.jetec.wicloud.ShowMessage;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ConnectListener {

    private String TAG = "LoginActivity";
    private Uri uri = Uri.parse("http://www.jetec.com.tw/");
    private Vibrator vibrator;
    private UserAccount userAccount = new UserAccount(this);
    private ShowMessage showMessage = new ShowMessage(this);
    private Connected connected = new Connected(this);
    private Loading loading = new Loading(this);
    private GetConnect getConnect = new GetConnect();
    private String account, password;
    private EditText editText, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        //隱藏標題欄
        supportRequestWindowFeature( Window.FEATURE_NO_TITLE);
        //隱藏狀態欄
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        Login();
    }

    private void Login(){
        setContentView(R.layout.login);

        TextView textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        Button button = findViewById(R.id.button);

        getConnect.setListener(this);

        /*Check user's data*/
        if (userAccount.getCount() != 0) {
            Cursor check = userAccount.select();
            check.moveToPosition(0);
            account = check.getString(check.getColumnIndex("account"));
            password = check.getString(check.getColumnIndex("password"));
            editText.setText(account);
            editText2.setText(password);
        }

        editText.setOnClickListener(v -> vibrator.vibrate(100));

        editText2.setOnClickListener(v -> vibrator.vibrate(100));

        textView.setOnClickListener(v -> {
            vibrator.vibrate(100);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        button.setOnClickListener(v -> {
            vibrator.vibrate(100);
            account = editText.getText().toString().trim();
            password = editText2.getText().toString().trim();
            if (!account.matches("") && !password.matches("")) {
                loading.show(getString(R.string.Signin));
                new Thread(connect).start();
            }
            else {
                loading.dismiss();
                showMessage.show(getString(R.string.login));
            }
        });
    }

    private Runnable connect = new Runnable() {
        @Override
        public void run() {
            connected.setConnect(account, password, getConnect);
        }
    };

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
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        loading.dismiss();
        userAccount.close();
    }

    @Override
    public void isConnected(JSONObject responseJson) {
        Log.d(TAG, "isConnected!");
        Intent intent = new Intent(this, ViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("responseJson", responseJson.toString());
        startActivity(intent);
        finish();
    }
}