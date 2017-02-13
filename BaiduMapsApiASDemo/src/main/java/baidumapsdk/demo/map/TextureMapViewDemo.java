/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package baidumapsdk.demo.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import baidumapsdk.demo.R;

/**
 * 此Demo用来说明用TextureMapView显示地图
 */
public class TextureMapViewDemo extends Activity {

    private TextureMapView mMapView;
    private TextureMapView mMapView2;
    private BaiduMap mBaiduMap;
    private BaiduMap mBaiduMap2;
    private ViewPager pager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_map_view_demo);
        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mMapView2 = (TextureMapView) findViewById(R.id.mTexturemap2);
        mBaiduMap = mMapView.getMap();
        mBaiduMap2 = mMapView2.getMap();

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("texturehint").setIndicator("功能说明",
                null).setContent(
                R.id.texturehint));

        tabHost.addTab(tabHost.newTabSpec("mTexturemap").setIndicator("地图")
                .setContent(R.id.mTexturemap));

        tabHost.addTab(tabHost.newTabSpec("textdesc").setIndicator("Scrollview页")
                .setContent(R.id.textdesc));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
    }


}
