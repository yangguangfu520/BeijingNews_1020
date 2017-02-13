package com.atguigu.screenadapter_code;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 在代码中给控件设置大小，从而达到屏幕适配的这种方式叫：代码屏幕适配
 * 应用场景：广告平台的sdk,第三方sdk
 */

public class MainActivity extends AppCompatActivity {

    private TextView tv1,tv2,tv3,tv4;
    private int screenWidth;
    private int screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);

        //得到屏幕的宽
        DisplayMetrics out = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(out);
        //得到屏幕的宽和高
        screenHeight = out.heightPixels;
        screenWidth = out.widthPixels;


        //设置控件的参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(screenWidth*0.25),(int)(screenHeight*0.1));
        tv1.setLayoutParams(params);

        params = new LinearLayout.LayoutParams((int)(screenWidth*0.5),(int)(screenHeight*0.1));
        tv2.setLayoutParams(params);


        params = new LinearLayout.LayoutParams((int)(screenWidth*0.75),(int)(screenHeight*0.1));
        tv3.setLayoutParams(params);


        params = new LinearLayout.LayoutParams((int)(screenWidth),(int)(screenHeight*0.1));
        tv4.setLayoutParams(params);
    }
}
