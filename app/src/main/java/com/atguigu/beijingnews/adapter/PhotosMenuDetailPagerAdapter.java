package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.baselibrary.BitmapCacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.baselibrary.NetCacheUtils;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.PicassoSampleActivity;
import com.atguigu.beijingnews.bean.PhotosMenuDetailPagerBean;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/9 11:42
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class PhotosMenuDetailPagerAdapter extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapter.ViewHolder> {


    private final RecyclerView recyclerview;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SECUSS://图片请求成功
                    //位置
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if(recyclerview.isShown()){
                        ImageView ivIcon = (ImageView) recyclerview.findViewWithTag(position);
                        if(ivIcon != null&& bitmap != null){
                            Log.e("TAG","网络缓存图片显示成功"+position);
                            ivIcon.setImageBitmap(bitmap);
                        }
                    }

                    break;

                case NetCacheUtils.FAIL://图片请求失败
                    position = msg.arg1;
                    Log.e("TAG","网络缓存失败"+position);
                    break;
            }
        }
    };
    private  Context mContext;
    private  List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;
    private BitmapCacheUtils bitmapCacheUtils;
    public PhotosMenuDetailPagerAdapter(Context mContext, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news, RecyclerView recyclerview) {
        this.mContext = mContext;
        this.datas = news;
        this.recyclerview = recyclerview;
        bitmapCacheUtils = new BitmapCacheUtils(handler);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_photosmenu_detail_pager, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //1.根据位置得到数据
        PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = datas.get(position);
        holder.tvTitle.setText(newsEntity.getTitle());
        //设置图片
        //Glide加载图片
//        Glide.with(mContext).load(Constants.BASE_URL+newsEntity.getListimage())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.news_pic_default)
//                .error(R.drawable.news_pic_default)
//                .into(holder.ivIcon);
        //设置标识
        holder.ivIcon.setTag(position);
        Bitmap bitmap = bitmapCacheUtils.getBitmapFromNet(Constants.BASE_URL+newsEntity.getListimage(),position);
        if(bitmap != null){//内存或者本地
            holder.ivIcon.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            //设置点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, PicassoSampleActivity.class);
                    intent.putExtra("url",Constants.BASE_URL+datas.get(getLayoutPosition()).getListimage());
                    mContext.startActivity(intent);


                }
            });
        }
    }

}
