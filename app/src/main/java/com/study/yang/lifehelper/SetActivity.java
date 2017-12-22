package com.study.yang.lifehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.study.yang.lifehelper.contract.SetContract;
import com.study.yang.lifehelper.presenter.SetPresenterImpl;
import com.study.yang.lifehelper.utils.DialogUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends BaseActivity implements SetContract.View {

    @Bind(R.id.rl_set)
    RelativeLayout rlSet;
    @Bind(R.id.ll_reset_pwd)
    LinearLayout llResetPwd;
    @Bind(R.id.cb_set_high)
    CheckBox cbSetHigh;
    @Bind(R.id.rl_voice_high_quality)
    RelativeLayout rlVoiceHighQuality;
    @Bind(R.id.ll_clear_all)
    LinearLayout llClearAll;
    @Bind(R.id.cb_add_pwd)
    CheckBox cbAddPwd;
    @Bind(R.id.rl_add_pwd)
    RelativeLayout rlAddPwd;
    private SetPresenterImpl setPresenter;
    private boolean voiceHigh = false;
    private boolean isAddPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        setPresenter = new SetPresenterImpl(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPresenter.initData();
    }

    @OnClick({R.id.rl_set, R.id.rl_add_pwd, R.id.ll_reset_pwd, R.id.rl_voice_high_quality, R.id.ll_clear_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_set:
                finish();
                break;
            case R.id.rl_add_pwd:
                isAddPwd = isAddPwd ? false : true;
                cbAddPwd.setChecked(isAddPwd);
                setPresenter.saveCbIsAddPwd(isAddPwd);
                break;
            case R.id.ll_reset_pwd:
                showSecurityDialog();
                break;
            case R.id.rl_voice_high_quality:
                voiceHigh = voiceHigh ? false : true;
                cbSetHigh.setChecked(voiceHigh);
                setPresenter.saveCbHighState(voiceHigh);
                break;

            case R.id.ll_clear_all:
                showTipDialog("真的要清空所有数据吗？");
                break;
        }
    }

    @Override
    public void showSecurityDialog() {
        DialogUtils.showSecurityDialog(this);
    }

    @Override
    public void showTipDialog(String tipString) {
        DialogUtils.showTipDialog(this, tipString);
    }



    @Override
    public void initDataSuccess(boolean voiceHigh,boolean isAddPwd) {
        this.voiceHigh = voiceHigh;
        this.isAddPwd = isAddPwd;
        cbSetHigh.setChecked(voiceHigh);
        cbAddPwd.setChecked(isAddPwd);
    }

    @Override
    public void refreshActivity() {
        setPresenter.initData();
    }

}
