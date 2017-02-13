package com.baidu.location.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.baidu.baidulocationdemo.R;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.service.LocationService;

/***
 * 展示定位sdk自定义定位模式，注意！设置setOpenAutoNotifyMode后,setScanSpan会失效，回调由setOpenAutoNotifyMode内设置的数值决定
 * @author baidu
 *
 */
public class LocationAutoNotify extends Activity{
	private RadioGroup selectLocMode;
	private EditText distance,time;
	private Button startLoc;
	private LocationService locService;
	private LocationClientOption mOption;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autonotifyconfig);
		selectLocMode = (RadioGroup)findViewById(R.id.autonotify_selectMode);
		distance = (EditText)findViewById(R.id.autonotify_distance);
		time = (EditText)findViewById(R.id.autonotify_time);
		startLoc = (Button)findViewById(R.id.autonotify_start);
		locService =  ((LocationApplication)getApplication()).locationService;
		mOption = new LocationClientOption();
		mOption = locService.getDefaultLocationClientOption();
		mOption.setOpenAutoNotifyMode(); //设置默认值
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startLoc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int setFrequence = 60*1000;
				int	setDistance = 100;
				int setSensitivity = LocationClientOption.LOC_SENSITIVITY_HIGHT;

				switch (selectLocMode.getCheckedRadioButtonId()) {
				case R.id.radio_hight:
					setSensitivity = LocationClientOption.LOC_SENSITIVITY_HIGHT;
					break;
				case R.id.radio_low:
					mOption.setLocationMode(LocationMode.Battery_Saving);
					setSensitivity = LocationClientOption.LOC_SENSITIVITY_MIDDLE;
					break;
				case R.id.radio_device:
					mOption.setLocationMode(LocationMode.Device_Sensors);
					setSensitivity = LocationClientOption.LOC_SENSITIVITY_LOW;
					break;
				default:
					break;
				}
				try {
					setDistance = Integer.parseInt(distance.getText().toString());
				}catch (Exception e){
					setDistance = 100;
				}
				try {
					setFrequence = Integer.parseInt(time.getText().toString());
				}catch (Exception e){
					setFrequence = 60*1000;
				}
				/**
				 * 设置前需停止定位服务，设置后重启定位服务才可以生效
				 */
				mOption.setOpenAutoNotifyMode(setFrequence,setDistance,setSensitivity);
				locService.setLocationOption(mOption);
				Intent locIntent = new Intent(LocationAutoNotify.this, LocationActivity.class);
				locIntent.putExtra("from", 1);
				LocationAutoNotify.this.startActivity(locIntent);
			}
			
		});
	}
	
}
