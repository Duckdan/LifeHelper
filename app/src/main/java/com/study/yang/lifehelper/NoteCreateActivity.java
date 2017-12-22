package com.study.yang.lifehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.study.yang.lifehelper.contract.NoteCreateContract;
import com.study.yang.lifehelper.db.Note;
import com.study.yang.lifehelper.presenter.NoteCreatePresenterImpl;
import com.study.yang.lifehelper.utils.DialogUtils;
import com.study.yang.lifehelper.utils.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/30/030.
 */
public class NoteCreateActivity extends BaseActivity implements NoteCreateContract.View {
    @Bind(R.id.rl_note)
    RelativeLayout rlNote;
    @Bind(R.id.iv_note_save)
    ImageView ivNoteSave;
    @Bind(R.id.note_fab_search)
    FloatingActionButton noteFabSearch;
    @Bind(R.id.cl_note_create)
    CoordinatorLayout clNoteCreate;
    @Bind(R.id.et_note)
    EditText etNote;
    private NoteCreatePresenterImpl noteCreatePresenter;
    private boolean isIvNoteSave = false;
    private int mEtColorBgType = 0;

    private int[] colors = {R.color.note_book_incense, R.color.note_proguard_eye,
            R.color.note_night, R.color.note_parchment};
    private long mId = -1;
    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        ButterKnife.bind(this);
        noteCreatePresenter = new NoteCreatePresenterImpl(this, this);
        aboutIntentProxy();
    }

    private void aboutIntentProxy() {
        Intent intent = getIntent();
        if (intent != null) {
            note = (Note) intent.getSerializableExtra("note");
            if(note !=null) {
                mId = note.getId();
                String content = note.getContent();
                String colorType = note.getColorType();
                if (mId != -1) {
                    etNote.setText(content);
                    etNote.setSelection(content.length());

                    if (colorType != null) {
                        mEtColorBgType = Integer.parseInt(colorType);
                    } else {
                        mEtColorBgType = 0;
                    }
                    etNote.setBackgroundResource(colors[mEtColorBgType]);
                }
            }
        }
    }

    @OnClick({R.id.rl_note, R.id.iv_note_save, R.id.note_fab_search})
    public void onClick(View view) {
        String editTextContent = getEditTextContent();
        switch (view.getId()) {
            case R.id.rl_note:
                if (TextUtils.isEmpty(editTextContent)) {
                    return;
                }
                if (mId != -1 && !isIvNoteSave) {
                    note.setContent(editTextContent);
                    noteCreatePresenter.modifyNoteToDB(note, mEtColorBgType);
                    finish();
                    return;
                }

                if (!isIvNoteSave) {
                    noteCreatePresenter.saveNoteToDB(editTextContent, mEtColorBgType);
                }
                finish();
                break;
            case R.id.iv_note_save:
                if (TextUtils.isEmpty(editTextContent)) {
                    ToastUtils.showToast(this, "便签内容为空，不能保存！");
                    return;
                }
                if (mId != -1) {
                    note.setContent(editTextContent);
                    noteCreatePresenter.modifyNoteToDB(note, mEtColorBgType);
                    //finish();
                    isIvNoteSave = true;
                    break;
                }
                isIvNoteSave = true;
                noteCreatePresenter.saveNoteToDB(editTextContent, mEtColorBgType);
                break;
            case R.id.note_fab_search:
                showSnackBar();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String editTextContent = getEditTextContent();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtils.isEmpty(editTextContent)) {
                return super.onKeyDown(keyCode, event);
            }
            if (mId != -1 && !isIvNoteSave) {
                note.setContent(editTextContent);
                noteCreatePresenter.modifyNoteToDB(note, mEtColorBgType);
                //finish();
                return super.onKeyDown(keyCode, event);
            }

            if (!isIvNoteSave) {
                noteCreatePresenter.saveNoteToDB(editTextContent, mEtColorBgType);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showSnackBar() {
        Snackbar.
                make(clNoteCreate, "设置背景颜色", Snackbar.LENGTH_SHORT).
                setAction("点击我", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogToChooseColor();
                    }
                }).show();
    }

    @Override
    public void showDialogToChooseColor() {
        DialogUtils.createNoteBgDialog(this);
    }

    @Override
    public void setEditTextBg(int colorId, int adaprePosition) {
        mEtColorBgType = adaprePosition;
        etNote.setBackgroundResource(colorId);
    }

    @Override
    public String getEditTextContent() {
        return etNote.getText().toString().trim();
    }

    @Override
    public void success() {
        ToastUtils.showToast(this, "保存成功！");
    }

    @Override
    public void fail() {
        ToastUtils.showToast(this, "保存失败！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isIvNoteSave = false;
    }
}
