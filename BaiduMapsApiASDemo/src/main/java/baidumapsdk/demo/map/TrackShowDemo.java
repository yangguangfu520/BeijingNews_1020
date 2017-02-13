/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package baidumapsdk.demo.map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import baidumapsdk.demo.R;

/**
 * 轨迹运行demo展示
 */
public class TrackShowDemo extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Polyline mPolyline;
    private Marker mMoveMarker;
    private Handler mHandler;


    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.00002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onCreate(this, savedInstanceState);
        mBaiduMap = mMapView.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(40.056865, 116.307766));
        builder.zoom(19.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mHandler = new Handler(Looper.getMainLooper());
        drawPolyLine();
        moveLooper();
    }

    private void drawPolyLine() {

        List<LatLng> polylines = new ArrayList<>();
        for (int index = 0; index < latlngs.length; index++) {
            polylines.add(latlngs[index]);
        }

        polylines.add(latlngs[0]);
        PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).color(Color.RED);

        mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
        OverlayOptions markerOptions;
        markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).position(polylines.get(0))
                .rotate((float) getAngle(0));
        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);

    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMap.clear();
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 循环进行移动逻辑
     */
    public void moveLooper() {
        new Thread() {

            public void run() {

                while (true) {

                    for (int i = 0; i < latlngs.length - 1; i++) {


                        final LatLng startPoint = latlngs[i];
                        final LatLng endPoint = latlngs[i + 1];
                        mMoveMarker
                                .setPosition(startPoint);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // refresh marker's rotate
                                if (mMapView == null) {
                                    return;
                                }
                                mMoveMarker.setRotate((float) getAngle(startPoint,
                                        endPoint));
                            }
                        });
                        double slope = getSlope(startPoint, endPoint);
                        // 是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);

                        double intercept = getInterception(slope, startPoint);

                        double xMoveDistance = isReverse ? getXMoveDistance(slope) :
                                -1 * getXMoveDistance(slope);


                        for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse);
                             j = j - xMoveDistance) {
                            LatLng latLng = null;
                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, startPoint.longitude);
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            }

                            final LatLng finalLatLng = latLng;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mMapView == null) {
                                        return;
                                    }
                                    mMoveMarker.setPosition(finalLatLng);
                                }
                            });
                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }

        }.start();
    }

    private static final LatLng[] latlngs = new LatLng[] {
            new LatLng(40.055826, 116.307917),
            new LatLng(40.055916, 116.308455),
            new LatLng(40.055967, 116.308549),
            new LatLng(40.056014, 116.308574),
            new LatLng(40.056440, 116.308485),
            new LatLng(40.056816, 116.308352),
            new LatLng(40.057997, 116.307725),
            new LatLng(40.058022, 116.307693),
            new LatLng(40.058029, 116.307590),
            new LatLng(40.057913, 116.307119),
            new LatLng(40.057850, 116.306945),
            new LatLng(40.057756, 116.306915),
            new LatLng(40.057225, 116.307164),
            new LatLng(40.056134, 116.307546),
            new LatLng(40.055879, 116.307636),
            new LatLng(40.055826, 116.307697),
    };

}

