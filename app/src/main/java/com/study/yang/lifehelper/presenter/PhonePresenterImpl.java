package com.study.yang.lifehelper.presenter;

import android.content.Context;

import com.study.yang.lifehelper.bean.phone.PhoneBean;
import com.study.yang.lifehelper.contract.PhoneContract;
import com.study.yang.lifehelper.model.PhoneModelImpl;

/**
 * Created by Administrator on 2017/03/24
 */

public class PhonePresenterImpl implements PhoneContract.Presenter {

    private Context context;
    private PhoneContract.View view;
    private final PhoneModelImpl phoneModel;

    public PhonePresenterImpl(Context context, PhoneContract.View view) {
        this.context = context;
        this.view = view;
        phoneModel = new PhoneModelImpl(context);
    }

    @Override
    public void queryPhoneNumber() {
        view.showDialog();
        phoneModel.queryPhoneNumber(view.getPhoneNumber(), new PhoneContract.CallBack<PhoneBean>() {
            @Override
            public void success(PhoneBean response) {
                view.dismissDialog();
                view.success(response);
            }

            @Override
            public void phoneFail() {
                view.dismissDialog();
            }

            @Override
            public void fail() {
                view.dismissDialog();
                view.fail();
            }
        });
    }

}