package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.data.MoneyDetailsBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.DepositAdapter;
import com.hzease.tomeet.widget.adapters.MoneyDetailsAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/5.
 */

public class AllMoneyDetailsFragment extends BaseFragment {
    @BindView(R.id.lv_me_touchbalance_fmt)
    RecyclerView lv_me_touchbalance_fmt;
    @BindView(R.id.ll_no_moneydetails)
    LinearLayout ll_no_moneydetails;
    private MoneyDetailsAdapter adapter;
    private int page = 0;


    public static AllMoneyDetailsFragment newInstance(){
        return new AllMoneyDetailsFragment();
    }
    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_touchbalance;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        lv_me_touchbalance_fmt.setLayoutManager(new LinearLayoutManager(mContext));
        lv_me_touchbalance_fmt.addItemDecoration(new DividerItemDecoration(mContext,1));
        adapter = new MoneyDetailsAdapter(mContext);
        lv_me_touchbalance_fmt.setAdapter(adapter);
        PTApplication.getRequestService().getDetails(0,15,PTApplication.userToken,PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MoneyDetailsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MoneyDetailsBean moneyDetailsBean) {
                        if (moneyDetailsBean.isSuccess()){
                            initDetails(moneyDetailsBean.getData(),false);
                        }
                    }
                });
        lv_me_touchbalance_fmt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastCompletelyVisibleItem;
            int firstCompletelyVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastCompletelyVisibleItem + 1 == adapter.getItemCount()
                            && firstCompletelyVisibleItem != 0
                            && adapter.getmLoadMoreStatus() != DepositAdapter.NO_LOAD_MORE
                            ) {
                        adapter.changeMoreStatus(DepositAdapter.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PTApplication.getRequestService().getDetails(++page,15,PTApplication.userToken,PTApplication.userId)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<MoneyDetailsBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(MoneyDetailsBean moneyDetailsBean) {
                                                if (moneyDetailsBean.isSuccess()){
                                                    initDetails(moneyDetailsBean.getData(),true);
                                                }
                                            }
                                        });
                            }
                        }, 500);
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

    private void initDetails(List<MoneyDetailsBean.DataBean> data,boolean isLoadMore) {
        if (isLoadMore){
            adapter.getList().addAll(data);
            if (data.size() == 30) {
                Logger.i("date.size():  " + data.size());
                adapter.changeMoreStatus(DepositAdapter.PULLUP_LOAD_MORE);
            } else {
                adapter.changeMoreStatus(DepositAdapter.NO_LOAD_MORE);
            }
        }else {
            if (data.size() == 0) {
                ll_no_moneydetails.setVisibility(View.VISIBLE);
            } else {
                adapter.setList(data);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
