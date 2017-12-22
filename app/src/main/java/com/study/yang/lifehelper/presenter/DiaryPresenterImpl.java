package com.study.yang.lifehelper.presenter;

import android.content.Context;


import com.study.yang.lifehelper.contract.DiaryContract;
import com.study.yang.lifehelper.db.Diary;
import com.study.yang.lifehelper.model.DiaryModelImpl;

import java.util.List;

/**
 * Created by Administrator on 2017/04/05
 */

public class DiaryPresenterImpl implements DiaryContract.Presenter {

    private final Context context;
    private final DiaryContract.View view;
    private final DiaryModelImpl diaryModel;

    public DiaryPresenterImpl(Context context, DiaryContract.View view) {

        this.context = context;
        this.view = view;
        diaryModel = new DiaryModelImpl();
    }

    @Override
    public void showDiaryFromDB() {
        diaryModel.showDiaryFromDB(new DiaryContract.CallBack() {
            @Override
            public void success(List<Diary> response) {
                view.success(response);
            }

            @Override
            public void fail() {
                view.fail();
            }
        });
    }
}