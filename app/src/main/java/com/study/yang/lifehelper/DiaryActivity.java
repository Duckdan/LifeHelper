package com.study.yang.lifehelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.study.yang.lifehelper.adapter.DiaryListAdapter;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.DiaryContract;
import com.study.yang.lifehelper.db.Diary;
import com.study.yang.lifehelper.presenter.DiaryPresenterImpl;
import com.study.yang.lifehelper.utils.DialogUtils;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiaryActivity extends BaseActivity implements DiaryContract.View {

    @Bind(R.id.rl_diary)
    RelativeLayout rlDiary;
    @Bind(R.id.rl_diary_first)
    RelativeLayout rlDiaryFirst;
    @Bind(R.id.rv_diary_list)
    RecyclerView rvDiaryList;
    @Bind(R.id.iv_diary_show_mode)
    ImageView ivDiaryShowMode;
    @Bind(R.id.ll_add_diary)
    LinearLayout llAddDiary;
    private DiaryPresenterImpl diaryPresenter;
    private DiaryListAdapter adapter;
    private boolean isLinearLayout = false;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButterKnife.bind(this);
        diaryPresenter = new DiaryPresenterImpl(this, this);
        SharedPreferences sp = getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        boolean isAddPwd = sp.getBoolean(LifeHelper.IS_ADD_PWD, true);
        if (!isAddPwd) {
            // 清除密码
            sp.edit().remove(LifeHelper.DIARY_PWD).commit();
            // 清楚是否创建密码的痕迹
            sp.edit().remove(LifeHelper.DIARY_IS_CREATE_PWD).commit();
            //清除幸运数
            sp.edit().remove(LifeHelper.DIARY_LUCK_NUMBER).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDiaryFromDB();
    }

    @OnClick({R.id.rl_diary, R.id.iv_diary_show_mode, R.id.ll_add_diary})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_diary:
                finish();
                break;
            case R.id.iv_diary_show_mode:
                changeRvLayoutManager();
                break;
            case R.id.ll_add_diary:
                Intent intent = new Intent(this, DiaryCreateActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void changeRvLayoutManager() {
        if (isLinearLayout) {
            if (linearLayoutManager == null) {
                linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            }
            rvDiaryList.setLayoutManager(linearLayoutManager);

        } else {
            if (gridLayoutManager == null) {
                gridLayoutManager = new GridLayoutManager(this, 2);
            }
            rvDiaryList.setLayoutManager(gridLayoutManager);
        }
        isLinearLayout = isLinearLayout ? false : true;
    }

    @Override
    public void showDiaryFromDB() {
        //  List<Diary> diaryList = LifeHelperApplication.diaryDao.queryBuilder().build().list();
        diaryPresenter.showDiaryFromDB();
    }

    @Override
    public void refreshRv(Diary diary) {
        if (adapter != null) {
            adapter.getData(diary);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void success(List<Diary> response) {

        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvDiaryList.setLayoutManager(linearLayoutManager);
        }

        if (adapter == null) {
            adapter = new DiaryListAdapter(this, response);

            adapter.setOnDiaryItemClickListener(new DiaryListAdapter.OnDiaryItemClickListener() {
                @Override
                public void onClick(Diary diary) {
                    Intent intent = new Intent(DiaryActivity.this, DiaryCreateActivity.class);
                    intent.putExtra("diary", diary);
                    startActivity(intent);
                }
            });

            adapter.setOnDiaryItemLongClickListener(new DiaryListAdapter.OnDiaryItemLongClickListener() {
                @Override
                public void onLongClick(Diary diary) {
                    DialogUtils.showDeleteDialog(DiaryActivity.this, diary);
                }
            });
            rvDiaryList.setAdapter(adapter);
        } else {
            adapter.setData(response);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void fail() {
        ToastUtils.showToast(this, "读取数据库失败！");
    }
}
