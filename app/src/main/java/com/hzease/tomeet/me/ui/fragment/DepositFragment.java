package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.DepositAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/7.
 */

public class DepositFragment extends BaseFragment {

    @BindView(R.id.rlv_me_deposit_fmt)
    RecyclerView rlv_me_deposit_fmt;
    @BindView(R.id.ll_no_deposit)
    LinearLayout ll_no_deposit;
    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    @BindView(R.id.tv_mywallet_withdrawals_fmt)
    TextView tv_mywallet_withdrawals_fmt;
    private DepositAdapter adapter;
    private int page;
    List<DepositBean.DataBean> mDatas;

    @OnClick(R.id.tv_mywallet_withdrawals_fmt)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mywallet_withdrawals_fmt:
                int j = 0;
                for (int i = 0; i < mDatas.size(); i++) {
                    float amount = Float.valueOf(mDatas.get(i).getAmount());
                    float refundAmount = Float.valueOf(mDatas.get(i).getRefundAmount());
                    if (amount > refundAmount) {
                        ToastUtils.getToast(mContext, "您还有未退还的保证金，请退还后提现");
                        return;
                    } else {
                        j++;
                    }
                }
                if (j == mDatas.size()) {
                    // 将 fragment_container View 中的内容替换为此 Fragment ，
                    transaction.replace(R.id.fl_content_me_activity, WithdrawalsFragment.newInstance());
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    transaction.addToBackStack(null);
                    // 执行事务
                    transaction.commit();
                }
                break;
        }
    }

    public static DepositFragment newInstance() {
        return new DepositFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_deposit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mDatas = new ArrayList<>();
        rlv_me_deposit_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_me_deposit_fmt.addItemDecoration(new SpacesItemDecoration(5));
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        adapter = new DepositAdapter(mContext, transaction);
        rlv_me_deposit_fmt.setAdapter(adapter);
        PTApplication.getRequestService().getDeposit(0, 15, PTApplication.userToken, PTApplication.userId)
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
                        if (depositBean.isSuccess()) {
                            initAdapter(depositBean.getData(), false);
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
                                PTApplication.getRequestService().getDeposit(++page, 15, PTApplication.userToken, PTApplication.userId)
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
                                                if (depositBean.isSuccess()) {
                                                    initAdapter(depositBean.getData(), true);
                                                } else {
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
                lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                // 第一个完全可见
                firstCompletelyVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });
    }

    private void initAdapter(List<DepositBean.DataBean> data, boolean isLoadMore) {
        if (isLoadMore) {
            mDatas.addAll(data);
            adapter.getList().addAll(data);
            //设置回到上拉加载更多
            if (data.size() == 30) {
                Logger.i("date.size():  " + data.size());
                adapter.changeMoreStatus(DepositAdapter.PULLUP_LOAD_MORE);
            } else {
                adapter.changeMoreStatus(DepositAdapter.NO_LOAD_MORE);
            }
        } else {
            if (data.size() == 0) {
                ll_no_deposit.setVisibility(View.VISIBLE);
            } else {
                mDatas = data;
                adapter.setList(data);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
