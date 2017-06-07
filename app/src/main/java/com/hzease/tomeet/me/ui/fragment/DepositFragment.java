package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.widget.adapters.DepositAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
                            initAdapter(depositBean.getData());
                        }
                    }
                });
    }

    private void initAdapter(List<DepositBean.DataBean> data) {
        rlv_me_deposit_fmt.setAdapter(new DepositAdapter(data,mContext,transaction));
    }
}
