package com.study.yang.lifehelper.contract;

import com.study.yang.lifehelper.bean.constellation.DayInfo;
import com.study.yang.lifehelper.bean.constellation.StarResultDayInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public interface ConstellationContract {

    public interface View {
        void showDialog();
        void dismissDialog();
        void success(DayInfo dayInfo);
        void fail();
    }

    public interface Presenter {
        void queryConstellationInfo(String name);
    }

    public interface Model<T> {
        void queryConstellationInfo(String name,CallBack<T> callBack);
    }

    public interface CallBack<T> {
        void success(T dayInfo);
        void fail();
    }

    public interface Day{
        @GET("/872-1")
        Observable<StarResultDayInfo> getDayInfo(@Query("star") String star, @Query("showapi_appid") String showapi_appid,
                                                 @Query("showapi_sign") String showapi_sign);
    }

}