package com.atguigu.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 14:09
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：缓存工具类
 */
public class CacheUtils {
    /**
     * 保持参数
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 得到保存的参数
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }


    /**
     * 缓存文本信息
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //sdcard可用
            //就用文本缓存到sdcard
            try {
                //lklkkslkskkskskksk
                String fileName = MD5Encoder.encode(key);

                String dir = Environment.getExternalStorageDirectory() + "/beijingnews/text/";
                //mnt/sdcard/beijintnews/text/lklkkslkskkskskksk
                File file = new File(dir, fileName);
                //mnt/sdcard/beijintnews/text/
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();//创建多级目录
                }
                //创建
                if (!file.exists()) {
                    file.createNewFile();
                }

                //保持到sdcard上了
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(value.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
            sp.edit().putString(key,value).commit();
        }

    }

    /**
     * 得到缓存的文本信息
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        String result = "";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            //sdcard可用
            //就用文本缓存到sdcard
            try {
                //lklkkslkskkskskksk
                String fileName = MD5Encoder.encode(key);

                String dir = Environment.getExternalStorageDirectory() + "/beijingnews/text/";
                //mnt/sdcard/beijintnews/text/lklkkslkskkskskksk
                File file = new File(dir, fileName);
                //mnt/sdcard/beijintnews/text/

                if(file.exists()){

                    int length;
                    byte[] buffer = new byte[1024];

                    //文件输入流
                    FileInputStream inputStream = new FileInputStream(file);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    while ((length=inputStream.read(buffer))!=-1){
                        outputStream.write(buffer,0,length);
                    }

                    //转换成字符串
                    result = outputStream.toString();
                    inputStream.close();
                    outputStream.close();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
            result = sp.getString(key,"");
        }

        return  result;
    }
}
