package com.study.yang.lifehelper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.yang.lifehelper.contract.CompassContract;
import com.study.yang.lifehelper.presenter.CompassPresenterImpl;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/24/024.
 */

public class CompassActivity extends BaseActivity implements CompassContract.View {
    @Bind(R.id.tv_orientation)
    TextView tvOrientation;
    @Bind(R.id.iv_pointer)
    ImageView ivPointer;
    @Bind(R.id.tv_des)
    TextView tvDes;
    @Bind(R.id.rl_compass)
    RelativeLayout mRlCompass;
    private CompassPresenterImpl compassPresenter;
    private double mLongitudeD;
    private double mLatitudeD;
    private String[] split;
    private boolean isClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);
        compassPresenter = new CompassPresenterImpl(this, this);
        firstShowLaLong();
        compassPresenter.registerProxy();
    }


    @Override
    public void firstShowLaLong() {
        String result = compassPresenter.firstShowLaLong();
        split = result.split("#");
        if (TextUtils.isEmpty(result)) {
            ToastUtils.showToast(this, "无法获取当前位置");
        } else {
            tvDes.setText(split[2]);
        }
    }

    @Override
    public void setResultDegress(String resultDegress) {
        tvOrientation.setText(resultDegress);
    }

    @Override
    public void setAnimation(RotateAnimation rotateAnimation) {
        ivPointer.startAnimation(rotateAnimation);
    }


    @Override
    public void setLaLong() {
        firstShowLaLong();
    }

    @Override
    public String getLa() {
        return split[0];
    }

    @Override
    public String getLong() {
        return split[1];
    }

    @Override
    public void success(String response) {
        if (TextUtils.isEmpty(response)) {
            tvDes.setText("很遗憾，当前经纬度没有信息标识！");
        } else {
            tvDes.setText(response);
        }
    }

    @Override
    public void fail() {
        ToastUtils.showToast(this, "网络忙请稍后重试！");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        compassPresenter.onDestroy();
    }


    @OnClick({R.id.rl_compass, R.id.tv_des})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_compass:
                onBackPressed();
                break;
            case R.id.tv_des:
                if (isClick) {
                    compassPresenter.getAddressByLaLong();
                } else {
                    firstShowLaLong();
                }

                isClick = isClick ? false : true;
                break;
        }
    }
}
