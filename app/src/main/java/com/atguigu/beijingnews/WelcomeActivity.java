package com.atguigu.beijingnews;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.beijingnews.activity.GuideActivity;
import com.atguigu.beijingnews.activity.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    private RelativeLayout activity_main;
    private ImageView iv_icon;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到屏幕的宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        setContentView(R.layout.activity_welcome);
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);

        //普通动画
//        setAnimation();

        //使用属性动画-解决有些设备上没有动画的问题
        setObjectAnimator();


    }

    private void setAnimation() {
        //三个动画：旋转动画，渐变动画，缩放动画
        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);//设置持续时间
        ra.setFillAfter(true);//设置停留在旋转后的状态

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);//设置持续时间
        aa.setFillAfter(true);//设置停留在旋转后的状态
//
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);//设置持续时间
        sa.setFillAfter(true);//设置停留在旋转后的状态
//
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(ra);
        set.addAnimation(sa);
//
//        //开始播放动画
        activity_main.startAnimation(set);
//
//        //监听动画播放完成
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                Toast.makeText(WelcomeActivity.this, "动画播放完成", Toast.LENGTH_SHORT).show();
                boolean startMain = CacheUtils.getBoolean(WelcomeActivity.this, "start_main");
                Intent intent = null;
                if (startMain) {
                    //进入主页面
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(WelcomeActivity.this, GuideActivity.class);

                }
                startActivity(intent);

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setObjectAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(activity_main,"rotation",0,360);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(activity_main, "alpha", 0, 1);
        //设置旋转的中心点
        activity_main.setPivotX(screenWidth / 2);
        activity_main.setPivotY(screenHeight / 2);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(activity_main, "scaleY", 0f, 1f, 1f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(activity_main, "scaleX", 0f, 1f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
//        animatorSet.setInterpolator(new BounceInterpolator());
        //两个动画一起播放
        animatorSet.playTogether(animator,animator2,animator3,animator4);
        //开始播放
        animatorSet.start();

        ///监听播放完成
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                Toast.makeText(WelcomeActivity.this, "动画播放完成", Toast.LENGTH_SHORT).show();
                boolean startMain = CacheUtils.getBoolean(WelcomeActivity.this, "start_main");
                Intent intent = null;
                if (startMain) {
                    //进入主页面
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(WelcomeActivity.this, GuideActivity.class);

                }
                startActivity(intent);

                finish();

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }



}
