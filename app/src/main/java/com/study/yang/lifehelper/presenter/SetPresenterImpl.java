package com.study.yang.lifehelper.presenter;


import android.content.Context;

import com.study.yang.lifehelper.contract.SetContract;
import com.study.yang.lifehelper.model.SetModelImpl;


/**
 * Created by Administrator on 2017/03/29
 */

public class SetPresenterImpl implements SetContract.Presenter {

    private final Context context;
    private final SetContract.View view;
    private final SetModelImpl setModel;

    public SetPresenterImpl(Context context, SetContract.View view) {
        this.context = context;
        this.view = view;
        setModel = new SetModelImpl(context);
    }

    @Override
    public void initData() {
        setModel.initData(new SetContract.CallBack() {

            @Override
            public void success(boolean voiceHigh,boolean isAddPwd) {
                view.initDataSuccess(voiceHigh,isAddPwd);
            }

            @Override
            public void fail() {

            }
        });
    }

    @Override
    public void saveCbHighState(boolean voiceHigh) {
        setModel.saveCbHighState(voiceHigh);
    }



    @Override
    public void saveCbIsAddPwd(boolean isAddPwd) {
        setModel.saveCbIsAddPwd(isAddPwd);
    }


}