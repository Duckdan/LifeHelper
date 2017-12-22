package com.study.yang.lifehelper.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.study.yang.lifehelper.db.DaoMaster;
import com.study.yang.lifehelper.db.DaoSession;
import com.study.yang.lifehelper.db.DiaryDao;
import com.study.yang.lifehelper.db.NoteDao;
import com.study.yang.lifehelper.db.VoiceDao;
import com.study.yang.lifehelper.db.weatherDao;
import com.zhy.autolayout.config.AutoLayoutConifg;


/**
 * Created by Administrator on 2017/3/31/031.
 */

public class LifeHelperApplication extends Application {
    public static DaoMaster.DevOpenHelper helperDB;
    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static NoteDao noteDao;

    public static DiaryDao diaryDao;
    public static VoiceDao voiceDao;
    public static weatherDao weatherDao;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //屏幕适配
       // AutoLayoutConifg.getInstance().useDeviceSize();
        context = this;
        initDao();
    }

    private void initDao() {
        if (helperDB == null) {
            helperDB = new DaoMaster.DevOpenHelper(getApplicationContext(), "lifeHelper.db", null);
        }
        SQLiteDatabase db = helperDB.getWritableDatabase();
        if (daoMaster == null) {
            daoMaster = new DaoMaster(db);
        }
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }


        if (noteDao == null) {
            noteDao = daoSession.getNoteDao();
        }

        if (diaryDao == null) {
            diaryDao = daoSession.getDiaryDao();
        }

        if (voiceDao == null) {
            voiceDao = daoSession.getVoiceDao();
        }

        if (weatherDao == null) {
            weatherDao = daoSession.getWeatherDao();
        }
    }

    public static void deleteDataFromDB() {
        if (noteDao != null) {
            noteDao.deleteAll();
        }

        if (diaryDao != null) {
            diaryDao.deleteAll();
        }

        if (voiceDao != null) {
            voiceDao.deleteAll();
        }

        if (weatherDao != null) {
            weatherDao.deleteAll();
        }
    }
}
