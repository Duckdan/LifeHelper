package com.study.yang.lifehelper.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.study.yang.lifehelper.DiaryActivity;
import com.study.yang.lifehelper.MainActivity;
import com.study.yang.lifehelper.ModifyPasswordActivity;
import com.study.yang.lifehelper.NoteActivity;
import com.study.yang.lifehelper.NoteCreateActivity;
import com.study.yang.lifehelper.ParitiesActivity;
import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.SetActivity;
import com.study.yang.lifehelper.VoiceActivity;
import com.study.yang.lifehelper.adapter.NoteRvBgAdapter;
import com.study.yang.lifehelper.app.LifeHelperApplication;
import com.study.yang.lifehelper.bean.voice.Recorder;
import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.db.Diary;
import com.study.yang.lifehelper.db.Note;
import com.study.yang.lifehelper.db.Voice;
import com.study.yang.lifehelper.model.VoiceRecordModelImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by zouqianyu on 2017/3/27.
 */

public class DialogUtils {
    private static boolean isPlaying = true;
    private static int playPosition = 0;
    private int mCurrentPosition;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    /**
     * 为了防止用户多次点击上一段
     */
    private boolean isClickPreEffective = true;

    /**
     * 为了防止用户多次点击下一段
     */
    private boolean isCLickNextEffective = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mCurrentPosition = mediaPlayer.getCurrentPosition();
                    mSkPosition.setProgress(mCurrentPosition);
                    mTvPosition.setText(DataFormatUtils.getFormatTime(mCurrentPosition));
                    break;
            }
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    };
    private SeekBar mSkPosition;
    private TextView mTvDuration;
    private TextView mTvPosition;

    public static void createDialog(final ParitiesActivity activity, final int dh) {
        final int index = getString(dh);
        if (index == -1) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View inflate = View.inflate(activity, R.layout.dialog_paritiews, null);
        builder.setView(inflate);
        final AlertDialog dialog = builder.create();
        Button btOriginal = (Button) inflate.findViewById(R.id.bt_original);
        Button btTarget = (Button) inflate.findViewById(R.id.bt_target);
        btOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.setTvOriginal(index);
            }
        });
        btTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.setTvTarget(index);
            }
        });
        dialog.show();
    }

    private static int getString(int dh) {
        String str = LifeHelper.HB[dh];
        if (str.contains("常用货币") || str.length() == 1) {
            return -1;
        } else {
            return dh;
        }
    }

    public static void createNoteBgDialog(final NoteCreateActivity noteCreateActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(noteCreateActivity);
        View inflate = View.inflate(noteCreateActivity, R.layout.item_note_create_dialog, null);
        RecyclerView rvNote = (RecyclerView) inflate.findViewById(R.id.rv_note);
        LinearLayoutManager layoutManager = new LinearLayoutManager(noteCreateActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvNote.setLayoutManager(layoutManager);
        NoteRvBgAdapter noteRvBgAdapter = new NoteRvBgAdapter(noteCreateActivity);
        builder.setView(inflate);
        final AlertDialog alertDialog = builder.create();
        noteRvBgAdapter.setOnItemViewClickListener(new NoteRvBgAdapter.OnItemViewClickListener() {
            @Override
            public void onClick(int colorId, int adapterPosition) {
                noteCreateActivity.setEditTextBg(colorId, adapterPosition);
                alertDialog.dismiss();
            }
        });
        rvNote.setAdapter(noteRvBgAdapter);
        alertDialog.show();
    }

    public static void showNoteTitleDialog(final NoteActivity noteActivity, final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
        View inflate = View.inflate(noteActivity, R.layout.item_note_title_dilog, null);
        final EditText etNote = (EditText) inflate.findViewById(R.id.et_note_title);
        Button btOk = (Button) inflate.findViewById(R.id.bt_ok);
        Button btCancle = (Button) inflate.findViewById(R.id.bt_cancel);
        String content = note.getTitle();
        if (!TextUtils.isEmpty(content)) {
            etNote.setText(content);
            etNote.setSelection(content.length());
        }
        builder.setView(inflate);
        final AlertDialog dialog = builder.create();
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etNote.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    title = "未命名";
                }
                note.setTitle(title);
                LifeHelperApplication.noteDao.update(note);
                dialog.dismiss();
                noteActivity.refreshLv(0, 0);
            }
        });

        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static void showNoteDelete(final NoteActivity noteActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(noteActivity);
        View inflate = View.inflate(noteActivity, R.layout.item_note_delete_dilog, null);
        TextView tvNoteDeleteMore = (TextView) inflate.findViewById(R.id.tv_note_delete_more);
        TextView tvNoteDeleteAll = (TextView) inflate.findViewById(R.id.tv_note_delete_all);
        builder.setView(inflate);
        final AlertDialog dialog = builder.create();
        tvNoteDeleteMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                noteActivity.refreshLv(1, 0); //多选
            }
        });

        tvNoteDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                noteActivity.refreshLv(1, 1); //全选
            }
        });
        dialog.show();
    }

    public static void showDialog(Context context, MainActivity mainActivity) {
        SharedPreferences sp = context.getSharedPreferences(LifeHelper.DIARY_SP_NAME,
                Context.MODE_PRIVATE);
        boolean isCreatePwd = sp.getBoolean(LifeHelper.DIARY_IS_CREATE_PWD, false);
        if (isCreatePwd) {
            showInputPwd(sp, mainActivity);
        } else {
            showSetPwd(sp, mainActivity);
        }
    }

    private static void showSetPwd(final SharedPreferences sp, final MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        View inflate = View.inflate(mainActivity, R.layout.item_diary_create_pwd_dialog, null);
        final EditText etDiaryPwd = (EditText) inflate.findViewById(R.id.et_diary_pwd);
        final EditText etDiaryPwdAgain = (EditText) inflate.findViewById(R.id.et_diary_pwd_again);
        final EditText etLuckNumber = (EditText) inflate.findViewById(R.id.et_luck_number);

        Button btOk = (Button) inflate.findViewById(R.id.bt_ok);
        Button btCancle = (Button) inflate.findViewById(R.id.bt_cancel);

        builder.setView(inflate);
        final AlertDialog dialog = builder.create();
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaryPwd = etDiaryPwd.getText().toString().trim();
                String diaryPwdAgain = etDiaryPwdAgain.getText().toString().trim();
                String luckNumber = etLuckNumber.getText().toString().trim();
                if (TextUtils.isEmpty(diaryPwd)) {
                    ToastUtils.showToast(mainActivity, "密码不能为空！");
                    return;
                }

                if (!diaryPwd.equals(diaryPwdAgain)) {
                    ToastUtils.showToast(mainActivity, "两次输入的密码不一致！");
                    return;
                }

                if (TextUtils.isEmpty(luckNumber)) {
                    ToastUtils.showToast(mainActivity, "幸运数不能为空！");
                    return;
                }

                sp.edit().putBoolean(LifeHelper.DIARY_IS_CREATE_PWD, true).commit();
                sp.edit().putString(LifeHelper.DIARY_PWD, PassWordUtils.getMD5(diaryPwd)).commit();
                sp.edit().putString(LifeHelper.DIARY_LUCK_NUMBER, PassWordUtils.getMD5(luckNumber)).commit();

                Intent intent = new Intent(mainActivity, DiaryActivity.class);
                mainActivity.startActivity(intent);
                dialog.dismiss();
            }
        });

        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static void showInputPwd(final SharedPreferences sp, final MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        View inflate = View.inflate(mainActivity, R.layout.item_diary_pwd_dialog, null);
        final EditText etDiaryPwd = (EditText) inflate.findViewById(R.id.et_diary_pwd);
        TextView tvForgetPwd = (TextView) inflate.findViewById(R.id.tv_forget_pwd);
        Button btOk = (Button) inflate.findViewById(R.id.bt_ok);
        Button btCancle = (Button) inflate.findViewById(R.id.bt_cancel);
        builder.setView(inflate);
        final AlertDialog dialog = builder.create();

        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, SetActivity.class);
                mainActivity.startActivity(intent);
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaryPwd = etDiaryPwd.getText().toString().trim();
                if (TextUtils.isEmpty(diaryPwd)) {
                    ToastUtils.showToast(mainActivity, "密码不能为空！");
                    return;
                }
                String encryptDiaryPwd = PassWordUtils.getMD5(diaryPwd);
                String pwd = sp.getString(LifeHelper.DIARY_PWD, "");
                if (encryptDiaryPwd.equals(pwd)) {
                    Intent intent = new Intent(mainActivity, DiaryActivity.class);
                    mainActivity.startActivity(intent);
                } else {
                    etDiaryPwd.setText("");
                    ToastUtils.showToast(mainActivity, "密码错误，请重试！");
                }
                dialog.dismiss();
            }
        });

        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showDeleteDialog(final DiaryActivity diaryActivity, final Diary diary) {
        AlertDialog.Builder builder = new AlertDialog.Builder(diaryActivity);
        View inflate = View.inflate(diaryActivity, R.layout.item_diary_delete_dialog, null);

        Button btOk = (Button) inflate.findViewById(R.id.bt_ok);
        Button btCancle = (Button) inflate.findViewById(R.id.bt_cancel);
        builder.setView(inflate);

        final AlertDialog dialog = builder.create();
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LifeHelperApplication.diaryDao.delete(diary);
                diaryActivity.refreshRv(diary);
                dialog.dismiss();
            }
        });

        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showSecurityDialog(final SetActivity setActivity) {
        SharedPreferences sp = setActivity.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        final String number = sp.getString("number", "");
        if (TextUtils.isEmpty(number)) {
            ToastUtils.showToast(setActivity, "当前没有设置密码，所以不存在重置密码！");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(setActivity);
        View view = View.inflate(setActivity, R.layout.item_set_security_dialog, null);
        final EditText etLuckNumber = (EditText) view.findViewById(R.id.et_luck_number);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String luckNumber = etLuckNumber.getText().toString().trim();
                if (TextUtils.isEmpty(luckNumber)) {
                    ToastUtils.showToast(setActivity, "内容不能为空！");
                    return;
                }

                String md5 = PassWordUtils.getMD5(luckNumber);
                //幸运数字
                if (number.equals(md5)) {
                    Intent intent = new Intent(setActivity, ModifyPasswordActivity.class);
                    setActivity.startActivity(intent);
                    alertDialog.dismiss();
                } else {
                    etLuckNumber.setText("");
                    ToastUtils.showToast(setActivity, "幸运数字与之前输入的值不符！");
                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    public static void showTipDialog(final SetActivity setActivity, String tipString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(setActivity);
        View view = View.inflate(setActivity, R.layout.item_set_tip_dialog, null);
        TextView tvTipContent = (TextView) view.findViewById(R.id.tv_tip_content);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvTipContent.setText(tipString);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = setActivity.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
                sp.edit().clear().commit();
                LifeHelperApplication.deleteDataFromDB();
                File file = new File(Recorder.getInstence().getRecordDir());
                FileUtils.delete(file);
                setActivity.refreshActivity();
                alertDialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    public static void checkFileNameIsRepeated(VoiceActivity activity, final VoiceRecordModelImpl voiceRecordModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.item_set_tip_dialog, null);
        TextView tvTipContent = (TextView) view.findViewById(R.id.tv_tip_content);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvTipContent.setText("文件名重复！");
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecordModel.startRecord();
                alertDialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void showPlayerDialog(final VoiceActivity activity, final List<Voice> list, final int position) {
        //当前被点击的条目
        final Voice voice = list.get(position);
        playPosition = position;
        Dialog dialog = new Dialog(activity, R.style.dialog);
        View view = View.inflate(activity, R.layout.item_voice_player_dialog, null);
        mSkPosition = (SeekBar) view.findViewById(R.id.sk_position);
        mTvDuration = (TextView) view.findViewById(R.id.tv_duration);
        mTvPosition = (TextView) view.findViewById(R.id.tv_position);
        final ImageView ivPause = (ImageView) view.findViewById(R.id.iv_pause);
        ImageView ivPre = (ImageView) view.findViewById(R.id.iv_pre);
        ImageView ivNext = (ImageView) view.findViewById(R.id.iv_next);

        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        final WindowManager manager = dialog.getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = display.getWidth();
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(voice.getFilepath());
            mediaPlayer.prepare();
            final int duration = (int) voice.getDuration();
            mTvDuration.setText(DataFormatUtils.getFormatTime(duration));
            mSkPosition.setMax(duration);
            mediaPlayer.start();
            mHandler.sendEmptyMessage(0);

            //当进度条的进度改变的时候
            mSkPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                        mTvPosition.setText(DataFormatUtils.getFormatTime(progress));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            ivPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickPreEffective) {
                        --playPosition;
                    }

                    if (playPosition < 0) {
                        ToastUtils.showToast(activity, "当前已经是第一段录音！");
                        isClickPreEffective = false;
                        playPosition = 0;
                    } else if (playPosition >= 0 && isClickPreEffective) {
                        Voice voicePre = list.get(playPosition);
                        // mHandler.removeMessages(0);
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(voicePre.getFilepath());
                            mediaPlayer.prepare();
                            int duration = (int) voicePre.getDuration();
                            mTvDuration.setText(DataFormatUtils.getFormatTime(duration));
                            mSkPosition.setMax(duration);
                            mediaPlayer.start();
                            mHandler.sendEmptyMessage(0);
                            /**
                             * 当播放的时候，显示暂停按钮，isPlaying = true 就是为了方便处理点击暂停之后的
                             *   业务逻辑
                             */
                            ivPause.setImageResource(R.drawable.video_pause_selector);
                            isPlaying = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    isCLickNextEffective = true;
                }
            });

            ivPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPlaying) {
                        ivPause.setImageResource(R.drawable.video_play_selector);
                        mediaPlayer.pause();
                        mHandler.removeMessages(0);
                    } else {
                        ivPause.setImageResource(R.drawable.video_pause_selector);
                        mediaPlayer.start();
                        mHandler.sendEmptyMessage(0);
                    }
                    isPlaying = isPlaying ? false : true;
                }
            });

            ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCLickNextEffective) {
                        ++playPosition;
                    }
                    if (playPosition >= list.size()) {
                        ToastUtils.showToast(activity, "当前已经是最后一段录音！");
                        playPosition = list.size() - 1;
                        isCLickNextEffective = false;
                    } else if (playPosition < list.size() && isCLickNextEffective) {
                        Voice voiceNext = list.get(playPosition);
                        //   mHandler.removeMessages(0);
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(voiceNext.getFilepath());
                            mediaPlayer.prepare();
                            int duration = (int) voiceNext.getDuration();
                            mTvDuration.setText(DataFormatUtils.getFormatTime(duration));
                            mSkPosition.setMax(duration);
                            mediaPlayer.start();
                            ivPause.setImageResource(R.drawable.video_pause_selector);
                            isPlaying = true;
                            mHandler.sendEmptyMessage(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    isClickPreEffective = true;
                }
            });

            //对话框消失的时候
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
                    mHandler.removeMessages(0);
                }
            });

            //播放完成的时候
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //  mp.release();
                    //   mp.reset();
                    ivPause.setImageResource(R.drawable.video_play_selector);
                    isPlaying = false;
                    mTvPosition.setText(mTvDuration.getText().toString().trim());
                    mHandler.removeMessages(0);
                }
            });
        } catch (IOException e) {
            ToastUtils.showToast(activity, "播放失败！");
            Log.d("VoiceFF", e.getMessage());
        }
    }
}
