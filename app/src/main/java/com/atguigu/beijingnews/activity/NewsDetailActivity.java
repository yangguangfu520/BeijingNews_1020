package com.atguigu.beijingnews.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.beijingnews.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_menu)
    ImageButton ibMenu;
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.ib_textsize)
    ImageButton ibTextsize;
    @InjectView(R.id.ib_share)
    ImageButton ibShare;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    private String url;
    /**
     * 缓存
     */
    private int tempSize = 2;
    /**
     * 真实文字大小
     */
    private int realSize = tempSize;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);
        url = getIntent().getStringExtra("url");
        //Toast.makeText(this, "url==" + url, Toast.LENGTH_SHORT).show();
        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        //WebView的使用
        webview.loadUrl(url);
//        webview.loadUrl("http://android.atguigu.com/");
        webSettings = webview.getSettings();
        //支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);

//        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        //添加缩放按钮-页面要支持

        webSettings.setBuiltInZoomControls(true);
        //支持双击变大变小-页面支持
        webSettings.setUseWideViewPort(true);
        //设置监听
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);
            }
        });

    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:

                finish();
                break;
            case R.id.ib_textsize:
//                Toast.makeText(this, "设置文字大小", Toast.LENGTH_SHORT).show();
                showChangeTextSizeDialog();
                break;
            case R.id.ib_share:
                //Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                showShare();
                break;
        }
    }

    /**
     * 改变文字大小
     */
    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tempSize = which;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                changeTextSize(realSize);
            }
        });
        builder.show();

    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0://超大字体
//                webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                webSettings.setTextZoom(200);
                break;
            case 1://大字体
                webSettings.setTextZoom(150);
                break;
            case 2://正常字体
                webSettings.setTextZoom(100);
                break;
            case 3://小字体
                webSettings.setTextZoom(75);
                break;
            case 4://超小字体
                webSettings.setTextZoom(50);
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("来自尚硅谷it教育");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://atguigu.com/");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("大王派我来巡山");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://atguigu.com/");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("尚硅谷it教育好");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("尚硅谷it教育");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://atguigu.com/");

// 启动分享GUI
        oks.show(this);
    }
}
