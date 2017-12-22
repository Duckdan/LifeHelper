package com.study.yang.lifehelper.presenter;

import android.app.Activity;

import com.study.yang.lifehelper.contract.VoiceRecordContract;
import com.study.yang.lifehelper.model.VoiceRecordModelImpl;


/**
* Created by Administrator on 2017/04/10
*/

public class VoiceRecordPresenterImpl implements VoiceRecordContract.Presenter{

    private final Activity activity;
    private final VoiceRecordContract.View view;
    private final VoiceRecordModelImpl voiceRecordModel;

    public VoiceRecordPresenterImpl(Activity activity, VoiceRecordContract.View view) {
        this.activity = activity;
        this.view = view;
        voiceRecordModel = new VoiceRecordModelImpl(activity);
    }

    @Override
    public void startRecord() {
        voiceRecordModel.startRecord(view.getFileName(),new VoiceRecordContract.CallBack(){
            @Override
            public void recordVoiceSuccess(boolean response) {
                view.updateUi(response);
            }

            @Override
            public void recordVoiceFail() {
                view.updateUi(false);
            }
        });
    }

    @Override
    public void stopRecord() {
        voiceRecordModel.stopRecord();
    }

    @Override
    public void saveFileToDB(String fileName,long duration,String filePath) {
        voiceRecordModel.saveFileToDB(fileName,duration,filePath,new VoiceRecordContract.CallBack(){
            @Override
            public void recordVoiceSuccess(boolean response) {

            }

            @Override
            public void recordVoiceFail() {

            }
        });
    }
}