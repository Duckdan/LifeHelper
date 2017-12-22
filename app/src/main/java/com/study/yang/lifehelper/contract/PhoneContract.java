package com.study.yang.lifehelper.contract;

import com.study.yang.lifehelper.bean.phone.PhoneBean;
import com.study.yang.lifehelper.bean.phone.PhoneResultBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/24/024.
 */

public interface PhoneContract {

    public interface View {
        String getPhoneNumber();

        void showDialog();

        void dismissDialog();

        void success(PhoneBean response);

        void fail();
    }

    public interface Presenter {
        void queryPhoneNumber();
    }

    public interface Model<T> {
        void queryPhoneNumber(String address, CallBack<T> callBack);
    }

    public interface CallBack<T> {
        void success(T response);
        void phoneFail();
        void fail();
    }

    public interface PhoneQueryInfo {
        //https://route.showapi.com/6-1?num=15824880849&showapi_appid=29971&showapi_sign=a79945dccf384b61847fc8d13183d67f
        @GET("/6-1")
        Observable<PhoneResultBean> getPhoneQueryInfo(@Query("num") String num, @Query("showapi_appid") String showapi_appid, @Query("showapi_sign") String showapi_sign);
    }
}