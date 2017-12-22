package com.study.yang.lifehelper.utils;

import android.content.Context;
import android.content.Intent;

import com.study.yang.lifehelper.db.Note;


/**
 * Created by Administrator on 2017/3/31/031.
 */

public class IntentUtils {
    public static void shareNonte(Note note, Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

//        String titleKey = context.getString(R.string.export_title);
//        String contentKey = context.getString(R.string.export_content);

        StringBuilder sb = new StringBuilder();
        sb.append("标题：").append(note.getTitle()).append(',');
        sb.append("内容：").append(note.getContent());

        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        intent.putExtra(intent.EXTRA_SUBJECT, "分享");
        context.startActivity(Intent.createChooser(intent,
                "分享" + ":" + note.getTitle()));
    }
}
