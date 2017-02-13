package baidumapsdk.demo.map;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.UiSettings;
import baidumapsdk.demo.R;

/**
 * 演示地图UI控制功能
 */
public class UISettingDemo extends Activity {

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;
    private static final int paddingLeft = 0;
    private static final int paddingTop = 0;
    private static final int paddingRight = 0;
    private static final int paddingBottom = 200;
    TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uisetting);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();

        MapStatus ms = new MapStatus.Builder().build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
        mBaiduMap.animateMapStatus(u, 1000);

        mMapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 介绍获取比例尺的宽高，需在MapView绘制完成之后
                int scaleControlViewWidth = mMapView.getScaleControlViewWidth();
                int scaleControlViewHeight = mMapView.getScaleControlViewHeight();
            }
        }, 0);
    }

    /**
     * 是否启用缩放手势
     *
     * @param v
     */
    public void setZoomEnable(View v) {
        mUiSettings.setZoomGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用平移手势
     *
     * @param v
     */
    public void setScrollEnable(View v) {
        mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用旋转手势
     *
     * @param v
     */
    public void setRotateEnable(View v) {
        mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用俯视手势
     *
     * @param v
     */
    public void setOverlookEnable(View v) {
        mUiSettings.setOverlookingGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用指南针图层
     *
     * @param v
     */
    public void setCompassEnable(View v) {
        mUiSettings.setCompassEnabled(((CheckBox) v).isChecked());
    }
    /**
     * 是否显示底图默认标注
     *
     * @param v
     */
    public void setMapPoiEnable(View v) {
        mBaiduMap.showMapPoi(((CheckBox) v).isChecked());
    }

    /**
     * 是否禁用所有手势
     *
     * @param v
     */
    public void setAllGestureEnable(View v) {
        mUiSettings.setAllGesturesEnabled(!((CheckBox) v).isChecked());
    }

    /**
     * 设置Padding区域
     *
     * @param v
     */
    public void setPadding( View v) {
        if (((CheckBox) v).isChecked()) {
            mBaiduMap.setViewPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            addView(mMapView);
        } else {
            mBaiduMap.setViewPadding(0, 0, 0, 0);
            mMapView.removeView(mTextView);
        }
    }

    private void addView(MapView mapView) {
        mTextView = new TextView(this);
        mTextView.setText(getText(R.string.instruction));
        mTextView.setTextSize(15.0f);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.BLACK);
        mTextView.setBackgroundColor(Color.parseColor("#AA00FF00"));

        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
        builder.width(mapView.getWidth());
        builder.height(paddingBottom);
        builder.point(new Point(0, mapView.getHeight()));
        builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);

        mapView.addView(mTextView, builder.build());

    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
    }

}
