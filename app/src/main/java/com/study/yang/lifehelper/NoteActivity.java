package com.study.yang.lifehelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.study.yang.lifehelper.adapter.NoteListAdapter;
import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.contract.NoteContract;
import com.study.yang.lifehelper.db.Note;
import com.study.yang.lifehelper.presenter.NotePresenterImpl;
import com.study.yang.lifehelper.utils.DialogUtils;
import com.study.yang.lifehelper.utils.IntentUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteActivity extends BaseActivity implements NoteContract.View {

    @Bind(R.id.rl_note)
    RelativeLayout rlNote;
    @Bind(R.id.ll_add_note)
    LinearLayout llAddNote;
    @Bind(R.id.lv_note)
    ListView lvNote;
    @Bind(R.id.rl_note_first)
    RelativeLayout rlNoteFirst;
    @Bind(R.id.tv_cancel_delete)
    TextView tvCancelDelete;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.fl_delete)
    FrameLayout flDelete;
    private NotePresenterImpl notePresenter;
    private NoteListAdapter mAdapter;
    private List<Note> noteList = new ArrayList<>();
    private float touchX;
    private float touchY;
    private PopupWindow popupWindow;
    private TextView tvNoteTitle;
    private TextView tvNoteDelte;
    private TextView tvNoteAlarm;
    private TextView tvNoteShare;
    //判断当前页面是不是正常模式
    private boolean currentPageMode = true;
    //删除模式，1代表全部删除
    private int deleteMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        notePresenter = new NotePresenterImpl(this, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        notePresenter.queryNoteFromDB();
    }

    @OnClick({R.id.rl_note, R.id.ll_add_note, R.id.tv_cancel_delete, R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_note:
                finish();
                break;
            case R.id.ll_add_note:
                jumpToNoteCreateActivity(NoteCreateActivity.class);
                break;
            case R.id.tv_cancel_delete:
                cancelDelete();
                break;
            case R.id.tv_delete:
                delete();
                break;
        }
    }

    @Override
    public void jumpToNoteCreateActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void setAdapter(List<Note> noteList) {
        if (noteList != null) {
            this.noteList.clear();
            this.noteList.addAll(noteList);
            Log.d("TAG/NoteActivity", this.noteList.toString());
            if (mAdapter == null) {
                mAdapter = new NoteListAdapter(this, this.noteList);
                lvNote.setAdapter(mAdapter);
            } else {
                mAdapter.setNoteList(this.noteList);
                mAdapter.notifyDataSetChanged();
            }

            aboutLvNoteListenerProxy();
        }
    }

    @Override
    public void refreshLv(int mode, int deleteMode) {
        if (mAdapter != null) {
            if (mode == 1) {
                this.deleteMode = deleteMode;
                mAdapter.setIsShowCheckBox(true);
                flDelete.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.VISIBLE);
                tvCancelDelete.setVisibility(View.VISIBLE);
                if (deleteMode == 1) {
                    for (Note note : noteList) {
                        note.setDeleteType(1);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void cancelDelete() {
        if (mAdapter != null) {
            mAdapter.setIsShowCheckBox(false);
            deleteMode = 0;
            currentPageMode = true;
            flDelete.setVisibility(View.GONE);
            tvCancelDelete.setVisibility(View.GONE);
            tvDelete.setVisibility(View.GONE);
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                note.setDeleteType(0);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void delete() {
        if (deleteMode == 1) {
            LifeHelperApplication.noteDao.deleteAll();
            this.noteList.clear();
            // mAdapter.notifyDataSetChanged();
        } else {
            List<Note> list = new ArrayList<>();
            list.addAll(noteList);
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (note.getDeleteType() == 1) {
                    LifeHelperApplication.noteDao.delete(note);
                    list.remove(note);
                }
            }
            noteList.clear();
            noteList.addAll(list);
            //  mAdapter.notifyDataSetChanged();
        }
        cancelDelete();
    }

    @Override
    public void shareNote(Note note, Context context) {
        IntentUtils.shareNonte(note, context);
    }

    /**
     * 关于列表条目的配置
     */
    private void aboutLvNoteListenerProxy() {
        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = NoteActivity.this.noteList.get(position);
                if (currentPageMode) {
                    Intent intent = new Intent(NoteActivity.this, NoteCreateActivity.class);
//                    intent.putExtra("id", note.getId());
//                    intent.putExtra("content", note.getContent());
//                    intent.putExtra("colorType", note.getColorType());
                    intent.putExtra("note", note);
                    startActivity(intent);
                } else {
                    CheckBox cbDelete = (CheckBox) view.findViewById(R.id.cb_delete);
                    if (cbDelete.isChecked()) {
                        cbDelete.setChecked(false);
                        note.setDeleteType(0);
                    } else {
                        cbDelete.setChecked(true);
                        note.setDeleteType(1);
                    }

                }
            }
        });

        lvNote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // if (event.getAction() == MotionEvent.ACTION_DOWN) {
                touchX = event.getRawX();
                touchY = event.getRawY();
                //   }
                return false;
            }
        });


        lvNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //ToastUtils.showToast(NoteActivity.this, "被点了！");
                popupWindow = new PopupWindow(NoteActivity.this);
                popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setFocusable(true);
                View inflate = View.inflate(NoteActivity.this, R.layout.item_popup_window, null);
                aboutPopupWindowViewProxy(inflate, position);
                popupWindow.setContentView(inflate);
                //WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                //systemService.getDefaultDisplay().
                //获取状态栏的高度
                Rect rectangle = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
                float height = touchY - rectangle.top - 100;
                popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, (int) touchX, (int) touchY);
                Log.d("TAG/NoteActivity", height + "===touchY" + touchY);
                Animation animation = AnimationUtils.loadAnimation(NoteActivity.this, R.anim.popup_anim);
                inflate.startAnimation(animation);
                return true;
            }
        });
    }

    /**
     * 关于popupWindow里面填充的view的配置
     *
     * @param view
     * @param position
     */
    private void aboutPopupWindowViewProxy(View view, final int position) {
        tvNoteTitle = (TextView) view.findViewById(R.id.tv_note_title);
        tvNoteDelte = (TextView) view.findViewById(R.id.tv_note_delte);
        tvNoteShare = (TextView) view.findViewById(R.id.tv_note_share);

        tvNoteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = NoteActivity.this.noteList.get(position);
                DialogUtils.showNoteTitleDialog(NoteActivity.this, note);
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        tvNoteDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = NoteActivity.this.noteList.get(position);
                note.setDeleteType(1);
                currentPageMode = false;
                DialogUtils.showNoteDelete(NoteActivity.this);
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        tvNoteShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = NoteActivity.this.noteList.get(position);
                shareNote(note, NoteActivity.this);
            }
        });
    }
}
