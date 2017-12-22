package com.study.yang.lifehelper.contract;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.study.yang.lifehelper.bean.compass.CompassResultBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/24/024.
 */

public interface CompassContract {

    public interface View {
        void firstShowLaLong();
        void setResultDegress(String resultDegress);
        void setAnimation(RotateAnimation rotateAnimation);
        void setLaLong();
        String getLa();
        String getLong();
        void success(String response);
        void fail();
    }

    public interface Presenter {
        String firstShowLaLong();
        void registerProxy();
        void getAddressByLaLong();
    }

    public interface Model {
        String firstShowLaLong();
        RotateAnimation getRotateAnimation(float degrees);
        Animation getAnimation(Animation animation);
        void registerProxy(CompassContract.View view);
        void getAddressByLaLong(String lation,String longtion,StringCallBack stringCallBack);
    }

    public interface StringCallBack{
        void success(String response);
        void fail();
    }

    public interface Address{
        //http://route.showapi.com/238-2?showapi_appid=29971&lng=114.1786270000&lat=22.5705580000&from=5&showapi_sign=a79945dccf384b61847fc8d13183d67f
       @GET("/238-2")
        Observable<CompassResultBean> getAddressByLngLat(@Query("showapi_appid") String showapi_appid,
                                                         @Query("lng") String lng,@Query("lat") String lat,
                                                         @Query("from") String from,@Query("showapi_sign") String showapi_sign);
    }

}