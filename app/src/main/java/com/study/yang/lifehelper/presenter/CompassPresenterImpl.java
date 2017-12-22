package com.study.yang.lifehelper.presenter;

import android.content.Context;

import com.study.yang.lifehelper.contract.CompassContract;
import com.study.yang.lifehelper.model.CompassModelImpl;


/**
 * Created by Administrator on 2017/03/24
 */

public class CompassPresenterImpl implements CompassContract.Presenter {

    private Context context;
    private CompassContract.View view;
    private final CompassModelImpl compassModel;

    public CompassPresenterImpl(Context context, CompassContract.View view) {
        this.context = context;
        this.view = view;
        compassModel = new CompassModelImpl(context);
    }

    @Override
    public String firstShowLaLong() {
        return compassModel.firstShowLaLong();
    }

    @Override
    public void registerProxy() {
        compassModel.registerProxy(view);
    }

    @Override
    public void getAddressByLaLong() {
        compassModel.getAddressByLaLong(view.getLa(),view.getLong(),new CompassContract.StringCallBack(){
            @Override
            public void success(String response) {
                    view.success(response);
            }

            @Override
            public void fail() {
                    view.fail();
            }
        });
    }

    public void onDestroy(){
        compassModel.onDestroy();
    }
}