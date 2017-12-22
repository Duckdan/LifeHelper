package com.study.yang.lifehelper.utils;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.study.yang.lifehelper.R;
import com.study.yang.lifehelper.app.LifeHelperApplication;


/**
 * Created by Administrator on 2017/4/10/010.
 */

public class VoiceUtils {
    public static ImageView getTimerImage(char number) {
        ImageView image = new ImageView(LifeHelperApplication.context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (number != ':') {
            image.setBackgroundResource(R.drawable.background_number);
        }
        switch (number) {
            case '0':
                image.setImageResource(R.drawable.number_0);
                break;
            case '1':
                image.setImageResource(R.drawable.number_1);
                break;
            case '2':
                image.setImageResource(R.drawable.number_2);
                break;
            case '3':
                image.setImageResource(R.drawable.number_3);
                break;
            case '4':
                image.setImageResource(R.drawable.number_4);
                break;
            case '5':
                image.setImageResource(R.drawable.number_5);
                break;
            case '6':
                image.setImageResource(R.drawable.number_6);
                break;
            case '7':
                image.setImageResource(R.drawable.number_7);
                break;
            case '8':
                image.setImageResource(R.drawable.number_8);
                break;
            case '9':
                image.setImageResource(R.drawable.number_9);
                break;
            case ':':
                image.setImageResource(R.drawable.colon);
                break;
        }
        image.setLayoutParams(lp);
        return image;
    }

}
