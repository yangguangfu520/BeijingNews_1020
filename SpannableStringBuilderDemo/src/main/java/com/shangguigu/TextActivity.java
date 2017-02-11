package com.shangguigu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.widget.TextView;

/**
 * 作者：杨光福 on 2016/4/5 00:30
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class TextActivity extends Activity {
    private TextView textview;

    private  SpannableStringBuilder ssb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        textview = (TextView) findViewById(R.id.textview);

        ssb = new SpannableStringBuilder("超级链接:网络 ");
        ssb.setSpan(new URLSpan("https://www.baidu.com"), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 网络
        ssb.setSpan(new ForegroundColorSpan(Color.LTGRAY), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
        textview.setMovementMethod(LinkMovementMethod.getInstance());// 让链接的点击事件响应的必要一句代码


//        SpannableStringBuilder  ssbs = new SpannableStringBuilder("超级链接:短信 ");
//        //把短信发送客户端调起来
//        ssbs.setSpan(new URLSpan("sms:13912345678"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 短信
//        //对5~7对应的文字设置背景颜色
//        ssbs.setSpan(new ForegroundColorSpan(Color.RED), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textview.setText(ssb);
    }
}
