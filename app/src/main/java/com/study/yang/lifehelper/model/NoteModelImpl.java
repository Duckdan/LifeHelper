package com.study.yang.lifehelper.model;



import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.contract.NoteContract;
import com.study.yang.lifehelper.db.Note;
import com.study.yang.lifehelper.db.NoteDao;

import java.util.List;

/**
 * Created by Administrator on 2017/03/30
 */

public class NoteModelImpl implements NoteContract.Model {

    @Override
    public List<Note> queryNoteFromDB() {
        List<Note> noteList = LifeHelperApplication.noteDao.queryBuilder().orderDesc(NoteDao.Properties.OrderTime).build().list();
        return noteList;
    }
}