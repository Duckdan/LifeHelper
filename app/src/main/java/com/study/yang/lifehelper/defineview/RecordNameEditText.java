/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.study.yang.lifehelper.defineview;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.study.yang.lifehelper.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecordNameEditText extends EditText {

    private Context mContext;

    private InputMethodManager mInputMethodManager;


    private String mDir;

    private String mExtension;



    public RecordNameEditText(Context context) {
        super(context, null);
        init(context);

    }

    public RecordNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordNameEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mInputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    public void initFileName(String dir, String extension, boolean englishOnly) {
        mDir = dir;
        mExtension = extension;

        // 默认使用中文
        if (!englishOnly) {
            setText(getProperFileName(mContext.getString(R.string.default_record_name)));
        } else {
            SimpleDateFormat dataFormat = new SimpleDateFormat("MMddHHmmss");
            setText(getProperFileName("rec_" + dataFormat.format(Calendar.getInstance().getTime())));
        }
    }

    //判断存储路径下是否存在该文件，从而达到初始化的目的
    private String getProperFileName(String name) {
        String uniqueName = name;

        if (isFileExisted(uniqueName)) {
            int i = 2;
            while (true) {
                String temp = uniqueName + "(" + i + ")";
                if (!isFileExisted(temp)) {
                    uniqueName = temp;
                    break;
                }
                i++;
            }
        }
        return uniqueName;
    }

    private boolean isFileExisted(String name) {
        String fullName = mDir + "/" + name.trim() + mExtension;
        File file = new File(fullName);
        return file.exists();
    }




}
