package com.atguigu.androidandh5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static com.atguigu.androidandh5.R.id.webview;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/28 11:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：java和js互调
 */
public class JsCallJavaVideoActivity extends Activity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_java_video);
        webView = (WebView) findViewById(webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js


        //添加javascript接口
        webView.addJavascriptInterface(new MyJavaScript(),"android");

        //设置客户端
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Toast.makeText(JsCallJavaVideoActivity.this, "页面加载完成", Toast.LENGTH_SHORT).show();
            }
        });

        //加载本地的html或者网络的html
        webView.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.htm");
//        webView.loadUrl("http://10.0.2.2:8080/assets/RealNetJSCallJavaActivity.htm");

    }

    class MyJavaScript{
        @JavascriptInterface
        public void  playVideo(int id,String videourl ,String title){
            //Toast.makeText(JsCallJavaVideoActivity.this, "videourl="+videourl+",title="+title, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(videourl),"video/*");
            startActivity(intent);


        }
    }

}
