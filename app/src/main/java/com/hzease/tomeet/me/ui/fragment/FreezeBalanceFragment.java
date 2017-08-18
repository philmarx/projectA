package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HavaBZmoneyRoomBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.FreezeBalanceRoomAdapter;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/22.
 */

public class FreezeBalanceFragment extends BaseFragment {
    @BindView(R.id.rv_freezebalance_desc_fmt)
    RecyclerView rv_freezebalance_desc_fmt;
    @BindView(R.id.ll_no_freezebalance)
    LinearLayout ll_no_freezebalance;
    BottomNavigationView bottomNavigationView;

    public static FreezeBalanceFragment newInstance() {
        return new FreezeBalanceFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_freezebalance;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        PTApplication.getRequestService().findHaveBZmoneyRoom(PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HavaBZmoneyRoomBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HavaBZmoneyRoomBean havaBZmoneyRoomBean) {
                        initDatas(havaBZmoneyRoomBean.isSuccess(), havaBZmoneyRoomBean.getData());
                    }
                });
        rv_freezebalance_desc_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_freezebalance_desc_fmt.addItemDecoration(new SpacesItemDecoration(20));
    }

    private void initDatas(boolean success, List<HavaBZmoneyRoomBean.DataBean> data) {
        if (success) {
            if (data.size() == 0) {
                ll_no_freezebalance.setVisibility(View.VISIBLE);
            } else {
                FreezeBalanceRoomAdapter adapter = new FreezeBalanceRoomAdapter(data, mContext);
                rv_freezebalance_desc_fmt.setAdapter(adapter);
            }
        } else {
            ToastUtils.getToast("网络链接失败");
        }
    }
}
