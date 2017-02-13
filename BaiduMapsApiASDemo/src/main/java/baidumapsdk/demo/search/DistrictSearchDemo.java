/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package baidumapsdk.demo.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;

import java.util.List;
import baidumapsdk.demo.R;

public class DistrictSearchDemo extends Activity implements OnGetDistricSearchResultListener, Button.OnClickListener {
    
    private DistrictSearch mDistrictSearch;
    private EditText mCity;
    private EditText mDistrict;
    private MapView mMapView;
    private final int color = 0xAA00FF00;
    private BaiduMap mBaiduMap;
    private Button mSearchButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_search_demo);
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);
        
        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        mCity = (EditText) findViewById(R.id.city);
        mDistrict = (EditText) findViewById(R.id.district);
        mSearchButton = (Button) findViewById(R.id.districSearch);
        mSearchButton.setOnClickListener(this);
    }
    
    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        mBaiduMap.clear();
        if (districtResult == null) {
            return;
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<List<LatLng>> polyLines = districtResult.getPolylines();
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                        .points(polyline).dottedLine(true).color(color);
                mBaiduMap.addOverlay(ooPolyline11);
                OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
                        .stroke(new Stroke(5, 0xAA00FF88)).fillColor(0xAAFFFF00);
                mBaiduMap.addOverlay(ooPolygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                                   .newLatLngBounds(builder.build()));
            
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        mDistrictSearch.destroy();
        super.onDestroy();
    }
    
    @Override
    public void onClick(View v) {
        String city = "";
        String district = "";
        if (mCity.getText() != null && !"".equals(mCity.getText()) ) {
            city = mCity.getText().toString();
        }
        if (mDistrict.getText() != null && !"".equals(mDistrict.getText()) ) {
            district = mDistrict.getText().toString();
        }
        mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(city).districtName(district));
    }
}
