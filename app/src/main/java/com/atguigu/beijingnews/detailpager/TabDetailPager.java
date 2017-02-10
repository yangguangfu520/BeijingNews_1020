package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.baselibrary.DensityUtil;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.NewsDetailActivity;
import com.atguigu.beijingnews.adapter.TabDetailPagerAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.bean.TabDetailPagerBean;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/6 15:26
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class TabDetailPager extends MenuDetailBasePager {
    public static final String ID_ARRAY = "id_array";
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;

    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView refreshListView;


    ListView listview;


    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llGroupPoint;


    private String url;
    private TabDetailPagerAdapter adapter;
    private int prePosition;
    /**
     * 列表数据
     */
    private List<TabDetailPagerBean.DataEntity.NewsEntity> news;
    /**
     * 顶部轮播图的数据
     */
    private List<TabDetailPagerBean.DataEntity.TopnewsEntity> topnews;
    /**
     * 更多的路径
     */
    private String moreUrl;

    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //图组详情页面的视图
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        ButterKnife.inject(this, view);//ListView就被实例化了-->PullToRefreshListView被初始化了

        listview = refreshListView.getRefreshableView();
//        Button button =new Button(mContext);
//        button.setText("我是头部的按钮哦");
        View headerView = View.inflate(mContext, R.layout.header_view, null);
        viewpager = (HorizontalScrollViewPager) headerView.findViewById(R.id.viewpager);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        llGroupPoint = (LinearLayout) headerView.findViewById(R.id.ll_group_point);

        listview.addHeaderView(headerView);

        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        refreshListView.setOnPullEventListener(soundListener);

        //设置下拉和上拉刷新
        refreshListView.setOnRefreshListener(new MyOnRefreshListener2());

        //设置ListView的item的点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //得到Bean对象
                TabDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position-2);
                String title = newsEntity.getTitle();
                int  ids = newsEntity.getId();
                Log.e("TAG","tilet=="+title+",id=="+ids);

                //1.获取是否已经存在,如果不存在才保存
                String idArray = CacheUtils.getString(mContext, ID_ARRAY);//""-->1111,
                //如果不包含才保存
                if(!idArray.contains(ids+"")){
                    //保存点击过的item的对应的id
                    CacheUtils.putString(mContext,ID_ARRAY,idArray+ids+",");


                    adapter.notifyDataSetChanged();//getCount-->getView
                    //2.刷新适配器
                }


                //跳转到新闻的浏览页面
                Intent intent = new Intent(mContext,NewsDetailActivity.class);
                intent.putExtra("url",Constants.BASE_URL+newsEntity.getUrl());
                mContext.startActivity(intent);




            }
        });
        return view;
    }

    class MyOnRefreshListener2 implements PullToRefreshBase.OnRefreshListener2<ListView> {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            //Toast.makeText(mContext, "下拉刷新", Toast.LENGTH_SHORT).show();
            isLoadMore = false;
            getDataFromNet();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
           // Toast.makeText(mContext, "上拉刷新", Toast.LENGTH_SHORT).show();
            if(!TextUtils.isEmpty(moreUrl)){
                isLoadMore = true;
                getMoreDataFromNet();
            }else{
                Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                refreshListView.onRefreshComplete();
            }

        }
    }

    /**
     * 请求更多数据
     */
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求更多数据成功==TabDetailPager==" + childrenBean.getTitle());
                processData(result);
                //把下拉刷新和上拉刷新隐藏
                refreshListView.onRefreshComplete();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求更多数据失败==TabDetailPager==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        Log.e("TAG", "TabDetailPager--url==" + url);
        String saveJson = CacheUtils.getString(mContext,url);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        //设置数据
        getDataFromNet();

    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求数据成功==TabDetailPager==" + childrenBean.getTitle());
                CacheUtils.putString(mContext,url,result);
                processData(result);
                //把下拉刷新和上拉刷新隐藏
                refreshListView.onRefreshComplete();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求数据失败==TabDetailPager==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private InternalHandler handler;
    private void processData(String json) {
        TabDetailPagerBean pagerBean = new Gson().fromJson(json, TabDetailPagerBean.class);

        String more = pagerBean.getData().getMore();
        if(TextUtils.isEmpty(more)){
            moreUrl = "";
        }else{
            moreUrl = Constants.BASE_URL + more;
        }

        if(!isLoadMore){
            //原来的代码

            news = pagerBean.getData().getNews();



            //设置适配器
            adapter = new TabDetailPagerAdapter(mContext, news);
            listview.setAdapter(adapter);


            //设置顶部新闻（轮播图）

            //设置Viewpager的适配器
            topnews = pagerBean.getData().getTopnews();
            viewpager.setAdapter(new MyPagerAdapter());
            //监听ViewPager页面的变化
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
            tvTitle.setText(topnews.get(prePosition).getTitle());

            //把之前所有的移除
            llGroupPoint.removeAllViews();
            //添加红点
            for (int i = 0; i < topnews.size(); i++) {


                //添加到线性布局
                ImageView point = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, ViewGroup.LayoutParams.WRAP_CONTENT);
                if(i!= 0){
                    //设置距离左边的距离
                    params.leftMargin = DensityUtil.dip2px(mContext,8);
                    point.setEnabled(false);
                }else{
                    point.setEnabled(true);
                }
                point.setLayoutParams(params);
                //设置图片背景选择器
                point.setBackgroundResource(R.drawable.point_selector);


                llGroupPoint.addView(point);
            }

        }else{
            isLoadMore = false;
            //更多
            news.addAll(pagerBean.getData().getNews());
            adapter.notifyDataSetChanged();


        }


        //顶部轮播图动态切换
        if(handler ==null){
            handler = new InternalHandler();
        }
        //把之前所有消息和任务移除
        handler.removeCallbacksAndMessages(null);

        handler.postDelayed(new MyRunnable(),3000);




    }

    class InternalHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //切换到下个页面
            int item = (viewpager.getCurrentItem()+1)%topnews.size();
            viewpager.setCurrentItem(item);

            handler.postDelayed(new MyRunnable(),3000);
        }
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {

            handler.sendEmptyMessage(0);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //先把之前的变灰
            llGroupPoint.getChildAt(prePosition).setEnabled(false);
            //把当前变高亮
            llGroupPoint.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        @Override
        public void onPageSelected(int position) {
            tvTitle.setText(topnews.get(position).getTitle());


        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state==ViewPager.SCROLL_STATE_DRAGGING){
                handler.removeCallbacksAndMessages(null);
            }else if(state==ViewPager.SCROLL_STATE_IDLE){
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(),3000);
            }

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置默认的和联网请求
            final TabDetailPagerBean.DataEntity.TopnewsEntity topnewsEntity = topnews.get(position);
            //加载图片
            Glide.with(mContext).load(Constants.BASE_URL + topnewsEntity.getTopimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //设置默认图片
                    .placeholder(R.drawable.news_pic_default)
                    //请求图片失败
                    .error(R.drawable.news_pic_default)
                    .into(imageView);
            //添加到ViewPager和返回
            container.addView(imageView);

            //设置触摸事件
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN://按下
                            handler.removeCallbacksAndMessages(null);
                            break;

                        case MotionEvent.ACTION_UP://离开
                            handler.postDelayed(new MyRunnable(),3000);
                            break;
                    }
                    return false;
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到新闻的浏览页面
                    Intent intent = new Intent(mContext,NewsDetailActivity.class);
                    intent.putExtra("url",Constants.BASE_URL+topnewsEntity.getUrl());
                    mContext.startActivity(intent);


                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
