package com.study.yang.lifehelper.bean.voice;


import android.text.TextUtils;


import com.study.yang.lifehelper.app.LifeHelperApplication;

import java.io.File;

/**
 * Created by Administrator on 2017/4/15/015.
 */

public class Recorder {
    private static final String SAMPLE_DEFAULT_DIR = "/LifeHelper/recorder";
    private static Recorder recorder;
    private final File sampleDir;

    private Recorder() {
        sampleDir = new File(LifeHelperApplication.context.getFilesDir().getAbsolutePath() + SAMPLE_DEFAULT_DIR);
    }

    public static synchronized Recorder getInstence() {
        if (recorder == null) {
            recorder = new Recorder();
        }
        return recorder;
    }

    public String getRecordDir() {
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        return sampleDir.getAbsolutePath();
    }

    public boolean isRecordExisted(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(sampleDir.getAbsolutePath() + "/" + path);
            return file.exists();
        }
        return false;
    }

    public void playerAudio(String filePath) {

    }


}
