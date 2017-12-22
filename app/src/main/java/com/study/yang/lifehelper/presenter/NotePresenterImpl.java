package com.study.yang.lifehelper.presenter;

import android.content.Context;


import com.study.yang.lifehelper.contract.NoteContract;
import com.study.yang.lifehelper.db.Note;
import com.study.yang.lifehelper.model.NoteModelImpl;

import java.util.List;

/**
* Created by Administrator on 2017/03/30
*/

public class NotePresenterImpl implements NoteContract.Presenter{

    private Context context;
    private NoteContract.View view;
    private final NoteModelImpl noteModel;

    public NotePresenterImpl(Context context, NoteContract.View view) {
        this.context = context;
        this.view = view;
        noteModel = new NoteModelImpl();
    }

    @Override
    public void createNewNote() {

    }

    @Override
    public void queryNoteFromDB() {
        List<Note> noteList = noteModel.queryNoteFromDB();
        view.setAdapter(noteList);
    }
}