package com.study.yang.lifehelper.contract;

import com.study.yang.lifehelper.bean.parities.ParitiesResultBean;



import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/27/027.
 */

public interface ParitiesContract {

    public interface View {
        void showRmbToUsa();

        void successRmbToUsa(String response);

        void failRmbToUsa();

        void successUsaToRmb(String response);

        void failUsaToRmb();

        void successResult(String response);

        void failResult();

        String getOriginalCode();

        String getTargetCode();

        String getNumber();



        void showMyDialog(int position);

        void setTvOriginal(int original);
        void setTvTarget(int target);
    }

    public interface Presenter {
        void showChangeResult();

        void showFirstRmbToUsa();

        void showFirstUsaToRmb();
    }

    public interface Model {
        void showFirstRmbToUsa(CallBack callBack);

        void showFirstUsaToRmb(CallBack callBack);

        void showChangeResult(String original, String number, String target, CallBack callBack);
    }

    public interface CallBack {
        void success(String response);

        void fail();
    }

    public interface Change {
        //https://route.showapi.com/105-31?fromCode=GBP&money=100&showapi_appid=29971&toCode=EUR&showapi_sign=a79945dccf384b61847fc8d13183d67f
        @GET("/105-31")
        Observable<ParitiesResultBean> getChangeResult(@Query("fromCode") String fromCode, @Query("money") String money,
                                                       @Query("showapi_appid") String showapi_appid, @Query("toCode") String toCode,
                                                       @Query("showapi_sign") String showapi_sign);
    }
}