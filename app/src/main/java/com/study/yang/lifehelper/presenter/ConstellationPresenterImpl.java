package com.study.yang.lifehelper.presenter;
import android.content.Context;

import com.study.yang.lifehelper.bean.constellation.DayInfo;
import com.study.yang.lifehelper.contract.ConstellationContract;
import com.study.yang.lifehelper.model.ConstellationModelImpl;

/**
* Created by Administrator on 2017/03/23
*/

public class ConstellationPresenterImpl implements ConstellationContract.Presenter{

    private Context context;
    private ConstellationContract.View view;
    private final ConstellationModelImpl constellationModel;

    public ConstellationPresenterImpl(Context context, ConstellationContract.View view) {
        this.context = context;
        this.view = view;
        constellationModel = new ConstellationModelImpl();
    }


    @Override
    public void queryConstellationInfo(String name) {
        view.showDialog();
        constellationModel.queryConstellationInfo(name, new ConstellationContract.CallBack<DayInfo>() {
            @Override
            public void success(DayInfo dayInfo) {
                view.dismissDialog();
                view.success(dayInfo);
            }

            @Override
            public void fail() {
                view.dismissDialog();
                view.fail();
            }
        });
    }
}