package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.detailpager.InteractMenuDetailPager;
import com.atguigu.beijingnews.detailpager.NewsMenuDetailPager;
import com.atguigu.beijingnews.detailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnews.detailpager.TopicMenuDetailPager;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/5 15:57
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：新闻中心
 */
public class NewsCenterPager extends BasePager {

    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;
    /**
     * 左侧菜单对应的数据
     */
    private List<NewsCenterBean.DataBean> dataBeanList;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "新闻页面加载数据了");
        //显示菜单按钮
        ib_menu.setVisibility(View.VISIBLE);
        //设置标题
        tv_title.setText("新闻");
        //实例视图
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);

        //和父类的FrameLayout结合
        fl_main.addView(textView);


        //联网请求
        getDataFromNet();
    }

    /**
     * 联网请求数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求成功=="+result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished==");
            }
        });

    }

    /**
     * 解析数据
     * 绑定数据
     *
     * @param json
     */
    private void processData(String json) {
        //1.解析数据：手动解析（用系统的Api解析）和第三方解析json的框架（Gson,fastjson）
//        Gson gson = new Gson();
//        NewsCenterBean centerBean = new Gson().fromJson(json, NewsCenterBean.class);
        NewsCenterBean centerBean = paraseJson(json);
        dataBeanList = centerBean.getData();

        Log.e("TAG", "新闻中心解析成功=" + dataBeanList.get(0).getChildren().get(0).getTitle());
        //把新闻中心的数据传递给左侧菜单
        MainActivity mainActivity = (MainActivity) mContext;
        //得到左侧菜单
        LeftMenuFragment leftMunuFragment = mainActivity.getLeftMenuFragment();

        //2.绑定数据

        menuDetailBasePagers = new ArrayList<>();
        menuDetailBasePagers.add(new NewsMenuDetailPager(mainActivity));//新闻详情页面
        menuDetailBasePagers.add(new TopicMenuDetailPager(mainActivity));//专题详情页面
        menuDetailBasePagers.add(new PhotosMenuDetailPager(mainActivity));//组图详情页面
        menuDetailBasePagers.add(new InteractMenuDetailPager(mainActivity));//互动详情页面
        //调用LeftMunuFragment的setData
        leftMunuFragment.setData(dataBeanList);


//        switchPager(0);

    }

    /**
     * 手动解析json数据使用系统的api
     * @param json
     * @return
     */
    private NewsCenterBean paraseJson(String json) {

        NewsCenterBean centerBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            centerBean.setRetcode(retcode);
            JSONArray data = jsonObject.optJSONArray("data");

            //数据集合
            List<NewsCenterBean.DataBean> dataBeans = new ArrayList<>();
            centerBean.setData(dataBeans);

            for (int i = 0; i < data.length(); i++) {
                JSONObject itemObject = (JSONObject) data.get(i);
                if (itemObject != null) {


                    //集合装数据
                    NewsCenterBean.DataBean itemBean = new NewsCenterBean.DataBean();
                    dataBeans.add(itemBean);

                    int id = itemObject.optInt("id");
                    itemBean.setId(id);
                    String title = itemObject.optString("title");
                    itemBean.setTitle(title);
                    int type = itemObject.optInt("type");
                    itemBean.setType(type);
                    String url = itemObject.optString("url");
                    itemBean.setUrl(url);
                    String url1 = itemObject.optString("url1");
                    itemBean.setUrl1(url1);
                    String excurl = itemObject.optString("excurl");
                    itemBean.setExcurl(excurl);
                    String dayurl = itemObject.optString("dayurl");
                    itemBean.setDayurl(dayurl);
                    String weekurl = itemObject.optString("weekurl");
                    itemBean.setDayurl(weekurl);

                    JSONArray children = itemObject.optJSONArray("children");

                    if (children != null && children.length() > 0) {


                        //设置children的数据
                        List<NewsCenterBean.DataBean.ChildrenBean> childrenBeans = new ArrayList<>();
                        itemBean.setChildren(childrenBeans);
                        for (int j = 0; j < children.length(); j++) {

                            NewsCenterBean.DataBean.ChildrenBean  childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
                            //添加到集合中
                            childrenBeans.add(childrenBean);
                            JSONObject childenObje = (JSONObject) children.get(j);
                            int idc = childenObje.optInt("id");
                            childrenBean.setId(idc);
                            String titlec = childenObje.optString("title");
                            childrenBean.setTitle(titlec);
                            int typec = childenObje.optInt("type");
                            childrenBean.setType(typec);
                            String urlc = childenObje.optString("url");
                            childrenBean.setUrl(urlc);



                        }
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return centerBean;
    }

    /**
     * 更加位置切换到不同的详情页面
     *
     * @param prePosition
     */
    public void switchPager(int prePosition) {
        //设置标题
        tv_title.setText(dataBeanList.get(prePosition).getTitle());


        MenuDetailBasePager menuDetailBasePager = menuDetailBasePagers.get(prePosition);
        //调用
        menuDetailBasePager.initData();
        //视图
        View rootView = menuDetailBasePager.rootView;
        fl_main.removeAllViews();//移除之前的
        fl_main.addView(rootView);


    }
}
