package baidumapsdk.demo.radar;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyInfo;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.radar.RadarUploadInfoCallback;

import java.util.ArrayList;
import java.util.List;
import baidumapsdk.demo.R;

/**
 * 演示周边雷达的业务场景使用
 */
public class RadarDemo extends Activity implements RadarUploadInfoCallback,
        RadarSearchListener, BDLocationListener, OnMarkerClickListener, OnMapClickListener {

    // 界面空间相关
    private CustomViewPager mPager; // 自定义viewPager，目的是禁用手势滑动
    private List<View> listViews;
    private TabHost mTabHost;
    private EditText userId;
    private EditText userDes;
    private Button uploadOnece;
    private Button uploadContinue;
    private Button stopUpload;
    private Button switchBtn;
    private Button searchNearbyBtn;
    private Button clearRstBtn;
    private Button clearInfoBtn;
    private int index = 0;
    private Button listPreBtn;
    private Button listNextBtn;
    private TextView listCurPage;
    private Button mapPreBtn;
    private Button mapNextBtn;
    private TextView mapCurPage;


    // 定位相关
    LocationClient mLocClient;
    private int pageIndex = 0;
    private int curPage = 0;
    private int totalPage = 0;
    private LatLng pt = null;

    // 周边雷达相关
    RadarNearbyResult listResult = null;
    ListView mResultListView = null;
    RadarResultListAdapter mResultListAdapter = null;
    private String userID = "";
    private String userComment = "";
    private boolean uploadAuto = false;

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView popupText = null; // 泡泡view
    BitmapDescriptor ff3 = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radartablayout);
        // 初始化UI和地图
        initUI();
        // 周边雷达设置监听
        RadarSearchManager.getInstance().addNearbyInfoListener(this);
        // 周边雷达设置用户，id为空默认是设备标识
        RadarSearchManager.getInstance().setUserID(userID);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void initUI() {
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec("tabUpload")
                .setIndicator(composeLayout("上传位置", 0))
                .setContent(R.id.tabUpload));
        mTabHost.addTab(mTabHost.newTabSpec("tabGet")
                .setIndicator(composeLayout("检索周边", 0))
                .setContent(R.id.tabGet));
        mTabHost.setCurrentTab(0);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    mTabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(null);
                }
                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#B0E2FF"));
            }

        });

        mPager = (CustomViewPager) findViewById(R.id.viewpager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();

        View layout = mInflater.inflate(R.layout.activity_radarlist, null);
        View mapLayout = mInflater.inflate(R.layout.activity_radarmap, null);
        // 地图初始化
        mMapView = (MapView) mapLayout.findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setMyLocationEnabled(true);
        listViews.add(layout);
        listViews.add(mapLayout);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

        userId = (EditText) findViewById(R.id.id);
        userDes = (EditText) findViewById(R.id.des);
        uploadOnece = (Button) findViewById(R.id.uploadonece);
        uploadContinue = (Button) findViewById(R.id.uploadcontinue);
        stopUpload = (Button) findViewById(R.id.stopupload);
        switchBtn = (Button) findViewById(R.id.switchButton);
        searchNearbyBtn = (Button) findViewById(R.id.searchNearByButton);
        clearRstBtn = (Button) findViewById(R.id.clearResultButton);
        clearInfoBtn = (Button) findViewById(R.id.clearInfoButton);
        listPreBtn = (Button) layout.findViewById(R.id.radarlistpre);
        listNextBtn = (Button) layout.findViewById(R.id.radarlistnext);
        listCurPage = (TextView) layout.findViewById(R.id.radarListPage);
        mapPreBtn = (Button) mapLayout.findViewById(R.id.radarmappre);
        mapNextBtn = (Button) mapLayout.findViewById(R.id.radarmapnext);
        mapCurPage = (TextView) mapLayout.findViewById(R.id.radarMapPage);
        uploadContinue.setEnabled(true);
        stopUpload.setEnabled(false);
        clearRstBtn.setEnabled(false);
        clearInfoBtn.setEnabled(false);
        listPreBtn.setVisibility(View.INVISIBLE);
        listNextBtn.setVisibility(View.INVISIBLE);
        mapPreBtn.setVisibility(View.INVISIBLE);
        mapNextBtn.setVisibility(View.INVISIBLE);
        listCurPage.setVisibility(View.INVISIBLE);
        mapCurPage.setVisibility(View.INVISIBLE);

        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.DKGRAY);
        ArrayList<RadarNearbyInfo> infos = new ArrayList<RadarNearbyInfo>();
        mResultListAdapter = new RadarResultListAdapter(null);
        mResultListView = (ListView) layout.findViewById(R.id.radar_list);
        mResultListView.setAdapter(mResultListAdapter);
        mResultListAdapter.notifyDataSetChanged();
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                userID = userId.getText().toString();
                userComment = userDes.getText().toString();
                RadarSearchManager.getInstance().setUserID(userID);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        };
        // 用户ID和用户描述监听文本框变化
        userId.addTextChangedListener(textWatcher);
        userDes.addTextChangedListener(textWatcher);
    }

    /**
     * 上传一次位置
     *
     * @param v
     */
    public void uploadOnceClick(View v) {
        if (pt == null) {
            Toast.makeText(RadarDemo.this, "未获取到位置", Toast.LENGTH_LONG).show();
            return;
        }
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = userComment;
        info.pt = pt;
        RadarSearchManager.getInstance().uploadInfoRequest(info);
        clearInfoBtn.setEnabled(true);
    }

    /**
     * 开始自动上传
     *
     * @param v
     */
    public void uploadContinueClick(View v) {
        if (pt == null) {
            Toast.makeText(RadarDemo.this, "未获取到位置", Toast.LENGTH_LONG).show();
            return;
        }
        uploadAuto = true;
        RadarSearchManager.getInstance().startUploadAuto(this, 5000);
        uploadContinue.setEnabled(false);
        stopUpload.setEnabled(true);
        clearInfoBtn.setEnabled(true);
    }

    /**
     * 停止自动上传
     *
     * @param v
     */
    public void stopUploadClick(View v) {
        uploadAuto = false;
        RadarSearchManager.getInstance().stopUploadAuto();
        stopUpload.setEnabled(false);
        uploadContinue.setEnabled(true);
    }

    /**
     * 清除自己当前的信息
     *
     * @param v
     */
    public void clearInfoClick(View v) {
        RadarSearchManager.getInstance().clearUserInfo();
    }

    /**
     * 查找周边的人
     *
     * @param v
     */
    public void searchNearby(View v) {
        if (pt == null) {
            Toast.makeText(RadarDemo.this, "未获取到位置", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        pageIndex = 0;
        searchRequest(pageIndex);
    }

    /**
     * 上一页
     *
     * @param v
     */
    public void preClick(View v) {
        if (pageIndex < 1) {
            return;
        }
        // 上一页
        pageIndex--;
        searchRequest(pageIndex);
    }

    /**
     * 下一页
     *
     * @param v
     */
    public void nextClick(View v) {
        if (pageIndex >= totalPage - 1) {
            return;
        }
        // 下一页
        pageIndex++;
        searchRequest(pageIndex);
    }

    private void searchRequest(int index) {
        curPage = 0;
        totalPage = 0;

        RadarNearbySearchOption option = new RadarNearbySearchOption()
                .centerPt(pt).pageNum(pageIndex).radius(2000).pageCapacity(11);
        RadarSearchManager.getInstance().nearbyInfoRequest(option);

        listPreBtn.setVisibility(View.INVISIBLE);
        listNextBtn.setVisibility(View.INVISIBLE);
        mapPreBtn.setVisibility(View.INVISIBLE);
        mapNextBtn.setVisibility(View.INVISIBLE);
        listCurPage.setText("0/0");
        mapCurPage.setText("0/0");
        mBaiduMap.hideInfoWindow();
    }

    /**
     * 清除查找结果
     *
     * @param v
     */
    public void clearResult(View v) {
        parseResultToList(null);
        parseResultToMap(null);
        clearRstBtn.setEnabled(false);
        listPreBtn.setVisibility(View.INVISIBLE);
        listNextBtn.setVisibility(View.INVISIBLE);
        mapPreBtn.setVisibility(View.INVISIBLE);
        mapNextBtn.setVisibility(View.INVISIBLE);
        listCurPage.setVisibility(View.INVISIBLE);
        mapCurPage.setVisibility(View.INVISIBLE);
        mBaiduMap.hideInfoWindow();
    }

    // viewPager切换
    public void switchClick(View v) {
        if (index == 0) {
            // 切换为地图
            index = 1;
            switchBtn.setText("列表");
        } else {
            // 切换为列表
            index = 0;
            switchBtn.setText("地图");
        }
        mPager.setCurrentItem(index);

    }

    /**
     * 更新结果列表
     *
     * @param res
     */
    public void parseResultToList(RadarNearbyResult res) {
        if (res == null) {
            if (mResultListAdapter.list != null) {
                mResultListAdapter.list.clear();
                mResultListAdapter.notifyDataSetChanged();
            }

        } else {
            mResultListAdapter.list = res.infoList;
            mResultListAdapter.notifyDataSetChanged();
            if (curPage > 0) {
                listPreBtn.setVisibility(View.VISIBLE);
            }
            if (totalPage - 1 > curPage) {
                listNextBtn.setVisibility(View.VISIBLE);
            }
            if (totalPage > 0) {
                listCurPage.setVisibility(View.VISIBLE);
                listCurPage.setText(String.valueOf(curPage + 1) + "/" + String.valueOf(totalPage));
            }

        }

    }

    /**
     * 更新结果地图
     *
     * @param res
     */
    public void parseResultToMap(RadarNearbyResult res) {
        mBaiduMap.clear();
        if (res != null && res.infoList != null && res.infoList.size() > 0) {
            for (int i = 0; i < res.infoList.size(); i++) {
                MarkerOptions option = new MarkerOptions().icon(ff3).position(res.infoList.get(i).pt);
                Bundle des = new Bundle();
                if (res.infoList.get(i).comments == null || res.infoList.get(i).comments.equals("")) {
                    des.putString("des", "没有备注");
                } else {
                    des.putString("des", res.infoList.get(i).comments);
                }

                option.extraInfo(des);
                mBaiduMap.addOverlay(option);
            }
        }
        if (curPage > 0) {
            mapPreBtn.setVisibility(View.VISIBLE);
        }
        if (totalPage - 1 > curPage) {
            mapNextBtn.setVisibility(View.VISIBLE);
        }
        if (totalPage > 0) {
            mapCurPage.setVisibility(View.VISIBLE);
            mapCurPage.setText(String.valueOf(curPage + 1) + "/" + String.valueOf(totalPage));
        }

    }

    /**
     * 实现上传callback，自动上传
     */
    @Override
    public RadarUploadInfo onUploadInfoCallback() {
        // TODO Auto-generated method stub
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = userComment;
        info.pt = pt;
        Log.e("hjtest", "OnUploadInfoCallback");
        return info;
    }

    @Override
    public void onGetNearbyInfoList(RadarNearbyResult result,
                                    RadarSearchError error) {
        // TODO Auto-generated method stub
        if (error == RadarSearchError.RADAR_NO_ERROR) {
            Toast.makeText(RadarDemo.this, "查询周边成功", Toast.LENGTH_LONG)
                    .show();
            // 获取成功
            listResult = result;
            curPage = result.pageIndex;
            totalPage = result.pageNum;
            // 处理数据
            parseResultToList(listResult);
            parseResultToMap(listResult);
            clearRstBtn.setEnabled(true);
        } else {
            // 获取失败
            curPage = 0;
            totalPage = 0;
            Toast.makeText(RadarDemo.this, "查询周边失败", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onGetUploadState(RadarSearchError error) {
        // TODO Auto-generated method stub
        if (error == RadarSearchError.RADAR_NO_ERROR) {
            // 上传成功
            if (!uploadAuto) {
                Toast.makeText(RadarDemo.this, "单次上传位置成功", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            // 上传失败
            if (!uploadAuto) {
                Toast.makeText(RadarDemo.this, "单次上传位置失败", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onGetClearInfoState(RadarSearchError error) {
        // TODO Auto-generated method stub
        if (error == RadarSearchError.RADAR_NO_ERROR) {
            // 清除成功
            Toast.makeText(RadarDemo.this, "清除位置成功", Toast.LENGTH_LONG)
                    .show();
        } else {
            // 清除失败
            Toast.makeText(RadarDemo.this, "清除位置失败", Toast.LENGTH_LONG)
                    .show();
        }
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        mBaiduMap.hideInfoWindow();
        if (marker != null) {
            popupText = new TextView(RadarDemo.this);
            popupText.setBackgroundResource(R.drawable.popup);
            popupText.setTextColor(0xFF000000);
            popupText.setText(marker.getExtraInfo().getString("des"));
            mBaiduMap.showInfoWindow(new InfoWindow(popupText, marker.getPosition(), -47));
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
            mBaiduMap.setMapStatus(update);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#B0E2FF"));
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 释放周边雷达相关
        RadarSearchManager.getInstance().removeNearbyInfoListener(this);
        RadarSearchManager.getInstance().clearUserInfo();
        RadarSearchManager.getInstance().destroy();
        // 释放地图
        ff3.recycle();
        mMapView.onDestroy();
        mBaiduMap = null;
        super.onDestroy();
    }

    // 定位SDK回调
    @Override
    public void onReceiveLocation(BDLocation arg0) {
        // TODO Auto-generated method stub
        if (arg0 == null || mBaiduMap == null) {
            return;
        }
        pt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(arg0.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(arg0.getLatitude())
                .longitude(arg0.getLongitude()).build();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationData(locData);
        }

    }

    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == 0) {
                // 切换为列表
                index = 0;
                switchBtn.setText("地图");
            } else {
                // 切换为地图
                index = 1;
                switchBtn.setText("列表");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }


    /**
     * 结果列表listview适配器
     */
    private class RadarResultListAdapter extends BaseAdapter {
        public List<RadarNearbyInfo> list;

        public RadarResultListAdapter(List<RadarNearbyInfo> res) {
            super();
            this.list = res;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(RadarDemo.this,
                    R.layout.demo_info_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            title.setTextColor(Color.parseColor("#0000FF"));
            desc.setTextColor(Color.parseColor("#0000FF"));
            if (list == null || list.size() == 0 || index >= list.size()) {
                desc.setText("");
                title.setText("");
            } else {
                if (list.get(index).comments == null || list.get(index).comments.equals("")) {
                    desc.setText(String.valueOf(list.get(index).distance) + "米" + "_没有备注");
                } else {
                    desc.setText(String.valueOf(list.get(index).distance) + "米" + "_" + list.get(index).comments);
                }

                title.setText(list.get(index).userID);
            }


            return convertView;
        }

        @Override
        public int getCount() {
            if (list == null || (list != null && list.size() < 10)) {
                return 10;
            } else {
                return list.size();
            }

        }

        @Override
        public Object getItem(int index) {
            if (list == null) {
                return new RadarNearbyInfo();
            } else {
                return list.get(index);
            }

        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }

    public View composeLayout(String s, int i) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView iv = new ImageView(this);
        iv.setImageResource(i);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 5, 0, 0);
//      layout.addView(iv, lp);
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);
        tv.setText(s);
        tv.setTextColor(Color.parseColor("#0000FF"));
        tv.setTextSize(20);
        layout.addView(tv, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        return layout;
    }


}
