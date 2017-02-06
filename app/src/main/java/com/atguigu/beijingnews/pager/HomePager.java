package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnews.base.BasePager;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 15:57
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：主页面
 */
public class HomePager extends BasePager {
    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();//调用父类

        Log.e("TAG","主页面加载数据了");

        //设置标题
        tv_title.setText("主页");

        //实例视图
        TextView  textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("主页面");
        textView.setTextColor(Color.RED);

        //和父类的FrameLayout结合
        fl_main.addView(textView);

        //联网请求
    }
}
