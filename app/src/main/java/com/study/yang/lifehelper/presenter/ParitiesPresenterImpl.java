package com.study.yang.lifehelper.presenter;

import android.content.Context;

import com.study.yang.lifehelper.contract.ParitiesContract;
import com.study.yang.lifehelper.model.ParitiesModelImpl;


/**
 * Created by Administrator on 2017/03/27
 */

public class ParitiesPresenterImpl implements ParitiesContract.Presenter {

    private Context context;
    private ParitiesContract.View view;
    private final ParitiesModelImpl paritiesModel;

    public ParitiesPresenterImpl(Context context, ParitiesContract.View view) {
        this.context = context;
        this.view = view;
        paritiesModel = new ParitiesModelImpl(context);
    }

    @Override
    public void showChangeResult() {
        paritiesModel.showChangeResult(view.getOriginalCode(), view.getNumber(), view.getTargetCode(),
                new ParitiesContract.CallBack() {
            @Override
            public void success(String response) {
                view.successResult(response);
            }

            @Override
            public void fail() {
                view.failResult();
            }
        });
    }

    @Override
    public void showFirstRmbToUsa() {
        paritiesModel.showFirstRmbToUsa(new ParitiesContract.CallBack() {
            @Override
            public void success(String response) {
                view.successRmbToUsa(response);
            }

            @Override
            public void fail() {
                view.failRmbToUsa();
            }
        });
    }

    @Override
    public void showFirstUsaToRmb() {
        paritiesModel.showFirstUsaToRmb(new ParitiesContract.CallBack() {
            @Override
            public void success(String response) {
                view.successUsaToRmb(response);
            }

            @Override
            public void fail() {
                view.failUsaToRmb();
            }
        });
    }
}