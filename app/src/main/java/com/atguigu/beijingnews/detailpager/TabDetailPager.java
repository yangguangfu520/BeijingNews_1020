package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.adapter.TabDetailPagerAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.bean.TabDetailPagerBean;
import com.atguigu.beijingnews.utils.Constants;
import com.google.gson.Gson;

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
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    @InjectView(R.id.listview)
    ListView listview;
    private String url;
    private TabDetailPagerAdapter adapter;
    /**
     * 列表数据
     */
    private List<TabDetailPagerBean.DataEntity.NewsEntity> news;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //图组详情页面的视图
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        Log.e("TAG","TabDetailPager--url=="+url);
        //设置数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","请求数据成功==TabDetailPager=="+childrenBean.getTitle());
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","请求数据失败==TabDetailPager=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String json) {
        TabDetailPagerBean pagerBean = new Gson().fromJson(json,TabDetailPagerBean.class);
        news =pagerBean.getData().getNews();


        //设置适配器
        adapter = new TabDetailPagerAdapter(mContext,news);
        listview.setAdapter(adapter);



    }

}
