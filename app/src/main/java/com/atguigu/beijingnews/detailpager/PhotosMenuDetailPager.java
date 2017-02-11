package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.adapter.PhotosMenuDetailPagerAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.bean.PhotosMenuDetailPagerBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/6 11:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：图组详情页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {


    private final NewsCenterBean.DataBean dataBean;
    private PhotosMenuDetailPagerAdapter adapter;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;
    private String url;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        //图组详情页面的视图
        View view = View.inflate(mContext, R.layout.photos_menudetail_pager, null);
        ButterKnife.inject(this, view);//swipe_refresh_layout被实例化了
        //设置下拉多少距离起作用
        swipe_refresh_layout.setDistanceToTriggerSync(100);//设置下拉的距离

        swipe_refresh_layout.setColorSchemeColors(Color.BLUE, Color.RED);//设置不同颜色
        //设置背景的颜色
        swipe_refresh_layout.setProgressBackgroundColorSchemeResource(android.R.color.holo_blue_bright);
        //设置下拉刷新的监听
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(mContext, "我被拉下来了", Toast.LENGTH_SHORT).show();
                getDataFromNet(url);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        url = Constants.BASE_URL + dataBean.getUrl();
        Log.e("TAG", "图组的网络地址=====" + url);
        String saveJson = CacheUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet(url);
    }

    private void getDataFromNet(final String url) {
//        RequestParams params = new RequestParams(url);
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.e("TAG","图组数据请求成功==");
//                CacheUtils.putString(mContext,url,result);
//                processData(result);
//                swipe_refresh_layout.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e("TAG","图组数据请求失败=="+ex.getMessage());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });


        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "atguig")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "okhttp图组数据请求失败==" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "okhttp图组数据请求成功==");
                        CacheUtils.putString(mContext, url, response);
                        processData(response);
                        swipe_refresh_layout.setRefreshing(false);
                    }
                });
    }

    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
        Log.e("TAG", "数组解析数据成功======" + bean.getData().getNews().get(0).getTitle());

        //设置RecyclerView的适配器
        adapter = new PhotosMenuDetailPagerAdapter(mContext, bean.getData().getNews(), recyclerview);
        recyclerview.setAdapter(adapter);

        //设置布局管理器
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

    }

    private boolean isList = true;

    public void swichListGrid(ImageButton ib_swich_list_gird) {
        if (isList) {
            //Grid
            isList = false;
            //设置布局管理器
            recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));

            //按钮设置List效果
            ib_swich_list_gird.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            //List
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            isList = true;
            //按钮设置Grid效果
            ib_swich_list_gird.setImageResource(R.drawable.icon_pic_grid_type);
        }

    }
}
