package com.study.yang.lifehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.study.yang.lifehelper.contract.DiaryCreateContract;
import com.study.yang.lifehelper.db.Diary;
import com.study.yang.lifehelper.presenter.DiaryCreatePresenterImpl;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/5/005.
 */

public class DiaryCreateActivity extends BaseActivity implements DiaryCreateContract.View {
    @Bind(R.id.rl_diary)
    RelativeLayout rlDiary;
    @Bind(R.id.iv_diary_save)
    ImageView ivDiarySave;
    @Bind(R.id.et_diary_title)
    EditText etDiaryTitle;
    @Bind(R.id.et_diary_content)
    EditText etDiaryContent;
    @Bind(R.id.tv_diary_title_2)
    TextView tvDiaryTitle2;
    @Bind(R.id.tv_diary)
    TextView tvDiary;
    @Bind(R.id.ll_et_diary)
    LinearLayout llEtDiary;
    @Bind(R.id.ll_tv_diary)
    LinearLayout llTvDiary;

    //区分是否保存两次的标记
    private boolean isIvDiarySave = false;
    private DiaryCreatePresenterImpl diaryCreatePresenter;
    private Diary diary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_create);
        ButterKnife.bind(this);
        diaryCreatePresenter = new DiaryCreatePresenterImpl(this, this);
        aboutIntentProxy();
    }

    private void aboutIntentProxy() {
        Intent intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("diary");
        if (diary != null) {
            llEtDiary.setVisibility(View.GONE);
            etDiaryTitle.setVisibility(View.GONE);
            etDiaryContent.setVisibility(View.GONE);

            llTvDiary.setVisibility(View.VISIBLE);
            tvDiaryTitle2.setVisibility(View.VISIBLE);
            tvDiary.setVisibility(View.VISIBLE);
            tvDiary.setMovementMethod(ScrollingMovementMethod.getInstance());

            tvDiary.setText(diary.getContent());
            tvDiary.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    aboutTvOnLongClick();
                    return true;
                }
            });

            tvDiaryTitle2.setText(diary.getTitle());
            tvDiaryTitle2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    aboutTvOnLongClick();
                    return true;
                }
            });

        } else {
            llEtDiary.setVisibility(View.VISIBLE);
            etDiaryTitle.setVisibility(View.VISIBLE);
            etDiaryContent.setVisibility(View.VISIBLE);

            llTvDiary.setVisibility(View.GONE);
            tvDiaryTitle2.setVisibility(View.GONE);
            tvDiary.setVisibility(View.GONE);
        }
    }

    private void aboutTvOnLongClick() {
        ToastUtils.showToast(DiaryCreateActivity.this, "进入编辑模式！");

        tvDiary.setVisibility(View.GONE);
        llEtDiary.setVisibility(View.VISIBLE);
        llTvDiary.setVisibility(View.GONE);
        etDiaryContent.setVisibility(View.VISIBLE);
        etDiaryContent.setText(diary.getContent());
        etDiaryContent.setSelection(diary.getContent().length());

        tvDiaryTitle2.setVisibility(View.GONE);
        etDiaryTitle.setVisibility(View.VISIBLE);
        etDiaryTitle.setText(diary.getTitle());
        etDiaryTitle.setSelection(diary.getTitle().length());
    }

    @OnClick({R.id.rl_diary, R.id.iv_diary_save})
    public void onClick(View view) {
        String editTextContent = etDiaryContent.getText().toString().trim();
        switch (view.getId()) {
            case R.id.rl_diary:
                if (TextUtils.isEmpty(editTextContent)) {
                    finish();
                    break;
                }

                if (diary != null && !isIvDiarySave) {
                    String title = etDiaryTitle.getText().toString().trim();
                    if (TextUtils.isEmpty(title)) {
                        title = "未命名";
                    }
                    diary.setTitle(title);
                    diary.setContent(editTextContent);
                    diaryCreatePresenter.modifyDiaryToDB(diary);
                    finish();
                    break;
                }

                if (!isIvDiarySave) {
                    diaryCreatePresenter.saveDiaryToDB();
                }
                finish();
                break;
            case R.id.iv_diary_save:
                if (TextUtils.isEmpty(editTextContent)) {
                    ToastUtils.showToast(this, "内容为空，不能保存！");
                    break;
                }
                if (diary != null) {
                    String title = etDiaryTitle.getText().toString().trim();
                    if (TextUtils.isEmpty(title)) {
                        title = "未命名";
                    }
                    diary.setTitle(title);
                    diary.setContent(editTextContent);
                    diaryCreatePresenter.modifyDiaryToDB(diary);
                    isIvDiarySave = true;
                    finish();
                    break;
                }
                isIvDiarySave = true;
                diaryCreatePresenter.saveDiaryToDB();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String editTextContent = getDiaryContent();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtils.isEmpty(editTextContent)) {
                return super.onKeyDown(keyCode, event);
            }

            if (diary != null && !isIvDiarySave) {
                String title = etDiaryTitle.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    title = "未命名";
                }

                diary.setTitle(title);

                diary.setContent(editTextContent);
                diaryCreatePresenter.modifyDiaryToDB(diary);
                return super.onKeyDown(keyCode, event);
            }

            if (!isIvDiarySave) {
                diaryCreatePresenter.saveDiaryToDB();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public String getDiaryTitle() {
        return etDiaryTitle.getText().toString().trim();
    }

    @Override
    public String getDiaryContent() {
        return etDiaryContent.getText().toString().trim();
    }

    @Override
    public void success() {
        ToastUtils.showToast(this, "保存成功！");
    }

    @Override
    public void fail() {
        ToastUtils.showToast(this, "保存失败！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isIvDiarySave = false;
    }
}
