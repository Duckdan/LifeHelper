package com.study.yang.lifehelper.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;


import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.VoiceActivity;
import com.study.yang.lifehelper.adapter.VoiceListAdapter;
import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.bean.voice.Recorder;
import com.study.yang.lifehelper.contract.VoiceListContract;
import com.study.yang.lifehelper.db.Voice;
import com.study.yang.lifehelper.db.VoiceDao;
import com.study.yang.lifehelper.defineview.ListSlideView;
import com.study.yang.lifehelper.presenter.VoiceListPresenterImpl;
import com.study.yang.lifehelper.utils.DialogUtils;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/10/010.
 */

public class VoiceListFragment extends Fragment implements VoiceListContract.View {

    @Bind(R.id.lv_voice)
    ListSlideView lvVoice;
    @Bind(R.id.tv_list_tip)
    TextView tvListTip;
    private View view;
    private VoiceActivity activity;
    private VoiceListPresenterImpl voiceListPresenter;
    private String[] quertList = {VoiceDao.Properties.Id.columnName, VoiceDao.Properties.Filename.columnName,
            VoiceDao.Properties.Duration.columnName, VoiceDao.Properties.Filepath.columnName};
    private Recorder recorder;
    private VoiceListAdapter voiceListAdapter;
    private List<Voice> list;
    private Dialog dialog;
    private DialogUtils mDialogUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_list, null);
            ButterKnife.bind(this, view);
            activity = (VoiceActivity) getActivity();
            voiceListPresenter = new VoiceListPresenterImpl(activity, this);
            mDialogUtils = new DialogUtils();
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initDataProxy();
    }

    @Override
    public void initDataProxy() {
        list = LifeHelperApplication.voiceDao.queryBuilder().build().list();
        recorder = Recorder.getInstence();
        if (list.size() == 0) {
            tvListTip.setVisibility(View.VISIBLE);
            lvVoice.setVisibility(View.GONE);
        } else {
            tvListTip.setVisibility(View.GONE);
            lvVoice.setVisibility(View.VISIBLE);
            if (voiceListAdapter == null) {
                voiceListAdapter = new VoiceListAdapter(activity, list);
                registerAboutListListener();
                lvVoice.setAdapter(voiceListAdapter);
            } else {
                voiceListAdapter.setListData(list);
                voiceListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 注册关于ListView的点击事件
     */
    private void registerAboutListListener() {
        voiceListAdapter.setOnRemoveItemListener(new VoiceListAdapter.OnRemoveItemListener() {
            @Override
            public void onRemoveItem(Voice voice) {
                if (list != null) {
                    String filepath = voice.getFilepath();
                    File file = new File(filepath);
                    if (file.exists()) {
                        file.delete();
                    }
                    LifeHelperApplication.voiceDao.delete(voice);
                    list.remove(voice);
                    voiceListAdapter.notifyDataSetChanged();
                }

                if (list.size() == 0) { //当集合被删除完的时候，更新UI
                    lvVoice.setVisibility(View.GONE);
                    tvListTip.setVisibility(View.VISIBLE);
                } else {
                    lvVoice.slideBack();
                }

            }
        });

        lvVoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mDialogUtils.showPlayerDialog(activity, list, position);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialogUtils != null) {
            mDialogUtils.mediaPlayer.release();
        }
    }
}
