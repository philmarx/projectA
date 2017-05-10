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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentConfig;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.home.ui.HomeFragment;
import com.hzease.tomeet.widget.adapters.NearByCircleAdapter;
import com.hzease.tomeet.widget.adapters.RecommandCircleAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.circle.ui.SearchCircleActivity;

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
    @BindView(R.id.et_circle_search_fmt)
    EditText et_circle_search_fmt;
    //定义一个集合用来接受View
    private List<View> list = new ArrayList<>();

    //recycleview 测试集合
    private LayoutInflater mInflater;
    @BindView(R.id.cvp_mycircle_fmt)
    ViewPager cvpMycircleFmt;
    private ICircleContract.Presenter mPresenter;
    private RecommandCircleAdapter recommandCircleAdapter;
    private NearByCircleAdapter nearByCircleAdapter;


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
        mPresenter.findRecommand();
        mPresenter.findNearBy(HomeFragment.mLatitude,HomeFragment.mLongitude);
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
        initViewPagerItem();
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_mycircle_fmt.setLayoutManager(linearLayoutManager);
        rv_recommendedcircle_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_recommendedcircle_fmt.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
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


    private void initViewPagerItem() {
      /*  LayoutInflater lf = getActivity().getLayoutInflater().from(getContext());
        for (int i = 0; i < 6; i++) {
            View view1 = lf.inflate(R.layout.item_viewpager_home, null);
            RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.recyclerView);
            recyclerView.setAdapter(new RecyclerView.Adapter<MyCircleFragment.ViewHolder>() {
                @Override
                public MyCircleFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MyCircleFragment.ViewHolder holder, int position) {

                }

                @Override
                public int getItemCount() {
                    *//*if (strings.length<6){
                        return strings.length;
                    }else{
                        return strings.length
                    }*//*
                    return 0;
                }
            });
            list.add(view1);
        }*/

    }

    /**
     * 创建圈子成功
     */
    @Override
    public void createSuccess() {

    }

    /**
     * 展示喊话内容
     *
     * @param commentItemBean
     */
    @Override
    public void showDeclaration(CommentItemBean commentItemBean,boolean isLoadMore) {

    }

    @Override
    public void updateEditTextBodyVisible(int visible, CommentConfig config) {

    }

    /**
     * 完成喊话后的展示
     *
     * @param isSuccess
     */
    @Override
    public void showDeclareSucccess(boolean isSuccess,String msg) {

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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.texttest);
        }
    }
}
