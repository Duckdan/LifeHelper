package com.study.yang.lifehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.db.Diary;
import com.study.yang.lifehelper.utils.DataFormatUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5/005.
 */

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.DiaryViewHolder> {

    private Context context;
    private List<Diary> response;
    private OnDiaryItemClickListener listener;
    private OnDiaryItemLongClickListener longClickListener;

    public DiaryListAdapter(Context context, List<Diary> response) {
        this.context = context;
        this.response = response;
    }

    public void setData(List<Diary> response) {
        this.response = response;
    }

    public void getData(Diary diary) {
        response.remove(diary);
    }

    @Override
    public int getItemCount() {
        return response.size();
    }


    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_diary_rv, null);
        AutoUtils.autoSize(inflate);
        return new DiaryViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        Diary diary = response.get(position);
        holder.tvDiaryTitle.setText(diary.getTitle());
        String formatDate = DataFormatUtils.getFormatDate(diary.getSavetime());
        holder.tvDiaryTime.setText(formatDate);
    }

    public void setOnDiaryItemClickListener(OnDiaryItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDiaryItemLongClickListener(OnDiaryItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDiaryTitle;
        private TextView tvDiaryTime;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            initView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onClick(response.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    longClickListener.onLongClick(response.get(position));
                    return true;
                }
            });
        }

        private void initView(View itemView) {
            tvDiaryTitle = (TextView) itemView.findViewById(R.id.tv_diary_title);
            tvDiaryTime = (TextView) itemView.findViewById(R.id.tv_diary_time);
        }
    }

    public interface OnDiaryItemClickListener {
        void onClick(Diary diary);
    }

    public interface OnDiaryItemLongClickListener {
        void onLongClick(Diary diary);
    }
}
