/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.study.yang.lifehelper.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.study.yang.lifehelper.bean.voice.Recorder;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.IRecorder;
import com.study.yang.lifehelper.utils.ToastUtils;

import java.io.File;
import java.io.IOException;


public class RecorderService extends Service implements MediaRecorder.OnErrorListener {

    private int outputFileFormat;
    private Recorder recorder;
    private static MediaRecorder mediaRecorder;
    private boolean highQuality;
    private static long startTime;
    private static long stopTime;
    private static File soundFile;
    private static String fileName;
    private Handler handler = new Handler();
    private static long duration;

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            duration = System.currentTimeMillis() - startTime;
            if (duration >= (99 * 1000 * 1000 + 60 * 1000)) {
                reallyStopRecorder();
                ToastUtils.showToast(RecorderService.this, "达到录音最大时长，录音已停止！");
                sendBroadcast(LifeHelper.RECORDER_STOP);
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RecorderBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        recorder = Recorder.getInstence();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        outputFileFormat = intent.getIntExtra(LifeHelper.OUTPUT_FORMAT, MediaRecorder.OutputFormat.AMR_WB);
        highQuality = intent.getBooleanExtra(LifeHelper.VOICE_HIGH, true);
        fileName = intent.getStringExtra(LifeHelper.VOICE_FILE_NAME);
        String filePath = recorder.getRecordDir() + "/" + fileName + ".amr";
        soundFile = new File(filePath);
        return super.onStartCommand(intent, flags, startId);
    }

    private void reallyStartRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioSamplingRate(highQuality ? 16000 : 8000);
        mediaRecorder.setOutputFormat(outputFileFormat);
        mediaRecorder.setAudioEncoder(highQuality ? MediaRecorder.AudioEncoder.AMR_WB
                : MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(soundFile.getAbsolutePath());
        mediaRecorder.setOnErrorListener(this);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            sendBroadcast(LifeHelper.RECORDER_STOP);
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            return;
        }
        mediaRecorder.start();
        startTime = System.currentTimeMillis();
        handler.post(timeRunnable);
        sendBroadcast(LifeHelper.RECORDER_START);

    }

    private void reallyStopRecorder() {
        staticReallyStopRecorder();
        sendBroadcast(LifeHelper.RECORDER_STOP);
    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        if (LifeHelper.RECORDER_START.equals(action)) {
            intent.putExtra(LifeHelper.RECORDER_START, startTime);
        } else {
            intent.putExtra("duration", duration);
            intent.putExtra("filepath", soundFile.getAbsolutePath());
        }
        sendBroadcast(intent);
    }


    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        if (mr != null) {

            try {
                mr.stop();
            } catch (RuntimeException e) {

            }
            mr.release();
            mr = null;
        }
        sendBroadcast(LifeHelper.RECORDER_STOP);
        stopSelf();
    }

    public static boolean isRecording() {
        return mediaRecorder != null;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void staticReallyStopRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.stop(); // **停止录音**
            mediaRecorder.release(); // **释放资源**
            mediaRecorder = null;
        }
        stopTime = System.currentTimeMillis();
        //TODO
        //录音文件时长；
        duration = stopTime - startTime;
        // sendStaticBroadcast(LifeHelper.RECORDER_STOP);
    }

    public static long getStaticDuration() {
        return duration;
    }

    public static String getStaticFilePath() {
        return soundFile.getAbsolutePath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class RecorderBinder extends Binder implements IRecorder {

        @Override
        public void startRecorder() {
            reallyStartRecorder();
        }

        @Override
        public void stopRecorder() {
            reallyStopRecorder();
        }
    }

}
