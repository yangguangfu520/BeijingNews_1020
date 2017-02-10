package com.atguigu.beijingnews;

import android.app.Application;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;



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

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
