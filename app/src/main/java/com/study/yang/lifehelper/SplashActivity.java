package com.study.yang.lifehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/4/18/018.
 */

public class SplashActivity extends Activity {
    private Handler mhandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }.start();
    }
}
