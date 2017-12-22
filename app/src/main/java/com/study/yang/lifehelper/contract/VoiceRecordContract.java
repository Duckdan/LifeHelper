package com.study.yang.lifehelper.contract;

/**
 * Created by Administrator on 2017/4/10/010.
 */

public class VoiceRecordContract {
    public interface View {
        /**
         * 配置数据
         */
        void initDataProxy();


        /**
         * 重置文件名
         */
        void resetFileNameEditText();

        /**
         * 开始录音
         */
        void startRecord();

        /**
         * 停止录音
         */
        void stopRecord();

        /**
         * 获取文件名
         */
        String getFileName();

        /**
         * 更新界面
         */
        void updateUi(boolean response);
    }

    public interface Presenter {
        /**
         * 开始录音
         */
        void startRecord();

        /**
         * 停止录音
         */
        void stopRecord();

        /**
         * 保存数据到数据库
         */
        void saveFileToDB(String fileName, long duration, String filePath);
    }

    public interface Model {
        /**
         * 开始录音
         * @param fileName
         * @param callBack
         */
        void startRecord(String fileName, CallBack callBack);

        /**
         * 停止录音
         */
        void stopRecord();

        /**
         * 录音的具体逻辑
         */
        void startRecord();

        /**
         * 保存录音信息到数据库
         * @param fileName
         * @param duration
         * @param filePath
         * @param callBack
         */
        void saveFileToDB(String fileName, long duration, String filePath, CallBack callBack);
    }

    public interface CallBack{
        void recordVoiceSuccess(boolean response);
        void recordVoiceFail();
    }

}