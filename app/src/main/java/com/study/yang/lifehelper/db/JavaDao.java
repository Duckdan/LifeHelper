package com.study.yang.lifehelper.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Administrator on 2017/3/17/017.
 */

public class JavaDao {
    public static void main(String[] args) {
        Schema schema = new Schema(1,"com.study.yang.lifehelper.db");

        Entity note = schema.addEntity("Note");
        note.addIdProperty().autoincrement();
        note.addStringProperty("title");
        note.addStringProperty("saveTime");
        note.addStringProperty("content");
        note.addLongProperty("orderTime");
        note.addStringProperty("colorType");
        note.addIntProperty("deleteType");

        Entity weatherEntity = schema.addEntity("weather");
        weatherEntity.addIdProperty().autoincrement();
        weatherEntity.addStringProperty("daytime");
        weatherEntity.addIntProperty("day_weather_code");
        weatherEntity.addStringProperty("day_weather");
        weatherEntity.addStringProperty("day_wind_power");
        weatherEntity.addStringProperty("day_weather_pic");
        weatherEntity.addStringProperty("day_air_temperature");
        weatherEntity.addStringProperty("day_wind_direction");
        weatherEntity.addIntProperty("night_weather_code");
        weatherEntity.addStringProperty("night_weather");
        weatherEntity.addStringProperty("night_wind_power");
        weatherEntity.addStringProperty("night_air_temperature");
        weatherEntity.addStringProperty("night_weather_pic");
        weatherEntity.addStringProperty("night_wind_direction");

        Entity diary = schema.addEntity("Diary");
        diary.addIdProperty().autoincrement();
        diary.addStringProperty("title");
        diary.addStringProperty("content");
        diary.addLongProperty("savetime");
        diary.addStringProperty("deleteType");

        Entity voice = schema.addEntity("Voice");
        voice.addIdProperty().autoincrement();
        voice.addStringProperty("filename");
        voice.addLongProperty("duration");
        voice.addStringProperty("filepath");

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
