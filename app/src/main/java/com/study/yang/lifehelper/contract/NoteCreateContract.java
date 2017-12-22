package com.study.yang.lifehelper.contract;


import com.study.yang.lifehelper.db.Note;

/**
 * Created by Administrator on 2017/3/30/030.
 */

public class NoteCreateContract {

    public interface View {
        void showSnackBar();
        void showDialogToChooseColor();
        void setEditTextBg(int colorId, int adaprePosition);
        String getEditTextContent();
        void success();
        void fail();
    }

    public interface Presenter {
        void saveNoteToDB(String content, int position);
        void modifyNoteToDB(Note note, int position);
    }

    public interface Model {
        void saveNoteToDB(String content, int position, CallBack callBack);
        void modifyNoteToDB(Note note, int position, CallBack callBack);
    }

    public interface CallBack{
        void success();
        void fail();
    }

}