package baidumapsdk.demo.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import baidumapsdk.demo.R;

/**
 * 演示地图缩放，旋转，视角控制，单击，双击，长按，截图的事件响应
 */
public class MapControlDemo extends Activity {

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    /**
     * 当前地点击点
     */
    private LatLng currentPt;
    /**
     * 控制按钮
     */
    private Button zoomButton;
    private Button rotateButton;
    private Button overlookButton;
    private Button saveScreenButton;

    private String touchType;

    /**
     * 用于显示地图状态的面板
     */
    private TextView mStateBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapcontrol);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mStateBar = (TextView) findViewById(R.id.state);
        initListener();
    }

    /**
     * 对地图事件的消息响应
     */
    private void initListener() {
        mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent event) {

            }
        });


        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
            /**
             * 单击地图
             */
            public void onMapClick(LatLng point) {
                touchType = "单击地图";
                currentPt = point;
                updateMapState();
            }

            /**
             * 单击地图中的POI点
             */
            public boolean onMapPoiClick(MapPoi poi) {
                touchType = "单击POI点";
                currentPt = poi.getPosition();
                updateMapState();
                return false;
            }
        });
        mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            /**
             * 长按地图
             */
            public void onMapLongClick(LatLng point) {
                touchType = "长按";
                currentPt = point;
                updateMapState();
            }
        });
        mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
            /**
             * 双击地图
             */
            public void onMapDoubleClick(LatLng point) {
                touchType = "双击";
                currentPt = point;
                updateMapState();
            }
        });

        /**
         * 地图状态发生变化
         */
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
                updateMapState();
            }

            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState();
            }

            public void onMapStatusChange(MapStatus status) {
                updateMapState();
            }
        });
        zoomButton = (Button) findViewById(R.id.zoombutton);
        rotateButton = (Button) findViewById(R.id.rotatebutton);
        overlookButton = (Button) findViewById(R.id.overlookbutton);
        saveScreenButton = (Button) findViewById(R.id.savescreen);
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.equals(zoomButton)) {
                    perfomZoom();
                } else if (view.equals(rotateButton)) {
                    perfomRotate();
                } else if (view.equals(overlookButton)) {
                    perfomOverlook();
                } else if (view.equals(saveScreenButton)) {
                    // 截图，在SnapshotReadyCallback中保存图片到 sd 卡
                    mBaiduMap.snapshot(new SnapshotReadyCallback() {
                        public void onSnapshotReady(Bitmap snapshot) {
                            File file = new File("/mnt/sdcard/test.png");
                            FileOutputStream out;
                            try {
                                out = new FileOutputStream(file);
                                if (snapshot.compress(
                                        Bitmap.CompressFormat.PNG, 100, out)) {
                                    out.flush();
                                    out.close();
                                }
                                Toast.makeText(MapControlDemo.this,
                                        "屏幕截图成功，图片存在: " + file.toString(),
                                        Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Toast.makeText(MapControlDemo.this, "正在截取屏幕图片...",
                            Toast.LENGTH_SHORT).show();

                }
                updateMapState();
            }

        };
        zoomButton.setOnClickListener(onClickListener);
        rotateButton.setOnClickListener(onClickListener);
        overlookButton.setOnClickListener(onClickListener);
        saveScreenButton.setOnClickListener(onClickListener);
    }

    /**
     * 处理缩放 sdk 缩放级别范围： [3.0,19.0]
     */
    private void perfomZoom() {
        EditText t = (EditText) findViewById(R.id.zoomlevel);
        try {
            float zoomLevel = Float.parseFloat(t.getText().toString());
            MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(zoomLevel);
            mBaiduMap.animateMapStatus(u);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的缩放级别", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理旋转 旋转角范围： -180 ~ 180 , 单位：度 逆时针旋转
     */
    private void perfomRotate() {
        EditText t = (EditText) findViewById(R.id.rotateangle);
        try {
            int rotateAngle = Integer.parseInt(t.getText().toString());
            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).rotate(rotateAngle).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的旋转角度", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理俯视 俯角范围： -45 ~ 0 , 单位： 度
     */
    private void perfomOverlook() {
        EditText t = (EditText) findViewById(R.id.overlookangle);
        try {
            int overlookAngle = Integer.parseInt(t.getText().toString());
            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(overlookAngle).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的俯角", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新地图状态显示面板
     */
    private void updateMapState() {
        if (mStateBar == null) {
            return;
        }
        String state = "";
        if (currentPt == null) {
            state = "点击、长按、双击地图以获取经纬度和地图状态";
        } else {
            state = String.format(touchType + ",当前经度： %f 当前纬度：%f",
                    currentPt.longitude, currentPt.latitude);
        }
        state += "\n";
        MapStatus ms = mBaiduMap.getMapStatus();
        state += String.format(
                "zoom=%.1f rotate=%d overlook=%d",
                ms.zoom, (int) ms.rotate, (int) ms.overlook);
        mStateBar.setText(state);
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
