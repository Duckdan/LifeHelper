package com.study.yang.lifehelper.presenter;


import com.study.yang.lifehelper.VoiceActivity;
import com.study.yang.lifehelper.contract.VoiceListContract;

/**
* Created by Administrator on 2017/04/17
*/

public class VoiceListPresenterImpl implements VoiceListContract.Presenter{

    private final VoiceActivity activity;
    private final VoiceListContract.View view;

    public VoiceListPresenterImpl(VoiceActivity activity, VoiceListContract.View view) {

        this.activity = activity;
        this.view = view;
    }
}