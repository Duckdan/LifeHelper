package com.study.yang.lifehelper.contract;

/**
 * Created by Administrator on 2017/4/10/010.
 */

public class VoiceContract {

    public interface View {
        void setAdapter();
        void registerViewListener();
        int getCurrentItemIndex();
    }

    public interface Presenter {
    }

    public interface Model {
    }


}