package com.study.yang.lifehelper.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.SetContract;


/**
 * Created by Administrator on 2017/03/29
 */

public class SetModelImpl implements SetContract.Model {

    private Context context;

    public SetModelImpl(Context context) {

        this.context = context;
    }

    @Override
    public void initData(SetContract.CallBack callBack) {
        SharedPreferences sp = context.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        boolean voiceHigh = sp.getBoolean(LifeHelper.VOICE_HIGH, true);
        boolean isAddPwd = sp.getBoolean(LifeHelper.IS_ADD_PWD, true);
        callBack.success(voiceHigh,isAddPwd);
    }

    @Override
    public void saveCbHighState(boolean voiceHigh) {
        SharedPreferences sp = context.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(LifeHelper.VOICE_HIGH, voiceHigh).commit();
    }



    @Override
    public void saveCbIsAddPwd(boolean isAddPwd) {
        SharedPreferences sp = context.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(LifeHelper.IS_ADD_PWD, isAddPwd).commit();
    }
}