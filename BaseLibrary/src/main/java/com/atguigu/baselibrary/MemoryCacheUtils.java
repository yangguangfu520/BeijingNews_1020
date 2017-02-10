package com.atguigu.baselibrary;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/10 11:00
 * 微信：yangguangfu520
 * QQ号：54143511
 * 作用：内存缓存工具类
 */
public class MemoryCacheUtils {
    private LruCache<String,Bitmap> lruCache;
    public MemoryCacheUtils(){

        int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);

        lruCache = new LruCache<String,Bitmap>(maxSize){

            /**
             * 计算每张图片大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }


    /**
     * 根据url存储图片到内存中
     * @param url
     * @param bitmap
     */
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url,bitmap);
    }

    /**
     * url获取内存中的图片
     * @param url
     * @return
     */
    public Bitmap getBitmapFromUrl(String url) {
        return lruCache.get(url);
    }
}
