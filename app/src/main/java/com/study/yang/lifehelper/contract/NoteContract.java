package com.study.yang.lifehelper.contract;

import android.app.Activity;
import android.content.Context;


import com.study.yang.lifehelper.db.Note;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30/030.
 */

public class NoteContract {

    public interface View {
        void jumpToNoteCreateActivity(Class<? extends Activity> clazz);
        void setAdapter(List<Note> noteList);
        void refreshLv(int mode, int deleteMode);

        /**
         * 取消删除
         */
        void cancelDelete();

        /**
         * 删除
         */
        void delete();
        /**
         * 分享
         */
        void shareNote(Note note, Context context);
    }

    public interface Presenter {
        void createNewNote();
        void queryNoteFromDB();
    }

    public interface Model {
        List<Note> queryNoteFromDB();
    }


}