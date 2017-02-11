package com.example.gif;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GifActivity extends Activity {

	private GifImageView network_gifimageview;
	private AsyncHttpClient asyncHttpClient;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gif);
		network_gifimageview = (GifImageView) findViewById(R.id.network_gifimageview);

		dialog = ProgressDialog.show(this, "加载中", "加载网络图片中");
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient
				.get("http://cdn.duitang.com/uploads/item/201311/20/20131120213622_mJCUy.thumb.600_0.gif",
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
												  byte[] arg2) {
								// TODO Auto-generated method stub

								GifDrawable drawable = null;
								try {
									drawable = new GifDrawable(arg2);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								network_gifimageview
										.setBackground(drawable);

								dialog.dismiss();

							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
												  byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(),
										"加载网络图片出错", Toast.LENGTH_SHORT).show();
								dialog.dismiss();

							}
						});
	}

}
