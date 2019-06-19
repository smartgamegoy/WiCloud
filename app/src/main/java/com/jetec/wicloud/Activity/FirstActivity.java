package com.jetec.wicloud.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.jetec.wicloud.Pageview.*;
import com.jetec.wicloud.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirstActivity extends AppCompatActivity {

    private String TAG = "FirstActivity";
    private PageAdapter pageAdapter;
    private List<PageView> pageList;
    private Vibrator vibrator;
    private ViewPagerIndicator viewPagerIndicator;
    private Boolean p;

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

        Listview();
        Pageview();

    }

    private void Listview(){
        pageList = new ArrayList<>();
        pageList.add(new PageOneView(this));
        pageList.add(new PageTwoView(this));
        pageList.add(new PageThreeView(this));
        pageAdapter = new PageAdapter(pageList);
    }

    private void Pageview(){

        setContentView(R.layout.firstview);

        ViewPager viewPager = findViewById(R.id.pager);
        viewPagerIndicator = findViewById(R.id.indicator);
        viewPagerIndicator.setLength(pageList.size());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPagerIndicator.setSelected(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        p = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        p = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (viewPager.getCurrentItem() == Objects.requireNonNull(viewPager.getAdapter()).getCount() - 1 && !p) {
                            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                }
            }
        });
        //viewPager.setCurrentItem((pageList.size()) * 1000); //unlimited circle
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                vibrator.vibrate(100);
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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
    }
}