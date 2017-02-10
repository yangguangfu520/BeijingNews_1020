package com.atguigu.baselibrary;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/10 09:32
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：三级缓存工具类
 */
public class BitmapCacheUtils {
    //网络缓存工具类
    private NetCacheUtils netCacheUtils;
    //本地缓存工具类
    private LocalCacheUtils localCacheutils;
    //内存缓存工具类
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheutils = new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler, localCacheutils,memoryCacheUtils);
    }

    /**
     * 三级缓存设计步骤：
     *   * 从内存中取图片
     *   * 从本地文件中取图片
     *        向内存中保持一份
     *   * 请求网络图片，获取图片，显示到控件上
     *      * 向内存存一份
     *      * 向本地文件中存一份
     *
     * @param url
     * @param position
     * @return
     */
    public Bitmap getBitmapFromNet(String url, int position) {
        //1.从内存中取图片

        if (memoryCacheUtils != null) {
            Bitmap bitmap = memoryCacheUtils.getBitmapFromUrl(url);
            if (bitmap != null) {
                Log.e("TAG", "内存缓存图片成功==" + position);
                return bitmap;
            }
        }
        //2.从本地文件中取图片
        if (localCacheutils != null) {
            Bitmap bitmap = localCacheutils.getBitmapFromUrl(url);
            if (bitmap != null) {
                Log.e("TAG", "本地缓存图片成功==" + position);
                return bitmap;
            }
        }

        //3.请求网络图片，获取图片，显示到控件上
        netCacheUtils.getBitmapFromNet(url, position);

        return null;
    }
}
