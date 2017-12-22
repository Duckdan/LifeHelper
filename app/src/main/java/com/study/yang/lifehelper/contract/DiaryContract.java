package com.study.yang.lifehelper.contract;



import com.study.yang.lifehelper.db.Diary;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5/005.
 */

public class DiaryContract {

    public interface View {
        void showDiaryFromDB();
        void success(List<Diary> response);
        void fail();

        void refreshRv(Diary diary);
    }

    public interface Presenter {
        void showDiaryFromDB();
    }

    public interface Model {
        void showDiaryFromDB(CallBack callBack);
    }

    public interface CallBack {
        void success(List<Diary> response);

        void fail();
    }
}