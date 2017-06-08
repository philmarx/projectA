package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.DepositAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/7.
 */

public class DepositFragment extends BaseFragment {

    @BindView(R.id.rlv_me_deposit_fmt)
    RecyclerView rlv_me_deposit_fmt;
    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    private DepositAdapter adapter;
    private int page;

    public static DepositFragment newInstance() {
        return new DepositFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_deposit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rlv_me_deposit_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_me_deposit_fmt.addItemDecoration(new DividerItemDecoration(mContext,1));
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        adapter = new DepositAdapter(mContext, transaction);
        rlv_me_deposit_fmt.setAdapter(adapter);
        PTApplication.getRequestService().getDeposit(0,15,PTApplication.userToken,PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DepositBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DepositBean depositBean) {
                        if (depositBean.isSuccess()){
                            initAdapter(depositBean.getData(),false);
                        }
                    }
                });
        rlv_me_deposit_fmt.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                PTApplication.getRequestService().getDeposit(++page,15,PTApplication.userToken,PTApplication.userId)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<DepositBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(DepositBean depositBean) {
                                                if (depositBean.isSuccess()){
                                                    initAdapter(depositBean.getData(),true);
                                                }else {
                                                    ToastUtils.getToast(mContext, "数据加载失败，请重试");
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
                lastCompletelyVisibleItem=layoutManager.findLastCompletelyVisibleItemPosition();
                // 第一个完全可见
                firstCompletelyVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });
    }

    private void initAdapter(List<DepositBean.DataBean> data,boolean isLoadMore) {
        if (isLoadMore){
            adapter.getList().addAll(data);
            //设置回到上拉加载更多
            if (data.size() == 15){
                Logger.i("date.size():  " + data.size());
                adapter.changeMoreStatus(DepositAdapter.PULLUP_LOAD_MORE);
            }else{
                adapter.changeMoreStatus(DepositAdapter.NO_LOAD_MORE);
            }
        }else{
            adapter.setList(data);
        }
        adapter.notifyDataSetChanged();
    }
}
