package com.study.yang.lifehelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.yang.lifehelper.bean.constellation.DayInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/20/020.
 */
public class ConstellationDetailActivity extends BaseActivity {
    @Bind(R.id.tv_love_txt)
    TextView tvLoveTxt;
    @Bind(R.id.tv_work_txt)
    TextView tvWorkTxt;
    @Bind(R.id.rb_work_star)
    RatingBar rbWorkStar;
    @Bind(R.id.rb_money_star)
    RatingBar rbMoneyStar;
    @Bind(R.id.tv_lucky_color)
    TextView tvLuckyColor;
    @Bind(R.id.tv_lucky_time)
    TextView tvLuckyTime;
    @Bind(R.id.rb_love_star)
    RatingBar rbLoveStar;
    @Bind(R.id.tv_lucky_direction)
    TextView tvLuckyDirection;
    @Bind(R.id.rb_summary_star)
    RatingBar rbSummaryStar;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_money_txt)
    TextView tvMoneyTxt;
    @Bind(R.id.tv_general_txt)
    TextView tvGeneralTxt;
    @Bind(R.id.tv_grxz)
    TextView tvGrxz;
    @Bind(R.id.tv_lucky_num)
    TextView tvLuckyNum;
    @Bind(R.id.tv_day_notice)
    TextView tvDayNotice;
    @Bind(R.id.tv_day_info_title)
    TextView tvDayInfoTitle;
    @Bind(R.id.rl_day_info)
    RelativeLayout mRlDayInfo;
    private int mPosition;

    private String[] constellationName = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_day_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        DayInfo dayInfo = (DayInfo) intent.getSerializableExtra("dayInfo");
        mPosition = intent.getIntExtra("position", 0);
        tvDayInfoTitle.setText(constellationName[mPosition]);
        showData(dayInfo);
    }

    private void showData(DayInfo dayInfo) {
        Log.d("TAGGGG", dayInfo.work_txt);
        tvWorkTxt.setText("今日情感：" + dayInfo.love_txt);
        tvWorkTxt.setText("今日工作：" + dayInfo.work_txt);
        rbWorkStar.setRating(dayInfo.work_star);
        rbMoneyStar.setRating(dayInfo.money_star);
        tvLuckyColor.setText("幸运颜色：" + dayInfo.lucky_color);
        tvLuckyTime.setText("幸运时间：" + dayInfo.lucky_time);
        rbLoveStar.setRating(dayInfo.love_star);
        tvLuckyDirection.setText("幸运方向：" + dayInfo.lucky_direction);
        rbSummaryStar.setRating(dayInfo.summary_star);
        String daytime = dayInfo.time;
        StringBuilder sb = new StringBuilder(daytime);
        sb.insert(4, "年");
        sb.insert(7, "月");
        sb.insert(10, "日");
        tvTime.setText("今日时间：" + sb.toString());
        tvMoneyTxt.setText("今日财运：" + dayInfo.money_txt);
        tvGeneralTxt.setText("综合参考：" + dayInfo.general_txt);
        tvGrxz.setText("最佳伴侣：" + dayInfo.grxz);
        tvLuckyNum.setText("幸运数字：" + dayInfo.lucky_num);
        tvDayNotice.setText("今日注意：" + dayInfo.day_notice);
        tvLoveTxt.setText("今日情感：" + dayInfo.love_txt);
    }

    @OnClick(R.id.rl_day_info)
    public void onClick() {
        onBackPressed();
    }
}
