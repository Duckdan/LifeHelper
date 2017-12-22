package com.study.yang.lifehelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.study.yang.lifehelper.constant.LifeHelper;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by Administrator on 2017/3/27/027.
 */

public class ParitiesListAdapter extends BaseAdapter {

    private Context mContext;

    public ParitiesListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return LifeHelper.HB.length;
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
        TextView inflate = (TextView) View.inflate(mContext, android.R.layout.simple_list_item_1, null);
        AutoUtils.auto(inflate);
        inflate.setText(LifeHelper.HB[position]);
        return inflate;
    }
}
