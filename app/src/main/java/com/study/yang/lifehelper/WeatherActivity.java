package com.study.yang.lifehelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.study.yang.lifehelper.adapter.WeatherListAdapter;
import com.study.yang.lifehelper.contract.WeatherContract;
import com.study.yang.lifehelper.presenter.WeatherPresenterImpl;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class WeatherActivity extends BaseActivity implements WeatherContract.View {
    @Bind(R.id.et_address)
    EditText mEtAddress;
    @Bind(R.id.bt_query)
    Button mBtQuery;
    @Bind(R.id.rv_show)
    RecyclerView mRvShow;

    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.rl_weather)
    RelativeLayout mRlWeather;
    private WeatherPresenterImpl mWeatherPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        // DateFormat.format();
        mWeatherPresenter = new WeatherPresenterImpl(this, this);
        mWeatherPresenter.readFromDB();
        mRvShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager manager = (InputMethodManager) WeatherActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (WeatherActivity.this.getCurrentFocus() != null && WeatherActivity.this.getCurrentFocus().getWindowToken() != null) {
                        manager.hideSoftInputFromWindow(WeatherActivity.this.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });
    }


    @Override
    public String getAddress() {
        return mEtAddress.getText().toString().trim();
    }


    @Override
    public void setAdapter(WeatherListAdapter weatherListAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvShow.setLayoutManager(linearLayoutManager);
        mRvShow.setAdapter(weatherListAdapter);
    }

    @Override
    public void showDialog() {
        mProgressDialog = ProgressDialog.show(this, null, "正在查询中...");
    }

    @Override
    public void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void success(WeatherListAdapter weatherListAdapter) {
        setAdapter(weatherListAdapter);
    }


    @Override
    public void fail(Throwable throwable) {
        ToastUtils.showToast(this, "网络忙，请稍后重试！");
    }

    @Override
    public void showLastQueryAddress(String address) {
        if (!TextUtils.isEmpty(address)) {
            tvArea.setVisibility(View.VISIBLE);
            tvArea.setText(address);
        }
    }

    @OnClick({R.id.rl_weather, R.id.bt_query})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_weather:
                onBackPressed();
                break;
            case R.id.bt_query:
                String address = mEtAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    ToastUtils.showToast(this, "地名不能为空！");
                    return;
                }
                tvArea.setVisibility(View.VISIBLE);
                tvArea.setText(address);
                mWeatherPresenter.queryWeatherInfo();
                break;
        }
    }
}
