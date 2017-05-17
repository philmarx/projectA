package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.EvaluteBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.WaitEvaluateAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/5/11.
 */

public class GameEvaluateFragment extends BaseFragment implements IMeContract.View {


    @BindView(R.id.rv_gameevaluate_show_fmt)
    RecyclerView rv_gameevaluate_show_fmt;
    @BindView(R.id.bt_evaluate_submit_fmt)
    Button bt_evaluate_submit_fmt;
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    private WaitEvaluateAdapter adapter;
    private long roomId;

    public static GameEvaluateFragment newInstance() {
        return new GameEvaluateFragment();
    }

    @OnClick({
            R.id.bt_evaluate_submit_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_evaluate_submit_fmt:
                EvaluteBean evaluteBean = adapter.getEvaluteBean();
                PTApplication.getRequestService().evaluteGame(evaluteBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {
                                Logger.e("EvaluteBean   onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                ToastUtils.getToast(getContext(),"评论状态" + noDataBean.isSuccess());
                                if (noDataBean.isSuccess()){
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }else{
                                    ToastUtils.getToast(getContext(),noDataBean.getMsg());
                                }
                            }
                        });
                break;
        }
    }
    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

    }

    @Override
    public void showMyRooms(MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {

    }

    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public void feedBackSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public void authorizedSuccess() {

    }

    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {

    }

    /**
     * 显示待评价成员
     * @param data
     */
    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {
        adapter = new WaitEvaluateAdapter(data,getContext(),roomId);
        rv_gameevaluate_show_fmt.setAdapter(adapter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_evaluate;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        roomId = getArguments().getLong("roomId");
        rv_gameevaluate_show_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_gameevaluate_show_fmt.addItemDecoration(new SpacesItemDecoration(20));
        Logger.e("userId" + PTApplication.userId+"\ntoken:"+PTApplication.userToken + "\nroomId" + roomId);
        mPresenter.waitEvaluate(roomId, PTApplication.userToken,PTApplication.userId);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
