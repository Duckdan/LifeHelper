package com.study.yang.lifehelper.contract;

/**
 * Created by Administrator on 2017/4/12/012.
 */

public class ModifyPasswordContract {
    public interface View {
        void registerViewListener();

        String getNewPwd();

        String getAgainPwd();

        void success();

        void fail();

        void hideInputSoft();
    }

    public interface Presenter {
        void modifyPassword();
    }

    public interface Model {
        void modifyPassword(String newPwd, String againPwd, CallBack callBack);
    }

    public interface CallBack {
        void success(String response);

        void fail();
    }

}