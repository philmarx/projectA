package com.hzease.tomeet.circle.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.EvaluationInfoBean;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.SpacesItemProps;
import com.hzease.tomeet.widget.adapters.MyEvaluationInfoAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/9/18.
 */

public class EvaluationInfoFragment extends BaseFragment {
    BottomNavigationView bottomNavigationView;
    AutoRelativeLayout rl_circle_head;
    @BindView(R.id.rv_evaluation_info_fmt)
    RecyclerView rv_evaluation_info_fmt;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srl_refresh;
    @BindView(R.id.ll_no_message)
    LinearLayout ll_no_message;
    private CircleActivity mCircleActivity;
    //创建fragment事务管理器对象
    private FragmentTransaction transaction;

    int page;
    int size = 15;

    private MyEvaluationInfoAdapter myEvaluationInfoAdapter;
    public static EvaluationInfoFragment newInstance(){
        return new EvaluationInfoFragment();
    }
    public EvaluationInfoFragment() {
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_evaluationinfo;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mCircleActivity = (CircleActivity) getActivity();
        transaction = mCircleActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = (BottomNavigationView) mCircleActivity.findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        rl_circle_head = (AutoRelativeLayout) mCircleActivity.findViewById(R.id.circle_head);
        rl_circle_head.setVisibility(View.GONE);
        myEvaluationInfoAdapter = new MyEvaluationInfoAdapter(mContext);
        rv_evaluation_info_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_evaluation_info_fmt.addItemDecoration(new SpacesItemDecoration(10));
        rv_evaluation_info_fmt.setAdapter(myEvaluationInfoAdapter);
        page = 0;
        PTApplication.getRequestService().getMyEvaluation(PTApplication.userToken,PTApplication.userId,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EvaluationInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EvaluationInfoBean evaluationInfoBean) {
                        initAdapter(evaluationInfoBean,false);
                    }
                });
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        PTApplication.getRequestService().getMyEvaluation(PTApplication.userToken,PTApplication.userId,page,size)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<EvaluationInfoBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(EvaluationInfoBean evaluationInfoBean) {
                                        initAdapter(evaluationInfoBean,false);
                                    }
                                });
                    }
                }, 100);
            }
        });
        rv_evaluation_info_fmt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastCompletelyVisibleItem;
            int firstCompletelyVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 显示最后一条加载
                if (myEvaluationInfoAdapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastCompletelyVisibleItem + 1 == myEvaluationInfoAdapter.getItemCount()
                            && firstCompletelyVisibleItem != 0
                            && myEvaluationInfoAdapter.getmLoadMoreStatus() != myEvaluationInfoAdapter.NO_LOAD_MORE
                            ) {

                        myEvaluationInfoAdapter.changeMoreStatus(myEvaluationInfoAdapter.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PTApplication.getRequestService().getMyEvaluation(PTApplication.userToken,PTApplication.userId,++page,size)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<EvaluationInfoBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(EvaluationInfoBean evaluationInfoBean) {
                                                initAdapter(evaluationInfoBean,true);
                                            }
                                        });
                            }
                        }, 200);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                // 第一个完全可见
                firstCompletelyVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });
    }

    private void initAdapter(final EvaluationInfoBean evaluationInfoBean, boolean isLoadMore) {
        myEvaluationInfoAdapter.setOnItemClickLitener(new MyEvaluationInfoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                OneEvaluationFragment oneEvaluationFragment = OneEvaluationFragment.newInstance();
                Bundle args = new Bundle();
                args.putLong("id",Long.valueOf(evaluationInfoBean.getData().get(position).getDeclarationId()));
                args.putString("nickname",evaluationInfoBean.getData().get(position).getDeclarerName());
                oneEvaluationFragment.setArguments(args);
                transaction.replace(R.id.fl_content_bidding_activity, oneEvaluationFragment);
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        if (srl_refresh != null && !isLoadMore) {
            srl_refresh.setRefreshing(false);
        }
        if (evaluationInfoBean.isSuccess()){
            if (isLoadMore){
                //TODO 加载更多
                Logger.e("" + isLoadMore);
                myEvaluationInfoAdapter.getmData().addAll(evaluationInfoBean.getData());
                //设置回到上拉加载更多
                if (evaluationInfoBean.getData().size() == size) {
                    myEvaluationInfoAdapter.changeMoreStatus(myEvaluationInfoAdapter.PULLUP_LOAD_MORE);
                } else {
                    myEvaluationInfoAdapter.changeMoreStatus(myEvaluationInfoAdapter.NO_LOAD_MORE);
                }
            }else{
                // 刷新或者刚进入
                if (evaluationInfoBean.getData().size() == 0){
                    ll_no_message.setVisibility(View.VISIBLE);
                    srl_refresh.setVisibility(View.GONE);
                }else{
                    ll_no_message.setVisibility(View.GONE);
                    srl_refresh.setVisibility(View.VISIBLE);
                    myEvaluationInfoAdapter.setDatas(evaluationInfoBean.getData());
                    rv_evaluation_info_fmt.scrollToPosition(0);
                }

            }
            myEvaluationInfoAdapter.notifyDataSetChanged();
        }
    }


}
