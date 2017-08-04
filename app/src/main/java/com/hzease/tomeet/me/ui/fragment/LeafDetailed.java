package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.LeafDetailedBean;
import com.hzease.tomeet.widget.adapters.LeafDetailedAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/8/3.
 */

public class LeafDetailed extends BaseFragment {
    @BindView(R.id.lv_me_leaf_detailed_fmt)
    RecyclerView lv_me_leaf_detailed_fmt;
    Unbinder unbinder;

    public static LeafDetailed newInstance(){
        return new LeafDetailed();
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_leaf_detalied;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        PTApplication.getRequestService().findLeafDetails(0, 15, PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LeafDetailedBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LeafDetailedBean leafDetailedBean) {
                        if (leafDetailedBean.isSuccess()) {
                            initRVData(leafDetailedBean.getData());
                        }
                    }
                });
    }

    private void initRVData(List<LeafDetailedBean.DataBean> data) {
        lv_me_leaf_detailed_fmt.setLayoutManager(new LinearLayoutManager(mContext));
        lv_me_leaf_detailed_fmt.addItemDecoration(new DividerItemDecoration(mContext,1));
        lv_me_leaf_detailed_fmt.setAdapter(new LeafDetailedAdapter(data,mContext));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
