package com.atguigu.beijingnews;

import android.app.Application;

import org.xutils.x;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/6 10:07
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
