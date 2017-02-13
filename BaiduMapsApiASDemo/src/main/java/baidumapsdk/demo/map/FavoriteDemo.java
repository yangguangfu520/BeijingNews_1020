package baidumapsdk.demo.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.favorite.FavoriteManager;
import com.baidu.mapapi.favorite.FavoritePoiInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import baidumapsdk.demo.R;

/**
 * 演示如何使用本地点收藏功能
 */
public class FavoriteDemo extends Activity implements OnMapLongClickListener, 
        OnMarkerClickListener, OnMapClickListener {

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 界面控件相关
    private EditText locationText;
    private EditText nameText;
    private View mPop;
    private View mModify;
    EditText mdifyName;
    // 保存点中的点id
    private String currentID;
    // 现实marker的图标
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
    List<Marker> markers = new ArrayList<Marker>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLongClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapClickListener(this);
        // 初始化收藏夹
        FavoriteManager.getInstance().init();
        // 初始化UI
        initUI();
    }

    public void initUI() {
        locationText = (EditText) findViewById(R.id.pt);
        nameText = (EditText) findViewById(R.id.name);
        LayoutInflater mInflater = getLayoutInflater();
        mPop = (View) mInflater.inflate(R.layout.activity_favorite_infowindow, null, false);
    }

    /**
     * 添加收藏点
     *
     * @param v
     */
    public void saveClick(View v) {
        if (nameText.getText().toString() == null || nameText.getText().toString().equals("")) {
            Toast.makeText(FavoriteDemo.this, "名称必填", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (locationText.getText().toString() == null || locationText.getText().toString().equals("")) {
            Toast.makeText(FavoriteDemo.this, "坐标点必填", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        FavoritePoiInfo info = new FavoritePoiInfo();
        info.poiName(nameText.getText().toString());

        LatLng pt;
        try {
            String strPt = locationText.getText().toString();
            String lat = strPt.substring(0, strPt.indexOf(","));
            String lng = strPt.substring(strPt.indexOf(",") + 1);
            pt = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            info.pt(pt);
            if (FavoriteManager.getInstance().add(info) == 1) {
                Toast.makeText(FavoriteDemo.this, "添加成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(FavoriteDemo.this, "添加失败", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(FavoriteDemo.this, "坐标解析错误", Toast.LENGTH_LONG)
                    .show();
        }


    }

    /**
     * 修改收藏点
     *
     * @param v
     */
    public void modifyClick(View v) {
        mBaiduMap.hideInfoWindow();
        // 弹框修改
        LayoutInflater mInflater = getLayoutInflater();
        mModify = (LinearLayout) mInflater.inflate(R.layout.activity_favorite_alert, null);
        mdifyName = (EditText) mModify.findViewById(R.id.modifyedittext);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(mModify);
        String oldName = FavoriteManager.getInstance().getFavPoi(currentID).getPoiName();
        mdifyName.setText(oldName);
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = mdifyName.getText().toString();
                if (newName != null && !newName.equals("")) {
                    // modify
                    FavoritePoiInfo info = FavoriteManager.getInstance().getFavPoi(currentID);
                    info.poiName(newName);
                    if (FavoriteManager.getInstance().updateFavPoi(currentID, info)) {
                        Toast.makeText(FavoriteDemo.this, "修改成功", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FavoriteDemo.this, "名称不能为空，修改失败", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }


        });

        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 删除一个指定点
     *
     * @param v
     */
    public void deleteOneClick(View v) {
        if (FavoriteManager.getInstance().deleteFavPoi(currentID)) {
            Toast.makeText(FavoriteDemo.this, "删除点成功", Toast.LENGTH_LONG).show();
            if (markers != null) {
                for (int i = 0; i < markers.size(); i++) {
                    if (markers.get(i).getExtraInfo().getString("id").equals(currentID)) {
                        markers.get(i).remove();
                        markers.remove(i);
                        mBaiduMap.hideInfoWindow();
                        break;
                    }
                }
            }
        } else {
            Toast.makeText(FavoriteDemo.this, "删除点失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取全部收藏点
     *
     * @param v
     */
    public void getAllClick(View v) {
        mBaiduMap.clear();
        List<FavoritePoiInfo> list = FavoriteManager.getInstance().getAllFavPois();
        if (list == null || list.size() == 0) {
            Toast.makeText(FavoriteDemo.this, "没有收藏点", Toast.LENGTH_LONG).show();
            return;
        }
        // 绘制在地图
        markers.clear();
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions option = new MarkerOptions().icon(bdA).position(list.get(i).getPt());
            Bundle b = new Bundle();
            b.putString("id", list.get(i).getID());
            option.extraInfo(b);
            markers.add((Marker) mBaiduMap.addOverlay(option));
        }

    }

    /**
     * 删除全部点
     *
     * @param v
     */
    public void deleteAllClick(View v) {
        if (FavoriteManager.getInstance().clearAllFavPois()) {
            Toast.makeText(FavoriteDemo.this, "全部删除成功", Toast.LENGTH_LONG).show();
            mBaiduMap.clear();
            mBaiduMap.hideInfoWindow();
        } else {
            Toast.makeText(FavoriteDemo.this, "全部删除失败", Toast.LENGTH_LONG).show();
        }
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
        // 释放收藏夹功能资源
        FavoriteManager.getInstance().destroy();
        bdA.recycle();
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        mBaiduMap = null;
        super.onDestroy();
    }


    @Override
    public void onMapLongClick(LatLng point) {
        // TODO Auto-generated method stub
        locationText.setText(String.valueOf(point.latitude) + "," + String.valueOf(point.longitude));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mBaiduMap.hideInfoWindow();
        // TODO Auto-generated method stub
        if (marker == null) {
            return false;
        }
        InfoWindow mInfoWindow = new InfoWindow(mPop, marker.getPosition(), -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
        mBaiduMap.setMapStatus(update);
        currentID = marker.getExtraInfo().getString("id");
        return true;
    }

    @Override
    public void onMapClick(LatLng point) {
        // TODO Auto-generated method stub
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        // TODO Auto-generated method stub
        return false;
    }

}
