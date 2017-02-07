package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.bean.TabDetailPagerBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/7 10:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class TabDetailPagerAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<TabDetailPagerBean.DataEntity.NewsEntity> datas;

    public TabDetailPagerAdapter(Context mContext, List<TabDetailPagerBean.DataEntity.NewsEntity> news) {
        this.mContext = mContext;
        this.datas = news;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_tab_detailpager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        //根据位置得到对应的数据
        TabDetailPagerBean.DataEntity.NewsEntity newsEntity = datas.get(position);
        viewHolder.tvTitle.setText(newsEntity.getTitle());
        viewHolder.tvTime.setText(newsEntity.getPubdate());
        //加载图片
        Glide.with(mContext).load(Constants.BASE_URL+newsEntity.getListimage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default)
                .into(viewHolder.ivIcon);


        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
