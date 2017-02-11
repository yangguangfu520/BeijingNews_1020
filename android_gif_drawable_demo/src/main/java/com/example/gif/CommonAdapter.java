package com.example.gif;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class CommonAdapter extends BaseAdapter {


    private Context context;
    private List<String> imageUrls;
    private AsyncHttpClient asyncHttpClient;

    public CommonAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        asyncHttpClient = new AsyncHttpClient();
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final GifImageView imageView;
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder= new ViewHolder();
            convertView = View.inflate(context,R.layout.item,null);
            viewHolder.gifImageView = (GifImageView) convertView.findViewById(R.id.gifimageview);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//            imageView = new GifImageView(context);
//            int size = LinearLayout.LayoutParams.MATCH_PARENT;
//            imageView.setPadding(10, 10, 10, 10);
//            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(size, 200);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setBackgroundResource(R.drawable.vedio_default);

            convertView.setTag(viewHolder);


        } else {
//            imageView = (GifImageView) convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.gifImageView .setBackgroundResource(R.drawable.vedio_default);

        viewHolder.tv_title.setText("第--"+position + "--张图片");

        asyncHttpClient
                .get(imageUrls.get(position),
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {


                                try {

                                    GifDrawable drawable = new GifDrawable(bytes);
                                    viewHolder.gifImageView
                                            .setBackground(drawable);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }


                            @Override
                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                System.out.println(throwable.getMessage());
                            }
                        });

        return convertView;
    }

    static class ViewHolder{
        GifImageView gifImageView;
        TextView tv_title;
    }

}
