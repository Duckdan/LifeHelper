package com.study.yang.lifehelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.study.yang.lifehelper.adapter.HomeGridViewAdapter;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.MainContract;
import com.study.yang.lifehelper.utils.DialogUtils;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, MainContract.View {

    @Bind(R.id.gv_home)
    GridView mGvHome;
    private SharedPreferences sp;
    private String[] voicePermissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE};
    private String[] compassPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private long exitTime = 0; //推出时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mGvHome.setAdapter(new HomeGridViewAdapter(this));
        mGvHome.setOnItemClickListener(this);
        sp = getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                boolean isAddPwd = sp.getBoolean(LifeHelper.IS_ADD_PWD, true);
                String pwd = sp.getString(LifeHelper.DIARY_PWD, "");
                if (isAddPwd || !TextUtils.isEmpty(pwd)) {
                    DialogUtils.showDialog(this, this);
                    break;
                }
                if (!isAddPwd && TextUtils.isEmpty(pwd)) {
                    jumpToActivity(DiaryActivity.class);
                }
                //
                break;
            case 1:
                jumpToActivity(ConstellationActivity.class);
                break;
            case 2:
                jumpToActivity(ParitiesActivity.class);
                break;
            case 3:
                jumpToActivity(WeatherActivity.class);
                break;
            case 4:
                checkCompassSelfPermissionFromUser();
                break;
            case 5:
                checkVoiceSelfPermissionFromUser();
                break;
            case 6:
                jumpToActivity(PhoneActivity.class);
                break;
            case 7:
                jumpToActivity(NoteActivity.class);
                break;
            case 8:
                jumpToActivity(SetActivity.class);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - exitTime <= 2000) {
            finish();
        } else {
            ToastUtils.showToast(this, "再点一次将推出应用！");
            exitTime = System.currentTimeMillis();
        }
    }

    private void jumpToActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    private void checkCompassSelfPermissionFromUser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            //  没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 弹窗需要解释为何需要该权限，再次请求授权
                ToastUtils.showToast(this, "请授于应用定位权限！");

                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(this, compassPermissions, 1);
            }
        } else {
            jumpToActivity(CompassActivity.class);
        }
    }

    private void checkVoiceSelfPermissionFromUser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                ToastUtils.showToast(this, "请授于应用电话权限和录音权限！");

                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this, voicePermissions, 0);
            }
        } else {
            jumpToActivity(VoiceActivity.class);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (permissions.length == this.voicePermissions.length) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    jumpToActivity(VoiceActivity.class);
                }
                break;
            case 1:
                if (permissions.length == this.compassPermissions.length) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    jumpToActivity(CompassActivity.class);
                }
                break;
        }
    }
}
