package com.study.yang.lifehelper.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.study.yang.lifehelper.constant.LifeHelper;
import com.study.yang.lifehelper.contract.ModifyPasswordContract;
import com.study.yang.lifehelper.utils.PassWordUtils;
import com.study.yang.lifehelper.utils.ToastUtils;


/**
 * Created by Administrator on 2017/04/12
 */

public class ModifyPasswordModelImpl implements ModifyPasswordContract.Model {

    private Context context;

    public ModifyPasswordModelImpl(Context context) {

        this.context = context;
    }

    @Override
    public void modifyPassword(String newPwd, String againPwd, ModifyPasswordContract.CallBack callBack) {
        if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(againPwd)) {
            callBack.fail();
            return;
        }

        if (!newPwd.equals(againPwd)) {
            callBack.fail();
            return;
        }

        SharedPreferences sp = context.getSharedPreferences(LifeHelper.DIARY_SP_NAME, Context.MODE_PRIVATE);
        String pwdMd5 = PassWordUtils.getMD5(newPwd);
        boolean commit = sp.edit().putString(LifeHelper.DIARY_PWD, pwdMd5).commit();
        if (commit) {
            callBack.success(pwdMd5);
        } else {
            callBack.fail();
        }

    }
}