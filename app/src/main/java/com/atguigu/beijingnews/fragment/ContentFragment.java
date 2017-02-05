package com.atguigu.beijingnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 14:55
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：左侧菜单
 */
public class ContentFragment extends BaseFragment {
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        //把view注入到ButterKnife
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        rgMain.check(R.id.rb_news);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
