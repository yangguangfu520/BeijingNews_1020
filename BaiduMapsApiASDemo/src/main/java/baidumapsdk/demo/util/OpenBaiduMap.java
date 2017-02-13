package baidumapsdk.demo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;

import java.util.ArrayList;
import java.util.List;

import baidumapsdk.demo.R;

/**
 * 此Demo用来说明调起百度地图功能
 */
public class OpenBaiduMap extends Activity {

    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_baidumap);
        TextView text = (TextView) findViewById(R.id.open_Info);
        text.setTextColor(Color.YELLOW);
        text.setText("当手机没有安装百度地图客户端或版本过低时，默认调起百度地图webApp");
        ListView mListView = (ListView) findViewById(R.id.listView_openBaiduMap);
        mListView.setAdapter(new OpenBaiduMapListAdapter(getData()));

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        startNavi();
                        break;

                    case 1:
                        startWebNavi();
                        break;

                    case 2:
                        startPoiNearbySearch();
                        break;

                    case 3:
                        startPoiDetails();
                        break;

                    case 4:
                        startRoutePlanWalking();
                        break;

                    case 5:
                        startRoutePlanDriving();
                        break;

                    case 6:
                        startRoutePlanTransit();
                        break;
                    case 7:
                        startWalkingNavi();
                        break;

                    case 8:
                        startBikingNavi();
                        break;
                    case 9:
                        startPoiPanoShow();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("启动百度地图导航(Native)");
        data.add("启动百度地图导航(Web)");
        data.add("启动百度地图Poi周边检索");
        data.add("启动百度地图Poi详情页面");
        data.add("启动百度地图步行路线规划");
        data.add("启动百度地图驾车路线规划");
        data.add("启动百度地图公交路线规划");
        data.add("启动百度地图步行导航");
        data.add("启动百度地图骑行导航");
        data.add("启动百度地图全景");
        return data;
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 启动百度地图导航(Web)
     */
    public void startWebNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2);

        BaiduMapNavigation.openWebBaiduMapNavi(para, this);
    }

    /**
     * 启动百度地图步行导航(Native)
     *
     */
    public void startWalkingNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapWalkNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 启动百度地图骑行导航(Native)
     *
     */
    public void startBikingNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapBikeNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }
    /**
     * 启动百度地图Poi周边检索
     */
    public void startPoiNearbySearch() {
        LatLng ptCenter = new LatLng(mLat1, mLon1); // 天安门
        PoiParaOption para = new PoiParaOption()
                .key("天安门")
                .center(ptCenter)
                .radius(2000);

        try {
            BaiduMapPoiSearch.openBaiduMapPoiNearbySearch(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 启动百度地图Poi详情页面
     */
    public void startPoiDetails() {
        PoiParaOption para = new PoiParaOption().uid("65e1ee886c885190f60e77ff"); // 天安门

        try {
            BaiduMapPoiSearch.openBaiduMapPoiDetialsPage(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }
    /**
     * 启动百度地图POI全景页面
     */
    public void startPoiPanoShow() {
        try {
            BaiduMapPoiSearch.openBaiduMapPanoShow("65e1ee886c885190f60e77ff", this); // 天安门
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 启动百度地图步行路线规划
     */
    public void startRoutePlanWalking() {
        LatLng ptStart = new LatLng(34.264642646862, 108.95108518068);
        LatLng ptEnd = new LatLng(mLat2, mLon2);

        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(ptStart)
//          .startName("天安门")
//          .endPoint(ptEnd);
                .endName("大雁塔")
                .cityName("西安");

//      RouteParaOption para = new RouteParaOption()
//      .startName("天安门").endName("百度大厦");

//      RouteParaOption para = new RouteParaOption()
//      .startPoint(pt_start).endPoint(pt_end);

        try {
            BaiduMapRoutePlan.openBaiduMapWalkingRoute(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 启动百度地图驾车路线规划
     */
    public void startRoutePlanDriving() {
        LatLng ptStart = new LatLng(34.264642646862, 108.95108518068);
        LatLng ptEnd = new LatLng(mLat2, mLon2);

        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(ptStart)
//            .startName("天安门")
//            .endPoint(ptEnd);
                .endName("大雁塔")
                .cityName("西安");

//        RouteParaOption para = new RouteParaOption()
//                .startName("天安门").endName("百度大厦");

//        RouteParaOption para = new RouteParaOption()
//        .startPoint(pt_start).endPoint(pt_end);

        try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 启动百度地图公交路线规划
     */
    public void startRoutePlanTransit() {
        LatLng ptStart = new LatLng(mLat1, mLon1);
        LatLng ptEnd = new LatLng(mLat2, mLon2);

        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
//            .startPoint(pt_start)
                    .startName("天安门")
                        .endPoint(ptStart)
//            .endName("百度大厦")
                            .busStrategyType(EBusStrategyType.bus_recommend_way);

//        RouteParaOption para = new RouteParaOption()
//                .startName("天安门").endName("百度大厦").busStrategyType(EBusStrategyType.bus_recommend_way);

//        RouteParaOption para = new RouteParaOption()
//        .startPoint(ptStart).endPoint(ptEnd).busStrategyType(EBusStrategyType.bus_recommend_way);

        try {
            BaiduMapRoutePlan.openBaiduMapTransitRoute(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaiduMapNavigation.finish(this);
        BaiduMapRoutePlan.finish(this);
        BaiduMapPoiSearch.finish(this);
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(OpenBaiduMap.this);
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


    private class OpenBaiduMapListAdapter extends BaseAdapter {
        List<String> list;

        public OpenBaiduMapListAdapter(List<String> list) {
            super();
            this.list = list;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(OpenBaiduMap.this,
                    R.layout.demo_info_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            desc.setVisibility(View.INVISIBLE);
            title.setText(list.get(index));

            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int index) {
            return list.get(index);
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }
}
