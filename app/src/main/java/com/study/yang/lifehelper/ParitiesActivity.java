package com.study.yang.lifehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.yang.lifehelper.adapter.ParitiesListAdapter;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.ParitiesContract;
import com.study.yang.lifehelper.presenter.ParitiesPresenterImpl;
import com.study.yang.lifehelper.utils.DialogUtils;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParitiesActivity extends BaseActivity implements ParitiesContract.View, AdapterView.OnItemClickListener {

    @Bind(R.id.tv_show_usa)
    TextView tvShowUsa;
    @Bind(R.id.tv_show_rmb)
    TextView tvShowRmb;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.tv_original)
    TextView tvOriginal;
    @Bind(R.id.tv_target)
    TextView tvTarget;
    @Bind(R.id.bt_change)
    Button btChange;
    @Bind(R.id.lv_country)
    ListView lvCountry;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.rl_parities)
    RelativeLayout mRlParities;

    private ParitiesPresenterImpl paritiesPresenter;
    private int originalIndex, targetIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parities);
        ButterKnife.bind(this);
        paritiesPresenter = new ParitiesPresenterImpl(this, this);
        showRmbToUsa();
        lvCountry.setAdapter(new ParitiesListAdapter(this));
        lvCountry.setOnItemClickListener(this);
    }

//    @OnClick(R.id.bt_change)
//    public void onClick() {
//        paritiesPresenter.showChangeResult();
//    }

    @Override
    public void showRmbToUsa() {
        paritiesPresenter.showFirstRmbToUsa();
        paritiesPresenter.showFirstUsaToRmb();
    }

    @Override
    public void successRmbToUsa(String response) {
        tvShowRmb.setText("1元人民币=" + response + "美元");
    }

    @Override
    public void failRmbToUsa() {
        ToastUtils.showToast(this, "网络忙，请稍后重试！");
    }

    @Override
    public void successUsaToRmb(String response) {
        tvShowUsa.setText("1美元=" + response + "元人民币");
    }

    @Override
    public void failUsaToRmb() {
        ToastUtils.showToast(this, "网络忙，请稍后重试！");
    }

    @Override
    public void successResult(String response) {
        tvResult.setVisibility(View.VISIBLE);
        tvShowRmb.setVisibility(View.GONE);
        tvShowUsa.setVisibility(View.GONE);
        if (!"-1".equals(response)) {
            String orginal = tvOriginal.getText().toString().trim();
            String target = tvTarget.getText().toString().trim();
            tvResult.setText(getNumber() + orginal + "=" + response + target);
        } else {
            tvResult.setText("目前不支持这两种货币的兑换！");
            ToastUtils.showToast(this, "目前不支持这两种货币的兑换！");
        }
    }

    @Override
    public void failResult() {
        ToastUtils.showToast(this, "网络忙，请稍后重试！");
    }

    @Override
    public String getOriginalCode() {
        return LifeHelper.DH[originalIndex];
    }

    @Override
    public String getTargetCode() {
        return LifeHelper.DH[targetIndex];
    }

    @Override
    public String getNumber() {
        return etNumber.getText().toString().trim();
    }


    @Override
    public void showMyDialog(int position) {
        DialogUtils.createDialog(this, position);
    }

    @Override
    public void setTvOriginal(int original) {
        if (original == -1) {
            return;
        }
        originalIndex = original;
        tvOriginal.setText(LifeHelper.HB[original]);
    }

    @Override
    public void setTvTarget(int target) {
        if (target == -1) {
            return;
        }
        targetIndex = target;
        tvTarget.setText(LifeHelper.HB[target]);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showMyDialog(position);
    }


    @OnClick({R.id.rl_parities, R.id.bt_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_parities:
                onBackPressed();
                break;
            case R.id.bt_change:
                String number = getNumber();
                String originalCode = getOriginalCode();
                String targetCode = getTargetCode();
                if (TextUtils.isEmpty(number) || "源货币".equals(originalCode) || "目标货币".equals(targetCode)) {
                    ToastUtils.showToast(this,"请完善信息之后再查询！");
                    return;
                }
                paritiesPresenter.showChangeResult();
                break;
        }
    }
}
