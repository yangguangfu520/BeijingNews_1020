package com.atguigu.beijingnews.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.bean.NewsCenterBean;

import java.util.List;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 14:55
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：左侧菜单
 */
public class LeftMunuFragment extends BaseFragment {
    private TextView textView;
    /**
     * 左侧菜单对应的数据
     */
    private List<NewsCenterBean.DataBean> datas;

    @Override
    public View initView() {
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("左侧菜单——Fragment");
    }

    public void setData(List<NewsCenterBean.DataBean> dataBeanList) {
        this.datas = dataBeanList;

        for (int i=0;i<datas.size();i++){
            Log.e("TAG",datas.get(i).getTitle());
        }
    }
}
