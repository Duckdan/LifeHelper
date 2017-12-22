package com.study.yang.lifehelper.utils;


import com.study.yang.lifehelper.defineview.WheelImageView;

import static com.study.yang.lifehelper.constant.LifeHelper.SMALL_WHEEL_SPEED_FAST;
import static com.study.yang.lifehelper.constant.LifeHelper.SMALL_WHEEL_SPEED_NORMAL;
import static com.study.yang.lifehelper.constant.LifeHelper.SMALL_WHEEL_SPEED_SUPER_FAST;
import static com.study.yang.lifehelper.constant.LifeHelper.WHEEL_SPEED_FAST;
import static com.study.yang.lifehelper.constant.LifeHelper.WHEEL_SPEED_NORMAL;
import static com.study.yang.lifehelper.constant.LifeHelper.WHEEL_SPEED_SUPER_FAST;

/**
 * Created by Administrator on 2017/4/15/015.
 */

public class AnimationUtils {


    private final WheelImageView wheelLeft;
    private final WheelImageView wheelRight;
    private final WheelImageView wheelSmallLeft;
    private final WheelImageView wheelSmallRight;

    public AnimationUtils(WheelImageView wheelLeft, WheelImageView wheelRight,
                          WheelImageView wheelSmallLeft, WheelImageView wheelSmallRight) {
        this.wheelLeft = wheelLeft;
        this.wheelRight = wheelRight;
        this.wheelSmallLeft = wheelSmallLeft;
        this.wheelSmallRight = wheelSmallRight;
    }

    public void startAnimation() {
        wheelLeft.startAnimation(WHEEL_SPEED_NORMAL, true);
        wheelRight.startAnimation(WHEEL_SPEED_NORMAL, true);
        wheelSmallLeft.startAnimation(SMALL_WHEEL_SPEED_NORMAL, true);
        wheelSmallRight.startAnimation(SMALL_WHEEL_SPEED_NORMAL, true);
    }


    public void stopAnimation() {
        wheelLeft.stopAnimation();
        wheelRight.stopAnimation();
        wheelSmallLeft.stopAnimation();
        wheelSmallRight.stopAnimation();
    }

    public void startRecordPlayingAnimation() {
        wheelLeft.startAnimation(WHEEL_SPEED_NORMAL, true);
        wheelRight.startAnimation(WHEEL_SPEED_NORMAL, true);
        wheelSmallLeft.startAnimation(SMALL_WHEEL_SPEED_NORMAL, true);
        wheelSmallRight.startAnimation(SMALL_WHEEL_SPEED_NORMAL, true);
    }

    public void stopRecordPlayingAnimation() {
        stopAnimation();
        startRecordPlayingDoneAnimation();
    }

    public void startRecordPlayingDoneAnimation() {
        wheelLeft.startAnimation(WHEEL_SPEED_SUPER_FAST, false, 4);
        wheelRight.startAnimation(WHEEL_SPEED_SUPER_FAST, false, 4);
        wheelSmallLeft.startAnimation(SMALL_WHEEL_SPEED_SUPER_FAST, false, 2);
        wheelSmallRight.startAnimation(SMALL_WHEEL_SPEED_SUPER_FAST, false, 2);
    }

    public void startForwardAnimation() {
        wheelLeft.startAnimation(WHEEL_SPEED_FAST, true);
        wheelRight.startAnimation(WHEEL_SPEED_FAST, true);
        wheelSmallLeft.startAnimation(SMALL_WHEEL_SPEED_FAST, true);
        wheelSmallRight.startAnimation(SMALL_WHEEL_SPEED_FAST, true);
    }

    public void startBackwardAnimation() {
        wheelLeft.startAnimation(WHEEL_SPEED_FAST, false);
        wheelRight.startAnimation(WHEEL_SPEED_FAST, false);
        wheelSmallLeft.startAnimation(SMALL_WHEEL_SPEED_FAST, false);
        wheelSmallRight.startAnimation(SMALL_WHEEL_SPEED_FAST, false);
    }
}
