package com.study.yang.lifehelper.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.study.yang.lifehelper.bean.compass.CompassLationBean;

import java.util.List;

public class CompassLocationUtils {
    private Context mContext;

    private LocationManager locationManager;
    private String provider;
    private String provider_gps_network;
    private Location location = null;
    private CompassLationBean compassLationBean = null;

    public CompassLocationUtils(Context mContext) {
        super();
        this.mContext = mContext;
        locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        compassLationBean = new CompassLationBean();
    }

    public boolean isEnbled() {
        if (hasGPSDevice(mContext)) {
//            Toast.makeText(mContext, "has GPS", Toast.LENGTH_SHORT).show();
            // 如果GPS关，则打开
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false)
                OnOffGps();
            provider_gps_network = LocationManager.GPS_PROVIDER;
            return true;
        } else {
            Toast.makeText(mContext, "no GPS", Toast.LENGTH_SHORT).show();
            if (isNetworkAvailable(mContext) == false) {
                Toast.makeText(mContext, "no network", Toast.LENGTH_SHORT)
                        .show();
                return false;
            } else {
                provider_gps_network = LocationManager.NETWORK_PROVIDER;
                return true;
            }
        }
    }

    @TargetApi(23)
    public void exec() {
        // 通过GPS获取当前坐标
        // 获取经纬度
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 设置服务商的信息
            Criteria criteria = new Criteria();
            // 提供服务的精度标准
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            // 不需要高度信息
            criteria.setAltitudeRequired(false);
            // 不需要方位信息
            criteria.setBearingRequired(false);
            // 不允许产生费用
            criteria.setCostAllowed(false);
            // 消耗电力为低
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            // 取得最匹配的criteria
            provider = locationManager.getBestProvider(criteria, true);

            if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             //   try {
                    location = locationManager.getLastKnownLocation(provider);
                    if (location != null) {
                        setMyLocation();
                    } else {
                        if (provider_gps_network == LocationManager.GPS_PROVIDER)
                            GpsForArea();
                        else if (provider_gps_network == LocationManager.NETWORK_PROVIDER)
                            NetWorkForArea();
                    }
              //  } catch (Exception e) {
                //    e.printStackTrace();
               // }
            } else {
                location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    setMyLocation();
                } else {
                    if (provider_gps_network == LocationManager.GPS_PROVIDER)
                        GpsForArea();
                    else if (provider_gps_network == LocationManager.NETWORK_PROVIDER)
                        NetWorkForArea();
                }
            }

        } else {// 通过网络获取当前坐标
            NetWorkForArea();
        }
    }

    // 判断是否有可用网络
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else { // 如果仅仅是用来判断网络连接 　　　　　　 //则可以使用
            // cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 判断是否有GPS设备
    public boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    // 开关GPS
    public void OnOffGps() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(mContext, 0, gpsIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    // 通过网络获取当前位置坐标
    //Build.VERSION_CODES.M =23
    public void NetWorkForArea() {
        Log.i("TAG", ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)+"" );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);

                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    setMyLocation();
                }
            } catch (Exception e) {
                e.printStackTrace();
             System.out.println(" NetWorkForArea===ifeeee");
            }
          System.out.println(" NetWorkForArea===if");
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);

            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                setMyLocation();
            }
            System.out.println(" NetWorkForArea===else");
        }

    }

    // 通过GPS获取当前位置坐标
    public void GpsForArea() {
        Log.i("TAG", ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)+"" );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        100, 0, locationListener);

                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    setMyLocation();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(" GpsForArea===if");
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100, 0, locationListener);

            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                setMyLocation();
            }
            System.out.println(" GpsForArea===else");
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) { // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            if (location != null) {
                setMyLocation();
                System.out.println("locationListener===");
            }
        }

        public void onProviderDisabled(String provider) {
            // Provider被disable时触发此函数，比如GPS被关闭
        }

        public void onProviderEnabled(String provider) {
            // Provider被enable时触发此函数，比如GPS被打开
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        }
    };

    private void setMyLocation() {
        System.out.println("setMyLocation===");
        compassLationBean.setLatitude(location.getLatitude());
        compassLationBean.setLongitude(location.getLongitude());
    }

    public CompassLationBean getCompassLationBean() {
        return compassLationBean;
    }

    public void removeListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                if (locationManager != null) {
                    locationManager.removeUpdates(locationListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
            }
        }
    }
}
