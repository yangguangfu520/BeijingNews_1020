package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 15:41
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：视图的基类
 * HomePager、NewsCenterPager、SettingPager都继承该类
 * 在子类重新initData方法，实现子类的视图，并且视图在该方法中和基类的Fragmelayout布局结合在一起
 */
public class BasePager {

    /**
     * 上下文
     */
    public final Context mContext;
    public ImageButton ib_menu;
    public TextView tv_title;
    public FrameLayout fl_main;
    public ImageButton ib_swich_list_gird;
    /**
     * 代表各个页面的实例，视图
     */
    public View rootView;
    public BasePager(Context context){
        this.mContext = context;

        rootView = initView();
    }

    private View initView() {
        View view = View.inflate(mContext, R.layout.basepager,null);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        fl_main = (FrameLayout) view.findViewById(R.id.fl_main);
        ib_swich_list_gird = (ImageButton) view.findViewById(R.id.ib_swich_list_gird);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().toggle();//关<->开
            }
        });
        return view;
    }

    /**
     * 1.在子类重新initData方法，实现子类的视图，并且视图在该方法中和基类的Fragmelayout布局结合在一起
       2.绑定数据或者请求数据再绑定数据
     */
    public void  initData(){

    }
}
