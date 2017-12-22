package com.study.yang.lifehelper.model;

import android.content.Context;

import com.study.yang.lifehelper.bean.phone.PhoneBean;
import com.study.yang.lifehelper.bean.phone.PhoneResultBean;
import com.study.yang.lifehelper.contract.PhoneContract;
import com.study.yang.lifehelper.utils.CheckPhoneRegexUtils;
import com.study.yang.lifehelper.utils.ToastUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/03/24
 */

public class PhoneModelImpl implements PhoneContract.Model<PhoneBean> {

    private Context context;

    public PhoneModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public void queryPhoneNumber(String num, final PhoneContract.CallBack<PhoneBean> callBack) {
        if (!CheckPhoneRegexUtils.isMobile(num)) {
            ToastUtils.showToast(context, "当前号码不对，请重输！");
            callBack.phoneFail();
            return;
        }
        new Retrofit.
                Builder().
                baseUrl("https://route.showapi.com").
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                build().
                create(PhoneContract.PhoneQueryInfo.class).
                getPhoneQueryInfo(num, "29971", "a79945dccf384b61847fc8d13183d67f").
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<PhoneResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.fail();
                    }

                    @Override
                    public void onNext(PhoneResultBean phoneResultBean) {
                        callBack.success(phoneResultBean.showapi_res_body);
                    }
                });
    }
}