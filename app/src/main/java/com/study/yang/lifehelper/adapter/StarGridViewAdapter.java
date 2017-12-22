package com.study.yang.lifehelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.yang.lifehelper.R;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by zouqianyu on 2017/3/19.
 */
public class StarGridViewAdapter extends BaseAdapter {
    private int[] imgs = {R.drawable.aquarius, R.drawable.pisces, R.drawable.aries,
            R.drawable.taurus, R.drawable.gemini, R.drawable.cancer,
            R.drawable.leo, R.drawable.virgo, R.drawable.libra,
            R.drawable.scorpio, R.drawable.sagittarius, R.drawable.capricorn
    };

    private String[] constellationName = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

    private String[] constellationDate = {"1.20-2.18", "2.19-3.20", "3.21-4.19", "4.20-5.20", "5.21-6.21", "6.22-7.22",
            "7.23-8.22", "8.23-9.22", "9.23-10.23", "10.24-11.22", "11.23-12.21", "12.22-1.19"};
    private Context context;
    private ImageView ivPic;
    private TextView tvConstellation;
    private TextView tvTime;

    public StarGridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup group) {
        View inflate = View.inflate(context, R.layout.item_grid_view, null);
        AutoUtils.autoSize(inflate);
        ivPic = (ImageView) inflate.findViewById(R.id.iv_pic);
        tvConstellation = (TextView) inflate.findViewById(R.id.tv_constellation);
        tvTime = (TextView) inflate.findViewById(R.id.tv_time);

        ivPic.setBackgroundResource(imgs[position]);
        tvConstellation.setText(constellationName[position]);
        tvTime.setText(constellationDate[position]);
        return inflate;
    }
}
