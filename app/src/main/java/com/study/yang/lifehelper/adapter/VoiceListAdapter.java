package com.study.yang.lifehelper.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.VoiceActivity;
import com.study.yang.lifehelper.db.Voice;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17/017.
 */

public class VoiceListAdapter extends BaseAdapter {

    private final String mTimerFormat;
    private VoiceActivity activity;
    private List<Voice> list;
    private OnRemoveItemListener listener;

    public VoiceListAdapter(VoiceActivity activity, List<Voice> list) {
        this.activity = activity;
        this.list = list;
        mTimerFormat = activity.getResources().getString(R.string.timer_format);
    }

    public void setListData(List<Voice> list) {
        this.list = list;
    }

    public void setOnRemoveItemListener(OnRemoveItemListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_voice_list_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Voice voice = list.get(position);
        viewHolder.tvRecorderName.setText(voice.getFilename());
        Long duration = voice.getDuration() / 1000;
        String durationStr = String.format(mTimerFormat, duration / 60, duration % 60);
        viewHolder.tvReorderTime.setText(durationStr);
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveItem(voice);
            }
        });
        return convertView;
    }


    class ViewHolder {

        public TextView tvRecorderName;
        public TextView tvReorderTime;
        private ImageView ivDelete;

        public ViewHolder(View convertView) {
            initView(convertView);
            AutoUtils.autoSize(convertView);
        }

        private void initView(View convertView) {
            tvRecorderName = (TextView) convertView.findViewById(R.id.tv_recorder_name);
            tvReorderTime = (TextView) convertView.findViewById(R.id.tv_recorder_time);
            ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
        }
    }

    public interface OnRemoveItemListener {
        void onRemoveItem(Voice voice);
    }
}
