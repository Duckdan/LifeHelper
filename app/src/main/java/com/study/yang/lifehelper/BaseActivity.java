package com.study.yang.lifehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by Administrator on 2017/4/27/027.
 */

public class BaseActivity extends AppCompatActivity {

    protected InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TAG/onTouch===", "222222");
        if (this.getCurrentFocus() != null) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            Log.d("TAG/onTouch", "11111");
        }
        return super.onTouchEvent(event);
    }
}
