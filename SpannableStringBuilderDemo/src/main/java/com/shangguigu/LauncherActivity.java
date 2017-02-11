package com.shangguigu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 作者：杨光福 on 2016/4/5 00:26
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class LauncherActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViewById(R.id.btn_text).setOnClickListener(this);
        findViewById(R.id.btn_listtext).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_text:
                startActivity(new Intent(this,TextActivity.class));
                break;
            case R.id.btn_listtext:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }
}
