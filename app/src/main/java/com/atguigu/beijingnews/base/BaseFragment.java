package com.atguigu.beijingnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 14:51
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：基类Fragment
 */
public abstract class BaseFragment extends Fragment {
    public Context mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();//MainActivity
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 有子类实现各自的视图
     * @return
     */
    public abstract View initView() ;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 1.绑定数据
     * 2.请求网络得到数据，再绑定数据
     */
    public void initData() {

    }
}
