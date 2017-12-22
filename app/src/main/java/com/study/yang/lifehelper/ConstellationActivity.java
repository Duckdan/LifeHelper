package com.study.yang.lifehelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.study.yang.lifehelper.adapter.StarGridViewAdapter;
import com.study.yang.lifehelper.bean.constellation.DayInfo;
import com.study.yang.lifehelper.contract.ConstellationContract;
import com.study.yang.lifehelper.presenter.ConstellationPresenterImpl;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public class ConstellationActivity extends BaseActivity implements ConstellationContract.View, AdapterView.OnItemClickListener {


    @Bind(R.id.rl_constellation)
    RelativeLayout mRlConstellation;
    private String[] choices = {"shuiping", "shuangyu", "baiyang", "jinniu", "shuangzi", "juxie",
            "shizi", "chunv", "tiancheng", "tianxie", "sheshou", "mojie"};

    private ConstellationPresenterImpl constellationPresenter;
    private ProgressDialog progressDialog;
    private GridView mGvConstellation;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation);
        ButterKnife.bind(this);
        mGvConstellation = (GridView) findViewById(R.id.gv_constellation);
        mGvConstellation.setOnItemClickListener(this);
        constellationPresenter = new ConstellationPresenterImpl(this, this);
        mGvConstellation.setAdapter(new StarGridViewAdapter(this));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        constellationPresenter.queryConstellationInfo(choices[position]);
        this.position = position;
    }

    @Override
    public void showDialog() {
        progressDialog = ProgressDialog.show(this, null, "正在查询中...");
    }

    @Override
    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void success(DayInfo dayInfo) {
        Intent intent = new Intent(this, ConstellationDetailActivity.class);
        intent.putExtra("dayInfo", dayInfo);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void fail() {
        ToastUtils.showToast(this, "网络忙请稍后重试！");
    }

    @OnClick(R.id.rl_constellation)
    public void onClick() {
        onBackPressed();
    }
}
