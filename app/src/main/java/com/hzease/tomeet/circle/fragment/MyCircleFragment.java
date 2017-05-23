package com.hzease.tomeet.circle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.circle.ui.SearchCircleActivity;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.widget.adapters.MyCirclePage1Adapter;
import com.hzease.tomeet.widget.adapters.NearByCircleAdapter;
import com.hzease.tomeet.widget.adapters.RecommandCircleAdapter;
import com.hzease.tomeet.widget.adapters.TypeTwoAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/18.
 */

public class MyCircleFragment extends BaseFragment implements ICircleContract.View {


    //创建fragment事务管理器对象
    FragmentTransaction transaction;
    CircleActivity mCircleActivity;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    /**
     * 创建头部布局对象
     */
    AutoRelativeLayout rl_circle_head;
    @BindView(R.id.rv_mycircle_fmt)
    RecyclerView rv_mycircle_fmt;
    @BindView(R.id.rv_recommendedcircle_fmt)
    RecyclerView rv_recommendedcircle_fmt;
    //定义一个集合用来接受View
    private List<View> list = new ArrayList<>();

    //recycleview 测试集合
    private LayoutInflater mInflater;
    @BindView(R.id.cvp_mycircle_fmt)
    ViewPager cvpMycircleFmt;
    private ICircleContract.Presenter mPresenter;
    private RecommandCircleAdapter recommandCircleAdapter;
    private NearByCircleAdapter nearByCircleAdapter;
    private List<CircleInfoBean.DataBean> page1List;
    private List<CircleInfoBean.DataBean> page2List;


    @OnClick({
            R.id.et_circle_search_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.et_circle_search_fmt:
                startActivity(new Intent(getActivity(), SearchCircleActivity.class));
                break;
        }
    }
    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static MyCircleFragment newInstance() {
        return new MyCircleFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mPresenter.findMyCircle(0,12, PTApplication.userToken,PTApplication.userId);
        mPresenter.findRecommand();
        mPresenter.findNearBy(PTApplication.myLatitude,PTApplication.myLongitude);
        mCircleActivity = (CircleActivity) getActivity();
        //设置所在activity的头布局和底部导航栏不可见
        rl_circle_head = (AutoRelativeLayout) mCircleActivity.findViewById(R.id.circle_head);
        if (rl_circle_head.getVisibility() == View.GONE) {
            rl_circle_head.setVisibility(View.VISIBLE);
        }
        bottomNavigationView = (BottomNavigationView) mCircleActivity.findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        transaction = mCircleActivity.getSupportFragmentManager().beginTransaction();
        mInflater = LayoutInflater.from(getContext());
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_mycircle_fmt.setLayoutManager(linearLayoutManager);
        rv_recommendedcircle_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_recommendedcircle_fmt.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
    }


    private void initViewPagerItem(List<CircleInfoBean.DataBean> data) {
        Logger.e(data.size()+"initViewPagerItem");
        LayoutInflater lf = getActivity().getLayoutInflater().from(getContext());
        if (data.size()>6){
            initHave2Page(lf,data);
        }else{
            initHave1Page(lf,data);
        }
    }

