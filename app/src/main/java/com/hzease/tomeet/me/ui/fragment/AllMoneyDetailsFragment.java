package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MoneyDetailsBean;
import com.hzease.tomeet.widget.adapters.MoneyDetailsAdapter;

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
                            initDetails(moneyDetailsBean.getData());
                        }
                    }
                });
    }

    private void initDetails(List<MoneyDetailsBean.DataBean> mDatas) {
        lv_me_touchbalance_fmt.setLayoutManager(new LinearLayoutManager(mContext));
        lv_me_touchbalance_fmt.addItemDecoration(new DividerItemDecoration(mContext,1));
        lv_me_touchbalance_fmt.setAdapter(new MoneyDetailsAdapter(mDatas,mContext));
    }


}
