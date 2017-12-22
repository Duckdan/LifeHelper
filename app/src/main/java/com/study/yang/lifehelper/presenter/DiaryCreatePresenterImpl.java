package com.study.yang.lifehelper.presenter;

import android.content.Context;

import com.study.yang.lifehelper.contract.DiaryCreateContract;
import com.study.yang.lifehelper.db.Diary;
import com.study.yang.lifehelper.model.DiaryCreateModelImpl;


/**
 * Created by Administrator on 2017/04/05
 */

public class DiaryCreatePresenterImpl implements DiaryCreateContract.Presenter {

    private Context context;
    private DiaryCreateContract.View view;
    private final DiaryCreateModelImpl diaryCreateModel;

    public DiaryCreatePresenterImpl(Context context, DiaryCreateContract.View view) {

        this.context = context;
        this.view = view;
        diaryCreateModel = new DiaryCreateModelImpl();
    }

    @Override
    public void saveDiaryToDB() {
        diaryCreateModel.saveDiaryToDB(view.getDiaryTitle(), view.getDiaryContent(), new DiaryCreateContract.CallBack() {
            @Override
            public void success() {
                view.success();
            }

            @Override
            public void fail() {
                view.fail();
            }
        });
    }

    @Override
    public void modifyDiaryToDB(Diary diary) {
        diaryCreateModel.modifyDiaryToDB(diary, new DiaryCreateContract.CallBack() {
            @Override
            public void success() {
                view.success();
            }

            @Override
            public void fail() {

            }
        });
    }
}