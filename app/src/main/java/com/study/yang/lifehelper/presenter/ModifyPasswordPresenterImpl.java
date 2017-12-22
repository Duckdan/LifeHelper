package com.study.yang.lifehelper.presenter;

import android.content.Context;

import com.study.yang.lifehelper.contract.ModifyPasswordContract;
import com.study.yang.lifehelper.model.ModifyPasswordModelImpl;


/**
 * Created by Administrator on 2017/04/12
 */

public class ModifyPasswordPresenterImpl implements ModifyPasswordContract.Presenter {

    private final Context context;
    private final ModifyPasswordContract.View view;
    private final ModifyPasswordModelImpl modifyPasswordModel;

    public ModifyPasswordPresenterImpl(Context context, ModifyPasswordContract.View view) {

        this.context = context;
        this.view = view;
        modifyPasswordModel = new ModifyPasswordModelImpl(context);
    }

    @Override
    public void modifyPassword() {
        modifyPasswordModel.modifyPassword(view.getNewPwd(), view.getAgainPwd(), new ModifyPasswordContract.CallBack() {
            @Override
            public void success(String response) {
                view.success();
            }

            @Override
            public void fail() {
                view.fail();
            }
        });
    }
}