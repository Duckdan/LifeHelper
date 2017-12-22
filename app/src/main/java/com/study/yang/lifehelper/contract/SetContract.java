package com.study.yang.lifehelper.contract;

/**
 * Created by Administrator on 2017/3/29/029.
 */

public class SetContract {

    public interface View {
        /**
         * 显示密保问题的对话框
         */
        void showSecurityDialog();

        /**
         * 显示提示对话框
         */
        void showTipDialog(String tipString);

        void initDataSuccess(boolean voiceHigh, boolean isAddPwd);

        void refreshActivity();
    }

    public interface Presenter {

        void initData();


        void saveCbHighState(boolean voiceHigh);



        void saveCbIsAddPwd(boolean isAddPwd);
    }

    public interface Model {
        void initData(CallBack callBack);


        void saveCbHighState(boolean voiceHigh);


        void saveCbIsAddPwd(boolean isAddPwd);

    }

    public interface CallBack {
        void success(boolean voiceHigh, boolean isAddPwd);

        void fail();
    }

}