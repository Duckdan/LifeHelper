package com.study.yang.lifehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.study.yang.lifehelper.contract.ModifyPasswordContract;
import com.study.yang.lifehelper.presenter.ModifyPasswordPresenterImpl;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/12/012.
 */

public class ModifyPasswordActivity extends BaseActivity implements ModifyPasswordContract.View {


    @Bind(R.id.rl_set)
    RelativeLayout rlSet;
    @Bind(R.id.et_new_pwd)
    EditText etNewPwd;
    @Bind(R.id.iv_delete_pwd1)
    ImageView ivDeletePwd1;
    @Bind(R.id.et_again_pwd)
    EditText etAgainPwd;
    @Bind(R.id.iv_delete_pwd2)
    ImageView ivDeletePwd2;
    @Bind(R.id.bt_login)
    Button btLogin;
    private ModifyPasswordPresenterImpl modifyPasswordPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        modifyPasswordPresenter = new ModifyPasswordPresenterImpl(this, this);
        registerViewListener();
    }


    @OnClick({R.id.rl_set, R.id.iv_delete_pwd1, R.id.iv_delete_pwd2, R.id.bt_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_set:
                finish();
                break;
            case R.id.iv_delete_pwd1:
                etNewPwd.setText("");
                break;
            case R.id.iv_delete_pwd2:
                etAgainPwd.setText("");
                break;
            case R.id.bt_login:
                hideInputSoft();
                modifyPasswordPresenter.modifyPassword();
                break;
        }
    }

    @Override
    public void registerViewListener() {


        etNewPwd.addTextChangedListener(new TextContentChange(ivDeletePwd1));
        etNewPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ivDeletePwd1.setVisibility(View.INVISIBLE);
                }
                if (hasFocus && !TextUtils.isEmpty(etNewPwd.getText().toString())) {
                    ivDeletePwd1.setVisibility(View.VISIBLE);
                }
            }
        });

        etAgainPwd.addTextChangedListener(new TextContentChange(ivDeletePwd2));
        etAgainPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ivDeletePwd2.setVisibility(View.INVISIBLE);
                }
                if (hasFocus && !TextUtils.isEmpty(etAgainPwd.getText().toString())) {
                    ivDeletePwd2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public String getNewPwd() {
        return etNewPwd.getText().toString().trim();
    }

    @Override
    public String getAgainPwd() {
        return etAgainPwd.getText().toString().trim();
    }

    @Override
    public void success() {
        finish();
        ToastUtils.showToast(this, "修改成功,可返回重新登录！");
    }

    @Override
    public void fail() {
        ToastUtils.showToast(this, "修改失败！");
    }

    @Override
    public void hideInputSoft() {
        etNewPwd.clearFocus();
        etAgainPwd.clearFocus();
        mInputMethodManager.hideSoftInputFromWindow(btLogin.getWindowToken(), 0);
    }

    public class TextContentChange implements TextWatcher {
        private ImageView imageView;

        public TextContentChange(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() != 0) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
