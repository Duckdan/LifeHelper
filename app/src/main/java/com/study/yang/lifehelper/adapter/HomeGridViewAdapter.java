package com.study.yang.lifehelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.yang.lifehelper.MainActivity;
import com.study.yang.lifehelper.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by zouqianyu on 2017/3/20.
 */

public class HomeGridViewAdapter extends BaseAdapter {
    private int[] imgs = {R.drawable.bookb , R.drawable.star, R.drawable.huilv,
            R.drawable.weather, R.drawable.compass, R.drawable.voice,
            R.drawable.phone, R.drawable.note, R.drawable.set};
    private String[] titles = {"抒发心声", "今日运势", "汇率兑换", "天气预报", "东南西北", "欢声笑语",
            "号码归属", "要事记录", "系统设置"};
    private Context context;

    public HomeGridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 9;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_grid_layout, null);
        AutoUtils.autoSize(view);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        iv.setBackgroundResource(imgs[position]);
        tv.setText(titles[position]);
        return view;
    }
}
