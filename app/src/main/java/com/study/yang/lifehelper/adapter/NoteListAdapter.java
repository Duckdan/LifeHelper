package com.study.yang.lifehelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.db.Note;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30/030.
 */

public class NoteListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Note> noteList;
    private int[] bgs = {R.drawable.item_lv_note_book_incense_shape, R.drawable.item_lv_note_proguard_eye_shape,
            R.drawable.item_lv_note_night_shape, R.drawable.item_lv_note_parchment_shape};
    private int index = 0;

    private boolean isShowCheckBox = false;

    public NoteListAdapter(Context context, List<Note> noteList) {
        mContext = context;
        this.noteList = noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public void setIsShowCheckBox(boolean mode){
        isShowCheckBox = mode;
    }
    @Override
    public int getCount() {
        return noteList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        //  View inflate = null;
        NoteLvHolder noteLvHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_lv_note, null);
            noteLvHolder = new NoteLvHolder(convertView);
            convertView.setTag(noteLvHolder);
        } else {
            // inflate = convertView;
            noteLvHolder = (NoteLvHolder) convertView.getTag();
        }
        Note note = noteList.get(position);
        String title = note.getTitle();
        if (TextUtils.isEmpty(title)) {
            noteLvHolder.mTvNoteTitle.setText("未命名");
        } else {
            noteLvHolder.mTvNoteTitle.setText(title);
        }

        noteLvHolder.mTvNoteTime.setText(note.getSaveTime());
        noteLvHolder.mTvNoteContent.setText(note.getContent());

        String type = note.getColorType();
        if (type != null) {
            index = Integer.parseInt(type);
        } else {
            index = 0;
        }
        if (note.getDeleteType() == 0) {
            noteLvHolder.cbDelete.setChecked(false);
        } else if (note.getDeleteType() == 1) { //删除
            noteLvHolder.cbDelete.setChecked(true);
        }
        if (isShowCheckBox){
            noteLvHolder.cbDelete.setVisibility(View.VISIBLE);
        }else {
            noteLvHolder.cbDelete.setVisibility(View.GONE);
        }

        noteLvHolder.mLlItemLvBg.setBackgroundResource(bgs[index]);
        return convertView;
    }

    class NoteLvHolder {
        View view;
        public TextView mTvNoteTitle;
        public TextView mTvNoteTime;
        public TextView mTvNoteContent;
        public LinearLayout mLlItemLvBg;
        public CheckBox cbDelete;

        public NoteLvHolder(View view) {
            this.view = view;
            initView(view);
            AutoUtils.autoSize(view);
        }

        private void initView(View view) {
            mTvNoteTitle = (TextView) view.findViewById(R.id.tv_note_title);
            mTvNoteTime = (TextView) view.findViewById(R.id.tv_note_time);
            mTvNoteContent = (TextView) view.findViewById(R.id.tv_note_content);
            mLlItemLvBg = (LinearLayout) view.findViewById(R.id.ll_item_lv_bg);
            cbDelete = (CheckBox) view.findViewById(R.id.cb_delete);
        }
    }
}
