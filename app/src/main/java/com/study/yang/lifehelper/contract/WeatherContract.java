package com.study.yang.lifehelper.contract;

import com.study.yang.lifehelper.adapter.WeatherListAdapter;
import com.study.yang.lifehelper.bean.weather.WeatherResultBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/3/23/023.
 */

public interface WeatherContract {

    public interface View {
        /**
         * 获取地址
         */
        String getAddress();


        /**
         * 设置适配器
         */
        void setAdapter(WeatherListAdapter weatherListAdapter);

        /**
         * 显示查询天气的对话框
         */
        void showDialog();

        /**
         * 隐藏对话框
         */
        void dismissDialog();

        /**
         * 查询成功
         */
        void success(WeatherListAdapter weatherListAdapter);

        /**
         * 查询失败
         */
        void fail(Throwable throwable);

        /**
         * 显示上次查询的地名
         */
        void showLastQueryAddress(String address);
    }

    public interface Presenter {
        void queryWeatherInfo();
        void readFromDB();
    }

    public interface Model {
        void reallyQueryInfoWeather(String address,AdapterCallBack adapterCallBack);
        void readFromDB(AdapterCallBack adapterCallBack);
        void saveLastQueryAddress(String address);
        void clearDB();
        String readLastQueryAddress();
        public interface AdapterCallBack{
            void success(WeatherListAdapter weatherListAdapter);
            void addressFail();
            void fail(Throwable throwable);
        }
    }

    public interface   Weather {
        //https://route.showapi.com/9-9?area=深圳&showapi_appid=29971&&showapi_sign=a79945dccf384b61847fc8d13183d67f
        @GET("/9-9")
        Call<WeatherResultBean> getWeatherAllData(@Query("area") String location, @Query("showapi_appid") String showapi_appid, @Query("showapi_sign") String showapi_sign );
    }
}