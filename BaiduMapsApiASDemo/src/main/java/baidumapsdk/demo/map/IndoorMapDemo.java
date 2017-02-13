package baidumapsdk.demo.map;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.overlayutil.IndoorRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorPlanNode;
import com.baidu.mapapi.search.route.IndoorRouteLine;
import com.baidu.mapapi.search.route.IndoorRoutePlanOption;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import baidumapsdk.demo.indoorview.BaseStripAdapter;
import baidumapsdk.demo.indoorview.StripListView;
import baidumapsdk.demo.R;

public class IndoorMapDemo extends Activity implements OnGetRoutePlanResultListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    Button isIndoorBtn;
    Boolean isIndoor = true;

    StripListView stripListView;
    BaseStripAdapter mFloorListAdapter;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    RoutePlanSearch mSearch;
    IndoorRouteLine mIndoorRouteline;
    int nodeIndex = -1;
    private TextView popupText = null; // 泡泡view
    Button mBtnPre = null; // 上一个节点
    Button mBtnNext = null; // 下一个节点

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainview = inflater.inflate(R.layout.activity_indoor, null);
        layout.addView(mainview);

        mMapView = (MapView) mainview.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        LatLng centerpos = new LatLng(39.916958, 116.379278); // 西单大悦城
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(centerpos).zoom(19.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBaiduMap.setIndoorEnable(true);

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        isIndoorBtn = (Button) mainview.findViewById(R.id.isIndoor);
        isIndoorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isIndoor) {
                    mBaiduMap.setIndoorEnable(true);
                    isIndoorBtn.setText("关闭室内图");
                    Toast.makeText(IndoorMapDemo.this, "室内图打开", Toast.LENGTH_LONG).show();
                } else {
                    mBaiduMap.setIndoorEnable(false);
                    isIndoorBtn.setText("打开室内图");
                    Toast.makeText(IndoorMapDemo.this, "室内图关闭", Toast.LENGTH_LONG).show();
                }
                isIndoor = !isIndoor;
            }
        });

        mainview.findViewById(R.id.indoorRoutePlane).setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 发起室内路线规划检索
                IndoorPlanNode startNode = new IndoorPlanNode(new LatLng(39.917380, 116.37978), "F1");
                IndoorPlanNode endNode = new IndoorPlanNode(new LatLng(39.917239, 116.37955), "F6");
                IndoorRoutePlanOption irpo = new IndoorRoutePlanOption().from(startNode).to(endNode);
                mSearch.walkingIndoorSearch(irpo);
            }
        });

        stripListView = new StripListView(this);
        layout.addView( stripListView );
        setContentView(layout);
        mFloorListAdapter = new BaseStripAdapter(IndoorMapDemo.this);


        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b == false || mapBaseIndoorMapInfo == null) {
                    stripListView.setVisibility(View.INVISIBLE);
                    return;
                }
                mFloorListAdapter.setmFloorList( mapBaseIndoorMapInfo.getFloors());
                stripListView.setVisibility(View.VISIBLE);
                stripListView.setStripAdapter(mFloorListAdapter);
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
        });
        stripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mMapBaseIndoorMapInfo == null) {
                    return;
                }
                String floor = (String) mFloorListAdapter.getItem(position);
                mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                mFloorListAdapter.setSelectedPostion(position);
                mFloorListAdapter.notifyDataSetInvalidated();
            }
        });

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        if ( indoorRouteResult.error == SearchResult.ERRORNO.NO_ERROR  ) {
            IndoorRouteOverlay overlay = new IndoorRouteOverlay(mBaiduMap);
            mIndoorRouteline = indoorRouteResult.getRouteLines().get(0);
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            overlay.setData(indoorRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

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
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }


    /**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {
        LatLng nodeLocation = null;
        String nodeTitle = null;
        IndoorRouteLine.IndoorRouteStep step = null;


        if (mIndoorRouteline == null || mIndoorRouteline.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && v.getId() == R.id.pre) {
            return;
        }
        // 设置节点索引
        if (v.getId() == R.id.next) {
            if (nodeIndex < mIndoorRouteline.getAllStep().size() - 1) {
                nodeIndex++;
            } else {
                return;
            }
        } else if (v.getId() == R.id.pre) {
            if (nodeIndex > 0) {
                nodeIndex--;
            } else {
                return;
            }
        }
        // 获取节结果信息
        step = mIndoorRouteline.getAllStep().get(nodeIndex);
        nodeLocation = step.getEntrace().getLocation();
        nodeTitle = step.getInstructions();

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }

        // 移动节点至中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(IndoorMapDemo.this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(step.getFloorId() + ":" + nodeTitle);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

        // 让楼层对应变化
        mBaiduMap.switchBaseIndoorMapFloor(step.getFloorId(), mMapBaseIndoorMapInfo.getID());
//        mFloorListAdapter.setSelectedPostion();
        mFloorListAdapter.notifyDataSetInvalidated();
    }
}
