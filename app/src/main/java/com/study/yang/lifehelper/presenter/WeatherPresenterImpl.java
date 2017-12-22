package com.study.yang.lifehelper.presenter;
import android.content.Context;

import com.study.yang.lifehelper.adapter.WeatherListAdapter;
import com.study.yang.lifehelper.contract.WeatherContract;
import com.study.yang.lifehelper.model.WeatherModelImpl;

/**
* Created by Administrator on 2017/03/23
*/

public class WeatherPresenterImpl implements WeatherContract.Presenter{

    private Context context;
    private WeatherContract.View iview;
    private final WeatherModelImpl mWeatherModel;

    public WeatherPresenterImpl(Context context, WeatherContract.View iview) {
        this.context = context;
        this.iview = iview;
        mWeatherModel = new WeatherModelImpl(context);
    }
    @Override
    public void queryWeatherInfo() {
        //展示对话框
        iview.showDialog();

        mWeatherModel.reallyQueryInfoWeather(iview.getAddress(),new WeatherContract.Model.AdapterCallBack(){

            @Override
            public void success(WeatherListAdapter weatherListAdapter) {
                iview.dismissDialog();
                iview.success(weatherListAdapter);

            }

            @Override
            public void addressFail() {
                iview.dismissDialog();
            }

            @Override
            public void fail(Throwable throwable) {
                iview.dismissDialog();
                iview.fail(throwable);
            }
        });
    }

    @Override
    public void readFromDB() {
        String address = mWeatherModel.readLastQueryAddress();
        iview.showLastQueryAddress(address);

        mWeatherModel.readFromDB(new WeatherContract.Model.AdapterCallBack() {
            @Override
            public void success(WeatherListAdapter weatherListAdapter) {
                iview.setAdapter(weatherListAdapter);
            }


            @Override
            public void addressFail() {
                iview.dismissDialog();
            }


            @Override
            public void fail(Throwable throwable) {
                iview.fail(null);
            }
        });

    }

}