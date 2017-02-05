package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnews.base.BasePager;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 15:57
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：新闻中心
 */
public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        tv_title.setText("新闻");
        //实例视图
        TextView  textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);

        //和父类的FrameLayout结合
        fl_main.addView(textView);
    }
}
