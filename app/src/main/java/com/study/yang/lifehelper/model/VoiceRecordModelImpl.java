package com.study.yang.lifehelper.model;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;

import com.study.yang.lifehelper.VoiceActivity;
import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.bean.voice.Recorder;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.IRecorder;
import com.study.yang.lifehelper.contract.VoiceRecordContract;
import com.study.yang.lifehelper.db.Voice;
import com.study.yang.lifehelper.service.RecorderService;
import com.study.yang.lifehelper.utils.DialogUtils;
import com.study.yang.lifehelper.utils.ToastUtils;


/**
 * Created by Administrator on 2017/04/10
 */

public class VoiceRecordModelImpl implements VoiceRecordContract.Model {

    private VoiceActivity activity;
    private final SharedPreferences sp;
    private String fileName;
    private VoiceRecordContract.CallBack callBack;
    private String extension;
    private String recordFileType;
    private final Recorder recorder;
    private RecorderServiceConnection conn;
    private IRecorder iRecorder;
    private Intent serviceIntent;


    public VoiceRecordModelImpl(Activity activity) {
        this.activity = (VoiceActivity) activity;
        recorder = Recorder.getInstence();
        sp = activity.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void startRecord(String fileName, VoiceRecordContract.CallBack callBack) {
        this.fileName = fileName;
        this.callBack = callBack;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.showToast(activity, "内存卡不存在！");
            callBack.recordVoiceSuccess(false);
            return;
        }

        // 是否是高质量录音
        boolean isHighQuality = sp.getBoolean(LifeHelper.VOICE_HIGH, true);
        int outputfileformat = isHighQuality ? MediaRecorder.OutputFormat.AMR_WB : MediaRecorder.OutputFormat.AMR_NB;
        serviceIntent = new Intent(activity, RecorderService.class);
        serviceIntent.putExtra(LifeHelper.OUTPUT_FORMAT, outputfileformat);
        serviceIntent.putExtra(LifeHelper.VOICE_FILE_NAME, fileName);
        activity.startService(serviceIntent);
        conn = new RecorderServiceConnection();
        activity.bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);

    }


    @Override
    public void stopRecord() {
        if (iRecorder != null)
            iRecorder.stopRecorder();
        if (conn != null)
            activity.unbindService(conn);

        if (serviceIntent != null)
            activity.stopService(serviceIntent);

    }

    @Override
    public void startRecord() {
//            mRecorder.startRecording(outputfileformat, mFileNameEditText
//                            .getText().toString(), FILE_EXTENSION_AMR,
//         isHighQuality, mMaxFileSize);
        if (iRecorder != null)
            iRecorder.startRecorder();
    }

    @Override
    public void saveFileToDB(String fileName, long duration, String filePath, VoiceRecordContract.CallBack callBack) {
        Voice voice = new Voice(null, fileName, duration, filePath);
        long insert = LifeHelperApplication.voiceDao.insert(voice);
        if(insert<=0){
            callBack.recordVoiceFail();
        }else{
            callBack.recordVoiceSuccess(true);
        }
    }

    class RecorderServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iRecorder = (IRecorder) service;
            String isExistedFileName = fileName + ".amr";
            if (recorder.isRecordExisted(isExistedFileName)) {
                DialogUtils.checkFileNameIsRepeated(activity, VoiceRecordModelImpl.this);
            } else {
                startRecord();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
