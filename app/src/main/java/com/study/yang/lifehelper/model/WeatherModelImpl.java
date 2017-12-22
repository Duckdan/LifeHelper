package com.study.yang.lifehelper.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.study.yang.lifehelper.adapter.WeatherListAdapter;
import com.study.yang.lifehelper.bean.weather.WeatherBean;
import com.study.yang.lifehelper.bean.weather.WeatherResultBean;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.WeatherContract;
import com.study.yang.lifehelper.db.DaoMaster;
import com.study.yang.lifehelper.db.DaoSession;
import com.study.yang.lifehelper.db.weather;
import com.study.yang.lifehelper.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.study.yang.lifehelper.app.LifeHelperApplication.weatherDao;

/**
 * Created by Administrator on 2017/03/23
 */

public class WeatherModelImpl implements WeatherContract.Model {

    private Context context;

    private WeatherListAdapter lastAdapter;


    public WeatherModelImpl(Context context) {
        this.context = context;

    }


    @Override
    public void reallyQueryInfoWeather(final String address, final AdapterCallBack adapterCallBack) {
        Log.d("TAG/Weather", "reallyQueryInfoWeather");
        clearDB();
        new Retrofit.
                Builder().
                baseUrl("https://route.showapi.com/").
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(WeatherContract.Weather.class).
                getWeatherAllData(address, "29971", "a79945dccf384b61847fc8d13183d67f").
                enqueue(new Callback<WeatherResultBean>() {
                    //成功
                    @Override
                    public void onResponse(retrofit2.Call<WeatherResultBean> call, retrofit2.Response<WeatherResultBean> response) {
//                        Log.d("TAG/Weather",response.body().toString());
                        WeatherResultBean resultBean = response.body();
                        int ret_code = resultBean.showapi_res_body.ret_code;
                        if (ret_code == -1) {
                            ToastUtils.showToast(context, "找不到此地区！");
                            adapterCallBack.addressFail();
                            return;
                        }
                        saveLastQueryAddress(address);
                        WeatherListAdapter adapter = createAdapter(resultBean);
                        adapterCallBack.success(adapter);
                    }

                    //失败
                    @Override
                    public void onFailure(retrofit2.Call<WeatherResultBean> call, Throwable t) {
//                        Log.d("TAG/Weather",t.getMessage());
                        adapterCallBack.fail(t);
                    }
                });
    }

    /**
     * 创建适配器
     */
    private WeatherListAdapter createAdapter(WeatherResultBean resultBean) {
        WeatherBean showapi_res_body = resultBean.showapi_res_body;

        List<weather> dayList = showapi_res_body.dayList;
        if (dayList == null) {
            dayList = new ArrayList<>();
        } else {
            saveDataToDB(dayList);
        }
        WeatherListAdapter weatherListAdapter = new WeatherListAdapter(context, dayList);

        return weatherListAdapter;
    }

    /**
     * 保存数据至数据库
     *
     * @param dayList
     */
    private void saveDataToDB(List<weather> dayList) {
        Observable.
                from(dayList).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<weather>() {
                    @Override
                    public void call(weather weatherDayList) {
                        weatherDao.insert(weatherDayList);
                    }
                });
    }

    @Override
    public void readFromDB(final AdapterCallBack adapterCallBack) {

        Observable.
                create(new Observable.OnSubscribe<List<weather>>() {


                    @Override
                    public void call(Subscriber<? super List<weather>> subscriber) {
                        QueryBuilder<weather> queryBuilder = weatherDao.queryBuilder();
                        List<weather> list = queryBuilder.build().list();
                        subscriber.onNext(list);
                    }
                }).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<List<weather>>() {
                    @Override
                    public void call(List<weather> weatherDayLists) {
                        lastAdapter = new WeatherListAdapter(context, weatherDayLists);
                        adapterCallBack.success(lastAdapter);
                    }

                });

    }

    /**
     * 保存地名
     */
    @Override
    public void saveLastQueryAddress(String address) {
        SharedPreferences sp = context.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("address", address).commit();
    }

    @Override
    public void clearDB() {
        weatherDao.deleteAll();
    }

    @Override
    public String readLastQueryAddress() {
        SharedPreferences sp = context.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        String address = sp.getString("address", "");
        return address;
    }

}