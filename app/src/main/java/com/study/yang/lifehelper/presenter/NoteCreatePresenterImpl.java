package com.study.yang.lifehelper.presenter;

import android.content.Context;

import com.study.yang.lifehelper.contract.NoteCreateContract;
import com.study.yang.lifehelper.db.Note;
import com.study.yang.lifehelper.model.NoteCreateModelImpl;


/**
 * Created by Administrator on 2017/03/30
 */

public class NoteCreatePresenterImpl implements NoteCreateContract.Presenter {

    private Context context;
    private NoteCreateContract.View view;
    private final NoteCreateModelImpl noteCreateModel;

    public NoteCreatePresenterImpl(Context context, NoteCreateContract.View view) {

        this.context = context;
        this.view = view;
        noteCreateModel = new NoteCreateModelImpl();
    }

    @Override
    public void saveNoteToDB(final String content, int position) {
        noteCreateModel.saveNoteToDB(content, position, new NoteCreateContract.CallBack() {
            @Override
            public void success() {
                view.success();
            }

            @Override
            public void fail() {
                view.fail();
            }
        });
    }

    @Override
    public void modifyNoteToDB(Note note, int position) {
        noteCreateModel.modifyNoteToDB(note, position, new NoteCreateContract.CallBack() {
            @Override
            public void success() {
                view.success();
            }

            @Override
            public void fail() {

            }
        });
    }
}