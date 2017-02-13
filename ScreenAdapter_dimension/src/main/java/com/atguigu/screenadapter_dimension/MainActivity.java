package com.atguigu.screenadapter_dimension;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        float density  = getResources().getDisplayMetrics().density;
        Log.e("TAG","density=="+density);
        Toast.makeText(this, "density=="+density, Toast.LENGTH_SHORT).show();
    }
}
