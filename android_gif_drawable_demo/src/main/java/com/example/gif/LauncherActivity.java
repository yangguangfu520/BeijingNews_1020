package com.example.gif;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class LauncherActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViewById(R.id.btn_gif).setOnClickListener(this);
        findViewById(R.id.btn_listview_gif).setOnClickListener(this);
        findViewById(R.id.btn_gridview_gif).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_gif:
                startActivity(new Intent(this,GifActivity.class));
                break;
            case R.id.btn_listview_gif:
                startActivity(new Intent(this,ListViewActivity.class));
                break;
            case R.id.btn_gridview_gif:
                startActivity(new Intent(this,GridViewActivity.class));
                break;
        }
    }
}
