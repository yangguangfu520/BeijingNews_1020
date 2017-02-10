package com.atguigu.baselibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/10 10:23
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：本地缓存工具类
 */
public class LocalCacheUtils {
    private final MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    //http://n.sinaimg.cn/news/transform/20170209/oyyB-fyamvns4161987.jpg-->lklllklklllllk
    public void putBitmap(String url, Bitmap bitmap) {

        try {
            //lklkkslkskkskskksk
            String fileName = MD5Encoder.encode(url);

            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/image/";
            //mnt/sdcard/beijintnews/image/lklkkslkskkskskksk
            File file = new File(dir, fileName);
            //mnt/sdcard/beijintnews/image/
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();//创建多级目录
            }
            //创建
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据Url获取图片
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromUrl(String url) {

        try {
            //lklkkslkskkskskksk
            String fileName = MD5Encoder.encode(url);

            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/image/";
            //mnt/sdcard/beijintnews/image/lklkkslkskkskskksk
            File file = new File(dir, fileName);

            //如果该文件存在
            if (file.exists()) {

                FileInputStream inputStream = new FileInputStream(file);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //当本地获取图片的时候，并且保持到内存中
                if(bitmap != null){
                    memoryCacheUtils.putBitmap(url,bitmap);
                }
                return bitmap;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
