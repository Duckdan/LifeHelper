package com.study.yang.lifehelper.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.db.weather;
import com.zhy.autolayout.utils.AutoUtils;


import java.util.List;


public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.MyViewHolder> {
    private final Context context;
    private List<weather> weatherDayList;

    public WeatherListAdapter(Context context, List<weather> weatherDayList) {
        this.context = context;
        this.weatherDayList = weatherDayList;
    }

    @Override
    public int getItemCount() {
        return weatherDayList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_day_night,null);
        AutoUtils.autoSize(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        weather weatherDayList =  this.weatherDayList.get(position);
        String daytime = weatherDayList.daytime;
        StringBuilder sb = new StringBuilder(daytime);
        sb.insert(4,"年");
        sb.insert(7,"月");
        sb.insert(10,"日");
        holder.tvDate.setText(sb.toString());

        holder.sdvDay.setImageURI(Uri.parse(weatherDayList.day_weather_pic));
        holder.tvDayWeather.setText(weatherDayList.day_weather);
        holder.tvDayWind.setText(weatherDayList.day_wind_direction+" "+weatherDayList.day_wind_power);
        holder.tvDayTemperature.setText(weatherDayList.day_air_temperature+"℃");

        holder.sdvNight.setImageURI(Uri.parse(weatherDayList.night_weather_pic));
        holder.tvNightWeather.setText(weatherDayList.night_weather);
        holder.tvNightWind.setText(weatherDayList.night_wind_direction+" "+weatherDayList.night_wind_power);
        holder.tvNightTemperature.setText(weatherDayList.night_air_temperature+"℃");

    }

    class MyViewHolder extends ViewHolder {


        public LinearLayout llDay;
        public LinearLayout llNight;
        public TextView tvDayWeather;
        public TextView tvDayWind;
        public TextView tvDayTemperature;
        public TextView tvNightWeather;
        public TextView tvNightWind;
        public TextView tvNightTemperature;
        public SimpleDraweeView sdvDay;
        public SimpleDraweeView sdvNight;
        public TextView tvDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            findViewById();
        }

        /**
         * 查找控件
         */
        private void findViewById() {
            tvDate =  (TextView) itemView.findViewById(R.id.tv_date);

            llDay = (LinearLayout) itemView.findViewById(R.id.ll_day);
            sdvDay = (SimpleDraweeView) itemView.findViewById(R.id.sdv_day);
            tvDayWeather = (TextView) itemView.findViewById(R.id.tv_day_weather);
            tvDayWind = (TextView) itemView.findViewById(R.id.tv_day_wind);
            tvDayTemperature = (TextView) itemView.findViewById(R.id.tv_day_temperature);

            llNight = (LinearLayout) itemView.findViewById(R.id.ll_night);
            sdvNight = (SimpleDraweeView) itemView.findViewById(R.id.sdv_night);
            tvNightWeather = (TextView) itemView.findViewById(R.id.tv_night_weather);
            tvNightWind = (TextView) itemView.findViewById(R.id.tv_night_wind);
            tvNightTemperature = (TextView) itemView.findViewById(R.id.tv_night_temperature);
        }
    }
}
