package com.study.yang.lifehelper.model;

import android.content.Context;


import com.study.yang.lifehelper.bean.parities.ParitiesResultBean;
import com.study.yang.lifehelper.contract.ParitiesContract;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/03/27
 */

public class ParitiesModelImpl implements ParitiesContract.Model {

    private Context context;
    private final String RMB = "CNY";
    private final String USA = "USD";

    public ParitiesModelImpl(Context context) {
        this.context = context;
    }


    @Override
    public void showFirstRmbToUsa(ParitiesContract.CallBack callBack) {
        showChangeResult(RMB, "1", USA, callBack);
    }

    @Override
    public void showFirstUsaToRmb(ParitiesContract.CallBack callBack) {
        showChangeResult(USA, "1", RMB, callBack);
    }

    @Override
    public void showChangeResult(String original, String number, String target, final ParitiesContract.CallBack callBack) {
        new Retrofit.
                Builder().
                baseUrl("https://route.showapi.com").
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                build().
                create(ParitiesContract.Change.class).
                getChangeResult(original, number, "29971", target, "a79945dccf384b61847fc8d13183d67f").
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<ParitiesResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.fail();
                    }

                    @Override
                    public void onNext(ParitiesResultBean paritiesResultBean) {
                        if (paritiesResultBean.showapi_res_body.ret_code == 0) {
                            String money = paritiesResultBean.showapi_res_body.money;
                            callBack.success(money);
                        } else if (paritiesResultBean.showapi_res_body.ret_code == -1) {
                            callBack.success("-1");
                        } else {
                            callBack.fail();
                        }
                    }
                });
    }
}