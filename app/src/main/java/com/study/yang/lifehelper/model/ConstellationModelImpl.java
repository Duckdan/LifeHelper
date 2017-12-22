package com.study.yang.lifehelper.model;
import com.study.yang.lifehelper.bean.constellation.DayInfo;
import com.study.yang.lifehelper.bean.constellation.StarResultDayInfo;
import com.study.yang.lifehelper.contract.ConstellationContract;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
* Created by Administrator on 2017/03/23
*/

public class ConstellationModelImpl implements ConstellationContract.Model<DayInfo> {

    @Override
    public void queryConstellationInfo(String name, final ConstellationContract.CallBack<DayInfo> callBack) {
        new Retrofit.
                Builder().
                baseUrl("https://route.showapi.com").
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                build().
                create(ConstellationContract.Day.class).
                getDayInfo(name, "29971", "a79945dccf384b61847fc8d13183d67f").
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<StarResultDayInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.fail();
                    }

                    @Override
                    public void onNext(StarResultDayInfo starResultDayInfo) {
                        callBack.success(starResultDayInfo.showapi_res_body.day);
                    }
                });
    }

}