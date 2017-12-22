package com.study.yang.lifehelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.yang.lifehelper.R;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by Administrator on 2017/3/30/030.
 */

public class NoteRvBgAdapter extends RecyclerView.Adapter<NoteRvBgAdapter.NoteRvBgHolder> {

    private Context context;
    private int[] colors = {R.color.note_book_incense, R.color.note_proguard_eye,
            R.color.note_night, R.color.note_parchment};
    private String[] colorNames = {"书香", "护眼", "夜间", "羊皮纸"};
    private OnItemViewClickListener listener;

    public NoteRvBgAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public NoteRvBgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_note_create_cardview, null);
        return new NoteRvBgHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NoteRvBgHolder holder, int position) {
        holder.ivNoteEtBg.setBackgroundResource(colors[position]);
        holder.tvNoteName.setText(colorNames[position]);
    }


    public void setOnItemViewClickListener(OnItemViewClickListener listener) {
        this.listener = listener;
    }

    class NoteRvBgHolder extends RecyclerView.ViewHolder {

        public ImageView ivNoteEtBg;
        public TextView tvNoteName;

        public NoteRvBgHolder(View itemView) {
            super(itemView);
            initView(itemView);
            AutoUtils.autoSize(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    listener.onClick(colors[adapterPosition],adapterPosition);
                }
            });
        }

        private void initView(View itemView) {
            ivNoteEtBg = (ImageView) itemView.findViewById(R.id.iv_note_et_bg);
            tvNoteName = (TextView) itemView.findViewById(R.id.tv_note_name);
        }
    }

    public interface OnItemViewClickListener {
        void onClick(int colorId, int position);
    }
}
