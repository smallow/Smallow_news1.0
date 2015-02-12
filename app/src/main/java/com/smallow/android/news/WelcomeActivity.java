package com.smallow.android.news;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;


public class WelcomeActivity extends Activity {


    private boolean isFirstIn = false;
    private static final int TIME = 2000;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };

    private void goGuide() {
        Intent intent=new Intent(WelcomeActivity.this,GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void goHome() {
        Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("smallow", MODE_PRIVATE);
        isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE,TIME);
            sharedPreferences.edit().putBoolean("isFirstIn",false).commit();
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME,TIME);
        }

    }


}
