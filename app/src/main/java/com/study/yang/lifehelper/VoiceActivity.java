package com.study.yang.lifehelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;


import com.study.yang.lifehelper.adapter.VoiceVpAdapter;
import com.study.yang.lifehelper.contract.VoiceContract;
import com.study.yang.lifehelper.fragment.VoiceListFragment;
import com.study.yang.lifehelper.fragment.VoiceRecordFragement;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoiceActivity extends BaseActivity implements VoiceContract.View {

    @Bind(R.id.rl_voice)
    RelativeLayout rlVoice;
    @Bind(R.id.vp_voice)
    ViewPager vpVoice;
    @Bind(R.id.rb_voice_0)
    RadioButton rbVoice0;
    @Bind(R.id.rb_voice_1)
    RadioButton rbVoice1;
    @Bind(R.id.rg_voice)
    RadioGroup rgVoice;
    private VoiceListFragment voiceListFragment;
    private VoiceRecordFragement voiceRecordFragement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);

        setAdapter();
        registerViewListener();
    }

    @Override
    public void setAdapter() {
        List<Fragment> list = new ArrayList<>();
        voiceRecordFragement = new VoiceRecordFragement();
        voiceListFragment = new VoiceListFragment();
        list.add(voiceRecordFragement);
        list.add(voiceListFragment);
        vpVoice.setAdapter(new VoiceVpAdapter(getSupportFragmentManager(), list));
    }

    @Override
    public void registerViewListener() {
        vpVoice.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        voiceRecordFragement.resetFileNameEditText();
                        rgVoice.check(R.id.rb_voice_0);
                        break;
                    case 1:
                        voiceListFragment.initDataProxy();
                        rgVoice.check(R.id.rb_voice_1);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rgVoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_voice_0:
                        voiceRecordFragement.resetFileNameEditText();
                        vpVoice.setCurrentItem(0);
                        break;
                    case R.id.rb_voice_1:
                        voiceListFragment.initDataProxy();
                        vpVoice.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    @Override
    public int getCurrentItemIndex() {
        return vpVoice.getCurrentItem();
    }

    @OnClick(R.id.rl_voice)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当界面销毁的时候，mediaPlayer必须释放
       // mediaPlayer.release();
    }
}
