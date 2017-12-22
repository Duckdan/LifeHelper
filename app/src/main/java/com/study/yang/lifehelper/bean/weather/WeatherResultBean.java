package com.study.yang.lifehelper.bean.weather;


/**
 * Created by Administrator on 2017/3/17/017.
 */

public class WeatherResultBean {
//            "showapi_res_code": 0,
//            "showapi_res_error": "",
//            "showapi_res_body":
    public String showapi_res_code;
    public String showapi_res_error;
    public WeatherBean showapi_res_body;

    @Override
    public String toString() {
        return "WeatherResultBean{" +
                "showapi_res_body=" + showapi_res_body +
                ", showapi_res_code='" + showapi_res_code + '\'' +
                ", showapi_res_error='" + showapi_res_error + '\'' +
                '}';
    }
}
