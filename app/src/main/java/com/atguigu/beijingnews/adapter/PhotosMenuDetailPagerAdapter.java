package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.bean.PhotosMenuDetailPagerBean;

import java.util.List;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/9 11:42
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class PhotosMenuDetailPagerAdapter extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapter.ViewHolder> {


    private final Context mContext;
    private final List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;

    public PhotosMenuDetailPagerAdapter(Context mContext, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news) {
        this.mContext = mContext;
        this.datas = news;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_photosmenu_detail_pager,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
