package com.study.yang.lifehelper.model;


import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.contract.NoteCreateContract;
import com.study.yang.lifehelper.db.Note;
import com.study.yang.lifehelper.utils.DataFormatUtils;

/**
 * Created by Administrator on 2017/03/30
 */

public class NoteCreateModelImpl implements NoteCreateContract.Model {

    @Override
    public void saveNoteToDB(String content, int position, NoteCreateContract.CallBack callBack) {
        Note note = new Note();
        note.setTitle("未命名");
        note.setContent(content);
        note.setColorType(position + "");
        note.setOrderTime(System.currentTimeMillis());
        note.setSaveTime(DataFormatUtils.getFormatDate(System.currentTimeMillis()));
        note.setDeleteType(0);
        long insert = LifeHelperApplication.noteDao.insert(note);
        if (insert > 0) {
            callBack.success();
        } else {
            callBack.fail();
        }
    }

    @Override
    public void modifyNoteToDB(Note note, int position, NoteCreateContract.CallBack callBack){
        note.setColorType(position + "");
        note.setOrderTime(System.currentTimeMillis());
        note.setSaveTime(DataFormatUtils.getFormatDate(System.currentTimeMillis()));
        note.setDeleteType(0);
        LifeHelperApplication.noteDao.update(note);
      //  noteDao.updateInTx();
        callBack.success();
    }
}