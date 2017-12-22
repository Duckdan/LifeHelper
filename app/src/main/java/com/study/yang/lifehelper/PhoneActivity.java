package com.study.yang.lifehelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.yang.lifehelper.bean.phone.PhoneBean;
import com.study.yang.lifehelper.contract.PhoneContract;
import com.study.yang.lifehelper.presenter.PhonePresenterImpl;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/24/024.
 */

public class PhoneActivity extends BaseActivity implements PhoneContract.View {
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.bt_query)
    Button btQuery;
    @Bind(R.id.tv_QCellCore)
    TextView tvQCellCore;
    @Bind(R.id.tv_carrier_operator)
    TextView tvCarrierOperator;
    @Bind(R.id.tv_province_code)
    TextView tvProvinceCode;
    @Bind(R.id.tv_post_code)
    TextView tvPostCode;
    @Bind(R.id.tv_area_code)
    TextView tvAreaCode;
    @Bind(R.id.rl_phone)
    RelativeLayout mRlPhone;
    private ProgressDialog dialog;
    private PhonePresenterImpl phonePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        phonePresenter = new PhonePresenterImpl(this, this);
    }

    @Override
    public String getPhoneNumber() {
        return etPhone.getText().toString().trim();
    }

    @Override
    public void showDialog() {
        dialog = ProgressDialog.show(this, null, "正在查询...");
    }

    @Override
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void success(PhoneBean response) {
        if (response != null) {
            tvQCellCore.setText("号码归属地：" + response.prov + response.city);
            tvCarrierOperator.setText("运营商：" + response.name);
            tvProvinceCode.setText("城市编码：" + response.provCode);
            tvPostCode.setText("邮编：" + response.postCode);
            tvAreaCode.setText("区号：" + response.areaCode);
        }
    }

    @Override
    public void fail() {
        ToastUtils.showToast(this, "网络忙，请稍后重试！");
    }

    @OnClick({R.id.rl_phone, R.id.bt_query})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_phone:
                onBackPressed();
                break;
            case R.id.bt_query:
                String phoneNumber = getPhoneNumber();
                if(TextUtils.isEmpty(phoneNumber)){
                    ToastUtils.showToast(this,"号码不能为空");
                    return;
                }
                phonePresenter.queryPhoneNumber();
                break;
        }
    }


//    @OnClick(R.id.bt_query)
//    public void onClick() {
//        phonePresenter.queryPhoneNumber();
//    }
}
