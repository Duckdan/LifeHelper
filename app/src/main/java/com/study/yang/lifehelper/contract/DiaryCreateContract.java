package com.study.yang.lifehelper.contract;


import com.study.yang.lifehelper.db.Diary;

/**
 * Created by Administrator on 2017/4/5/005.
 */

public class DiaryCreateContract {

    public interface View {
        String getDiaryTitle();

        String getDiaryContent();

        void success();

        void fail();
    }

    public interface Presenter {
        void saveDiaryToDB();
        void modifyDiaryToDB(Diary diary);
    }

    public interface Model {
        void saveDiaryToDB(String title, String content, CallBack callBack);
        void modifyDiaryToDB(Diary diary, CallBack callBack);
    }

    public interface CallBack {
        void success();

        void fail();
    }

}