    /**
     * 加载一页recycleview的数据
     * @param lf
     * @param data
     */
    private void initHave1Page(LayoutInflater lf, final List<CircleInfoBean.DataBean> data) {
        Logger.e("initHave1Page");
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        View view = lf.inflate(R.layout.item_viewpager_home, null);
        RecyclerView recyclerViewPage1 = (RecyclerView) view.findViewById(R.id.myCircleRv);
        recyclerViewPage1.setLayoutManager(mLayoutManager);
        MyCirclePage1Adapter myCirclePage1Adapter = new MyCirclePage1Adapter(data,getContext());
        myCirclePage1Adapter.setOnItemClickLitener(new TypeTwoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putLong("circleId",data.get(position).getId());
                mCircleActivity.mFragmentList.get(2).setArguments(bundle);
                transaction.replace(R.id.fl_content_bidding_activity, mCircleActivity.mFragmentList.get(2));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        recyclerViewPage1.setAdapter(myCirclePage1Adapter);
        list.add(view);
        Logger.e(list.size()+"initHave1Page");
    }

    /**
     *加载两页recycleview的数据
     * @param lf
     */
    private void initHave2Page(LayoutInflater lf,List<CircleInfoBean.DataBean> data) {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager mLayoutManagertwo = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        View view1 = lf.inflate(R.layout.item_viewpager_home, null);
        RecyclerView recyclerViewPage1 = (RecyclerView) view1.findViewById(R.id.myCircleRv);
        recyclerViewPage1.setLayoutManager(mLayoutManager);
        View view2 = lf.inflate(R.layout.item_viewpager_two, null);
        RecyclerView recyclerViewPage2 = (RecyclerView) view2.findViewById(R.id.recyclerView1);
        recyclerViewPage2.setLayoutManager(mLayoutManagertwo);
        page1List = new ArrayList<>();
        page2List = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            page1List.add(data.get(i));
        }
        for (int i = 6; i < data.size(); i++) {
            page2List.add(data.get(i));
        }
        MyCirclePage1Adapter myCirclePage1Adapter = new MyCirclePage1Adapter(page1List,getContext());
        myCirclePage1Adapter.setOnItemClickLitener(new TypeTwoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putLong("circleId",page1List.get(position).getId());
                mCircleActivity.mFragmentList.get(2).setArguments(bundle);
                transaction.replace(R.id.fl_content_bidding_activity, mCircleActivity.mFragmentList.get(2));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        recyclerViewPage1.setAdapter(myCirclePage1Adapter);
        MyCirclePage1Adapter myCirclePage2Adapter = new MyCirclePage1Adapter(page2List,getContext());
        myCirclePage2Adapter.setOnItemClickLitener(new TypeTwoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putLong("circleId",page2List.get(position).getId());
                mCircleActivity.mFragmentList.get(2).setArguments(bundle);
                transaction.replace(R.id.fl_content_bidding_activity, mCircleActivity.mFragmentList.get(2));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        recyclerViewPage2.setAdapter(myCirclePage2Adapter);
        list.add(view1);
        list.add(view2);
        Logger.e(list.size()+"initHave2Page");
    }

    /**
     * 创建圈子成功
     */
    @Override
    public void createSuccess() {

    }

    /**
     * 展示喊话内容
     *  @param isSuccess
     * @param commentList
     */
    @Override
    public void showDeclaration(boolean isSuccess, List<CommentItemBean.DataBean> commentList, boolean isLoadMore) {

    }

    /**
     * 完成喊话后的展示
     *
     * @param isSuccess
     */
    @Override
    public void showDeclareSucccess(boolean isSuccess,String msg) {

    }

    @Override
    public void onDestroyView() {
        list.clear();
        Logger.e("onDestroyView");
        super.onDestroyView();
    }

    /**
     * 显示推荐圈子
     *
     * @param data
     */
    @Override
    public void showRecommandCircle(final List<CircleInfoBean.DataBean> data) {
        recommandCircleAdapter = new RecommandCircleAdapter(getContext(),data);
        recommandCircleAdapter.setOnItemClickLitener(new RecommandCircleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putLong("circleId",data.get(position).getId());
                mCircleActivity.mFragmentList.get(2).setArguments(bundle);
                transaction.replace(R.id.fl_content_bidding_activity, mCircleActivity.mFragmentList.get(2));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        rv_recommendedcircle_fmt.setAdapter(recommandCircleAdapter);
    }

    /**
     * 显示附近圈子
     *
     * @param data
     */
    @Override
    public void showNeayByCircle(final List<CircleInfoBean.DataBean> data) {
        nearByCircleAdapter = new NearByCircleAdapter(data,getContext());
        nearByCircleAdapter.setOnItemClickLitener(new NearByCircleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putLong("circleId",data.get(position).getId());
                mCircleActivity.mFragmentList.get(2).setArguments(bundle);
                transaction.replace(R.id.fl_content_bidding_activity, mCircleActivity.mFragmentList.get(2));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        rv_mycircle_fmt.setAdapter(nearByCircleAdapter);
    }

    @Override
    public void showCircleInfo(EnterCircleInfoBean.DataBean data) {

    }

    @Override
    public void joinCircleSuccess(String msg) {

    }

    /**
     * 退出圈子成功
     *
     * @param msg
     */
    @Override
    public void signOutCircleSuccess(String msg) {

    }

    /**
     * 修改圈子公告成功
     *
     * @param msg
     */
    @Override
    public void modifitySuccess(String msg) {

    }

    /**
     * 显示圈内房间
     *
     * @param data
     */
    @Override
    public void showRoomsByCircle(List<HomeRoomsBean.DataBean> data) {

    }

    /**
     * 显示我的圈子
     * @param data
     */
    @Override
    public void showMyCircle(List<CircleInfoBean.DataBean> data) {
        Logger.e(data.size()+"");
        initViewPagerItem(data);
        cvpMycircleFmt.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position), 0);
                return list.get(position);
            }
        });
    }
}
