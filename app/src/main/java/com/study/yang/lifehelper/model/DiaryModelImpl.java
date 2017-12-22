package com.study.yang.lifehelper.model;

import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.contract.DiaryContract;
import com.study.yang.lifehelper.db.Diary;
import com.study.yang.lifehelper.db.DiaryDao;



import java.util.List;

/**
 * Created by Administrator on 2017/04/05
 */

public class DiaryModelImpl implements DiaryContract.Model {

    @Override
    public void showDiaryFromDB(DiaryContract.CallBack callBack) {
        List<Diary> diaryList = LifeHelperApplication.
                diaryDao.
                queryBuilder().
                orderDesc(DiaryDao.Properties.Savetime).
                build().
                list();
        if (diaryList != null) {
            callBack.success(diaryList);
        } else {
            callBack.fail();
        }
    }
}