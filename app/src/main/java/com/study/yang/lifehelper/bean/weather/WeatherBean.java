package com.study.yang.lifehelper.bean.weather;


import com.study.yang.lifehelper.db.weather;

import java.util.List;

/**
 * Created by zouqianyu on 2017/3/15.
 */
public class WeatherBean {
//    "ret_code":0,
//            "area":"深圳",
//            "areaid":"101280601",
//            "dayList":
    public int ret_code;
    public String area;
    public String areaid;
    public List<weather> dayList;
}
