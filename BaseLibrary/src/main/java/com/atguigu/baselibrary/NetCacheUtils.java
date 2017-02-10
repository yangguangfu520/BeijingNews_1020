package com.atguigu.baselibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/10 09:35
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：网络缓存工具类
 */
public class NetCacheUtils {
    /**
     * 请求图片成功
     */
    public static final int SECUSS = 1;
    /**
     * 请求图片失败
     */
    public static final int FAIL = 2;
    private final Handler handler;
    /**
     * 本地缓存工具类
     */
    private final LocalCacheUtils localCacheutils;
    /**
     * 内存缓存工具类
     */
    private final MemoryCacheUtils memoryCacheUtils;
    private ExecutorService service;

    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheutils, MemoryCacheUtils memoryCacheUtils) {
        this.handler = handler;
        service =  Executors.newFixedThreadPool(10);
        this.localCacheutils = localCacheutils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    public void getBitmapFromNet(String url, int position) {
        //每进来一次创建一个线程请求一张图片
//        new Thread(new MyRunnable(url,position)).start();
        service.execute(new MyRunnable(url,position));
    }

    /**
     * 请求图片
     */
    class MyRunnable implements  Runnable{

        private final String url;
        private final int position;

        public MyRunnable(String url, int position) {
            this.url = url;
            this.position = position;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");//要大小
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.connect();
                int code = connection.getResponseCode();
                if(code ==200){
                    //请求图片成功
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    //保存一份到本地
                    localCacheutils.putBitmap(url,bitmap);
                    //保存一份到内存
                    memoryCacheUtils.putBitmap(url,bitmap);

                    //把图片显示到控件上
                    Message msg = Message.obtain();
                    msg.obj = bitmap;
                    msg.what = SECUSS;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
                //请求图片失败
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }

        }
    }
}
