package com.study.yang.lifehelper.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;


import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.VoiceActivity;
import com.study.yang.lifehelper.bean.voice.Recorder;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.VoiceRecordContract;
import com.study.yang.lifehelper.defineview.RecordNameEditText;
import com.study.yang.lifehelper.defineview.WheelImageView;
import com.study.yang.lifehelper.presenter.VoiceRecordPresenterImpl;
import com.study.yang.lifehelper.service.RecorderService;
import com.study.yang.lifehelper.utils.AnimationUtils;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.study.yang.lifehelper.utils.VoiceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.study.yang.lifehelper.constant.LifeHelper.FILE_EXTENSION_AMR;


/**
 * Created by Administrator on 2017/4/10/010.
 */

public class VoiceRecordFragement extends Fragment implements VoiceRecordContract.View {
    @Bind(R.id.time_calculator)
    LinearLayout timeCalculator;
    @Bind(R.id.wheel_left)
    WheelImageView wheelLeft;
    @Bind(R.id.wheel_right)
    WheelImageView wheelRight;
    @Bind(R.id.wheel_small_left)
    WheelImageView wheelSmallLeft;
    @Bind(R.id.wheel_small_right)
    WheelImageView wheelSmallRight;
    @Bind(R.id.file_name)
    RecordNameEditText fileName;
    @Bind(R.id.vumeter_layout)
    LinearLayout vumeterLayout;
    @Bind(R.id.bt_start_record)
    Button btStartRecord;
    @Bind(R.id.bt_stop_record)
    Button btStopRecord;

    String TAG = "VoiceRecord/LogUtils";

    private String mTimerFormat;
    private View view;
    private VoiceActivity activity;
    private VoiceRecordPresenterImpl voiceRecordPresenter;
    private final Handler mHandler = new Handler();
    private SharedPreferences sp;
    private AnimationUtils animationUtils;
    private Recorder recorder;
    private long orginalTime = 0;
    private RecorderReceiver receiver;
    private long startTime = 0;
    private long stopTime = 0;
    private boolean isStartRecorder = false;
    private Handler handler = new Handler();
    private boolean isRecording = false;
    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimerView();
        }
    };
    /**
     * 当失去焦点的时候，有关于时间的布局不在添加控件
     */
    private boolean aboutTimeCaculator = true;
    private boolean isUseStatic = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_record, null);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (getActivity().getCurrentFocus() != null && getActivity().getCurrentFocus().getWindowToken() != null) {
                            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    return false;
                }
            });
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initDataProxy();
        syncRecordService();
    }

    /**
     * 更服务同步
     */
    private void syncRecordService() {
        if (RecorderService.isRecording()) {
            fileName.setText(RecorderService.getFileName());
            fileName.setEnabled(true);
            isRecording = true;
            startTime = RecorderService.getStartTime();
            isUseStatic = true;
            updateUi(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        aboutTimeCaculator = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        aboutTimeCaculator = false;
        //   stopRecord();
    }

    /**
     * 页面配置文件
     */
    @Override
    public void initDataProxy() {
        activity = (VoiceActivity) getActivity();
        // 字符串的格式
        mTimerFormat = getResources().getString(R.string.timer_format);
        voiceRecordPresenter = new VoiceRecordPresenterImpl(activity, this);
        sp = activity.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        recorder = Recorder.getInstence();
        receiver = new RecorderReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LifeHelper.RECORDER_START);
        intentFilter.addAction(LifeHelper.RECORDER_STOP);
        activity.registerReceiver(receiver, intentFilter);
        if (animationUtils == null) {
            animationUtils = new AnimationUtils(wheelLeft, wheelRight, wheelSmallLeft, wheelSmallRight);
        }
        resetFileNameEditText();
        updateUi(false);
    }

    @OnClick({R.id.bt_start_record, R.id.bt_stop_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_record:
                if (isRecording) {
                    ToastUtils.showToast(activity, "正在录音，请停止录音之后再次点击...");
                    break;
                }
                ToastUtils.showToast(activity, "开始录音！");
                startRecord();
                break;
            case R.id.bt_stop_record:
                //stopAnimation();
                isRecording = false;
                ToastUtils.showToast(activity, "停止录音！");
                if (RecorderService.isRecording() && isUseStatic) {
                    RecorderService.staticReallyStopRecorder();
                    updateUi(false);
                    voiceRecordPresenter.saveFileToDB(RecorderService.getFileName(),
                            RecorderService.getStaticDuration(), RecorderService.getStaticFilePath());
                    break;
                }
                stopRecord();
                break;
        }
    }


    /**
     * 初始化录音文件名
     */
    @Override
    public void resetFileNameEditText() {
        String extension = FILE_EXTENSION_AMR;
        fileName.initFileName(recorder.getRecordDir(), extension, false);
    }

    @Override
    public void startRecord() {
        fileName.setEnabled(false);
        voiceRecordPresenter.startRecord();
    }

    @Override
    public void stopRecord() {
        fileName.setEnabled(true);
        resetFileNameEditText();
        voiceRecordPresenter.stopRecord();
    }

    @Override
    public String getFileName() {
        return fileName.getText().toString().trim();
    }

    @Override
    public void updateUi(boolean isStartAnimation) {
        if (isStartAnimation) {
            isStartRecorder = true;
            animationUtils.startAnimation();
        } else {
            isStartRecorder = false;
            animationUtils.stopAnimation();
        }
        updateTimerView();
    }

    /**
     * 更新界面头部的时间控件
     */
    private void updateTimerView() {
        if (isStartRecorder) {
            orginalTime = (System.currentTimeMillis() - startTime) / 1000;
        } else {
            orginalTime = 0;
        }
        String timeStr = String.format(mTimerFormat, orginalTime / 60, orginalTime % 60);
        if (aboutTimeCaculator) {
            timeCalculator.removeAllViews();
            for (int i = 0; i < timeStr.length(); i++) {
                timeCalculator.addView(VoiceUtils.getTimerImage(timeStr.charAt(i)));
            }
        }
        if (isStartRecorder) {
            handler.postDelayed(updateTimeRunnable, 500);
        } else {
            handler.removeCallbacks(updateTimeRunnable);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
        }
    }

    class RecorderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LifeHelper.RECORDER_START.equals(action)) {
                startTime = intent.getLongExtra(LifeHelper.RECORDER_START, System.currentTimeMillis());
                updateUi(true);
            } else if (LifeHelper.RECORDER_STOP.equals(action)) {
                long duration = intent.getLongExtra("duration", 0);
                String filepath = intent.getStringExtra("filepath");
                updateUi(false);
                if (duration == 0 || TextUtils.isEmpty(filepath)) {
                } else {
                    voiceRecordPresenter.saveFileToDB(RecorderService.getFileName(), duration, filepath);
                }
                //  LifeHelperApplication.voiceDao.insert()
            }
        }
    }
}
