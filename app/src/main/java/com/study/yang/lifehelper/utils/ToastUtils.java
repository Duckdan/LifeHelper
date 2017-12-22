package com.study.yang.lifehelper.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by Administrator on 2017/2/18/018.
 */

public class ToastUtils {
    private static Toast toast;
    public static void showToast(Context context, String text){
        if(toast == null){
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }
        toast.show();
    }
}
