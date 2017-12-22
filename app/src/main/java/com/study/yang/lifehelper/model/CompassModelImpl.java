package com.study.yang.lifehelper.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.study.yang.lifehelper.bean.compass.CompassLationBean;
import com.study.yang.lifehelper.bean.compass.CompassResultBean;
import com.study.yang.lifehelper.contract.CompassContract;
import com.study.yang.lifehelper.utils.CompassLocationUtils;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Administrator on 2017/03/24
 */
public class CompassModelImpl implements CompassContract.Model {

    private static final String SPACE = "  ";
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    float[] values = new float[3];
    float[] r = new float[9];

    public static final String NORTH = "北";
    public static final String SOURTH = "南";
    public static final String WEST = "西";
    public static final String EAST = "东";

    public static final String EASTNORTH = "东北";
    public static final String EASTSOURTH = "东南";
    public static final String WESTSOURTH = "西南";
    public static final String WESTNORTH = "西北";

    private CompassLocationUtils compassLocationUtils;
    private Context context;
    private CompassLationBean mLocation;

    private String reallyDegressResult = "";
    private float lastDegrees;
    private SensorManager mSensorManager;
    private CompassContract.View view;

    //传感器事件监听
    private SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValues = event.values.clone();
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magneticFieldValues = event.values.clone();
                    break;
            }
            //获取手机旋转矩阵的方法
            SensorManager.getRotationMatrix(r, null, accelerometerValues, magneticFieldValues);
            //获取手机个xyz三个方向的夹角，z,x,y
            SensorManager.getOrientation(r, values);

            float reallyDegrees = (float) Math.toDegrees(values[0]);
            if (Math.abs(reallyDegrees) < 5) { //当角度变化小于5的时候不发生变动
                return;
            }
            float degrees = -reallyDegrees;

            String resultDegress = topDegreesTextShow(reallyDegrees);
            view.setResultDegress(resultDegress);

            RotateAnimation rotateAnimation = getRotateAnimation(degrees);
            view.setAnimation(rotateAnimation);

            //    view.setLaLong();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    public CompassModelImpl(Context context) {
        this.context = context;
        compassLocationUtils = new CompassLocationUtils(context);
    }

    @Override
    public String firstShowLaLong() {

        if (compassLocationUtils.isEnbled()) {
            compassLocationUtils.exec();
            mLocation = compassLocationUtils.getCompassLationBean();
            String result = LaLong(mLocation.getLatitude(), mLocation.getLongitude());
            return result;
        } else {
            return null;
        }
    }

    @Override
    public RotateAnimation getRotateAnimation(float degrees) {
        RotateAnimation rotateAnimation = new RotateAnimation(lastDegrees, degrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //保存上一次的角度
        lastDegrees = degrees;
        //动画匀速执行
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
//            mIvPointer.startAnimation(rotateAnimation);
        return rotateAnimation;
    }


    @Override
    public Animation getAnimation(Animation animation) {
        return animation;
    }

    @Override
    public void registerProxy(CompassContract.View view) {
        this.view = view;
        //获取传感器的管理者
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        //获取磁场传感器
        Sensor magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //获取加速器传感器
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //注册磁场传感器，第三个参数代表最快更新
        mSensorManager.registerListener(mListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //注册磁场传感器，第三个参数代表最快更新
        mSensorManager.registerListener(mListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void getAddressByLaLong(String lation, String longtion, final CompassContract.StringCallBack stringCallBack) {
        new Retrofit.
                Builder().
                baseUrl("http://route.showapi.com").
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                build().
                create(CompassContract.Address.class).
                getAddressByLngLat("29971", longtion, lation, "5", "a79945dccf384b61847fc8d13183d67f").
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<CompassResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        stringCallBack.fail();
                    }

                    @Override
                    public void onNext(CompassResultBean compassResultBean) {
                        String formatted_address = compassResultBean.showapi_res_body.formatted_address;
                        stringCallBack.success(formatted_address);
                    }
                });
    }

    private String LaLong(double mLatitudeD, double mLongitudeD) {
        String address = mLatitudeD + "#" + mLongitudeD + "#";
        String mLatitude = "";
        String mLongitude = "";
        if (mLatitudeD > 0) {
            mLatitude = "北纬";
        } else {
            mLatitude = "南纬";
            mLatitudeD = -mLatitudeD;
        }
        mLatitude = mLatitude + mLatitudeD;

        if (mLongitudeD > 0) {
            mLongitude = "东经";
        } else {
            mLongitude = "西经";
            mLongitudeD = -mLongitudeD;
        }
        mLongitude = mLongitude + mLongitudeD;

        return address + mLatitude + "   " + mLongitude;
    }

    /**
     * 顶部角度展示
     *
     * @param reallyDegrees
     */
    public String topDegreesTextShow(float reallyDegrees) {
        if (-20 <= reallyDegrees && reallyDegrees <= 20) {
            if (reallyDegrees >= 0) {
                reallyDegressResult = NORTH + SPACE + (int) (reallyDegrees) + "°";
                // mTvOrientation.setText(NORTH + SPACE + (int) (reallyDegrees) + "°");
            } else {
                reallyDegrees += 360;
                // mTvOrientation.setText(NORTH + SPACE + (int) (reallyDegrees) + "°");
                reallyDegressResult = NORTH + SPACE + (int) (reallyDegrees) + "°";
            }
        } else if (-70 < reallyDegrees && reallyDegrees < -20) {
            reallyDegrees += 360;
            //mTvOrientation.setText(WESTNORTH + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = WESTNORTH + SPACE + (int) (reallyDegrees) + "°";
        } else if (-110 <= reallyDegrees && reallyDegrees <= -70) {
            reallyDegrees += 360;
            // mTvOrientation.setText(WEST + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = WEST + SPACE + (int) (reallyDegrees) + "°";
        } else if (-160 < reallyDegrees && reallyDegrees < -110) {
            reallyDegrees += 360;
            // mTvOrientation.setText(WESTSOURTH + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = WESTSOURTH + SPACE + (int) (reallyDegrees) + "°";
        } else if (-180 <= reallyDegrees && reallyDegrees <= -160) {
            reallyDegrees += 360;
            // mTvOrientation.setText(SOURTH + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = SOURTH + SPACE + (int) (reallyDegrees) + "°";
        } else if (160 <= reallyDegrees && reallyDegrees <= 180) {
            // mTvOrientation.setText(SOURTH + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = SOURTH + SPACE + (int) (reallyDegrees) + "°";
        } else if (110 < reallyDegrees && reallyDegrees < 160) {
            // mTvOrientation.setText(EASTSOURTH + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = EASTSOURTH + SPACE + (int) (reallyDegrees) + "°";
        } else if (70 <= reallyDegrees && reallyDegrees <= 110) {
            //  mTvOrientation.setText(EAST + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = EAST + SPACE + (int) (reallyDegrees) + "°";
        } else if (20 < reallyDegrees && reallyDegrees < 70) {
            //  mTvOrientation.setText(EASTNORTH + SPACE + (int) (reallyDegrees) + "°");
            reallyDegressResult = EASTNORTH + SPACE + (int) (reallyDegrees) + "°";
        }
        return reallyDegressResult;
    }

    public void onDestroy() {
        if (compassLocationUtils != null && mSensorManager != null) {
            compassLocationUtils.removeListener();
            mSensorManager.unregisterListener(mListener);
        }
    }
}