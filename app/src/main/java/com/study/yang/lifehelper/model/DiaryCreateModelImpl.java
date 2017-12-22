package com.study.yang.lifehelper.model;

import android.text.TextUtils;

import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.contract.DiaryCreateContract;
import com.study.yang.lifehelper.db.Diary;


/**
 * Created by Administrator on 2017/04/05
 */

public class DiaryCreateModelImpl implements DiaryCreateContract.Model {

    @Override
    public void saveDiaryToDB(String title, String content, DiaryCreateContract.CallBack callBack) {
        Diary diary = new Diary();
        if (TextUtils.isEmpty(title)) {
            title = "未命名";
        }
        diary.setTitle(title);
        diary.setContent(content);
        diary.setSavetime(System.currentTimeMillis());
        long insert = LifeHelperApplication.diaryDao.insert(diary);
        if (insert <= 0) {
            callBack.fail();
        } else {
            callBack.success();
        }
    }

    @Override
    public void modifyDiaryToDB(Diary diary, DiaryCreateContract.CallBack callBack) {
        diary.setSavetime(System.currentTimeMillis());
        LifeHelperApplication.diaryDao.update(diary);
        callBack.success();
    }
}