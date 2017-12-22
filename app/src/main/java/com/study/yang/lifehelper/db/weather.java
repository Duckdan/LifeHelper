package com.study.yang.lifehelper.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "WEATHER".
 */
public class weather {

    public Long id;
    public String daytime;
    public Integer day_weather_code;
    public String day_weather;
    public String day_wind_power;
    public String day_weather_pic;
    public String day_air_temperature;
    public String day_wind_direction;
    public Integer night_weather_code;
    public String night_weather;
    public String night_wind_power;
    public String night_air_temperature;
    public String night_weather_pic;
    public String night_wind_direction;

    public weather() {
    }

    public weather(Long id) {
        this.id = id;
    }

    public weather(Long id, String daytime, Integer day_weather_code, String day_weather, String day_wind_power, String day_weather_pic, String day_air_temperature, String day_wind_direction, Integer night_weather_code, String night_weather, String night_wind_power, String night_air_temperature, String night_weather_pic, String night_wind_direction) {
        this.id = id;
        this.daytime = daytime;
        this.day_weather_code = day_weather_code;
        this.day_weather = day_weather;
        this.day_wind_power = day_wind_power;
        this.day_weather_pic = day_weather_pic;
        this.day_air_temperature = day_air_temperature;
        this.day_wind_direction = day_wind_direction;
        this.night_weather_code = night_weather_code;
        this.night_weather = night_weather;
        this.night_wind_power = night_wind_power;
        this.night_air_temperature = night_air_temperature;
        this.night_weather_pic = night_weather_pic;
        this.night_wind_direction = night_wind_direction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public Integer getDay_weather_code() {
        return day_weather_code;
    }

    public void setDay_weather_code(Integer day_weather_code) {
        this.day_weather_code = day_weather_code;
    }

    public String getDay_weather() {
        return day_weather;
    }

    public void setDay_weather(String day_weather) {
        this.day_weather = day_weather;
    }

    public String getDay_wind_power() {
        return day_wind_power;
    }

    public void setDay_wind_power(String day_wind_power) {
        this.day_wind_power = day_wind_power;
    }

    public String getDay_weather_pic() {
        return day_weather_pic;
    }

    public void setDay_weather_pic(String day_weather_pic) {
        this.day_weather_pic = day_weather_pic;
    }

    public String getDay_air_temperature() {
        return day_air_temperature;
    }

    public void setDay_air_temperature(String day_air_temperature) {
        this.day_air_temperature = day_air_temperature;
    }

    public String getDay_wind_direction() {
        return day_wind_direction;
    }

    public void setDay_wind_direction(String day_wind_direction) {
        this.day_wind_direction = day_wind_direction;
    }

    public Integer getNight_weather_code() {
        return night_weather_code;
    }

    public void setNight_weather_code(Integer night_weather_code) {
        this.night_weather_code = night_weather_code;
    }

    public String getNight_weather() {
        return night_weather;
    }

    public void setNight_weather(String night_weather) {
        this.night_weather = night_weather;
    }

    public String getNight_wind_power() {
        return night_wind_power;
    }

    public void setNight_wind_power(String night_wind_power) {
        this.night_wind_power = night_wind_power;
    }

    public String getNight_air_temperature() {
        return night_air_temperature;
    }

    public void setNight_air_temperature(String night_air_temperature) {
        this.night_air_temperature = night_air_temperature;
    }

    public String getNight_weather_pic() {
        return night_weather_pic;
    }

    public void setNight_weather_pic(String night_weather_pic) {
        this.night_weather_pic = night_weather_pic;
    }

    public String getNight_wind_direction() {
        return night_wind_direction;
    }

    public void setNight_wind_direction(String night_wind_direction) {
        this.night_wind_direction = night_wind_direction;
    }

}