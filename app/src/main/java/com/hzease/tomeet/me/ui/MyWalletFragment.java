package com.hzease.tomeet.me.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomBean;
import com.hzease.tomeet.data.UserOrderBean;
import com.hzease.tomeet.me.IMeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/23.
 */

public class MyWalletFragment extends BaseFragment implements IMeContract.View  {
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.tv_mewallet_amount_fmt)
    TextView tv_mewallet_amount_fmt;
    @BindView(R.id.tv_mewallet_lockamount_fmt)
    TextView tv_mewallet_lockamount_fmt;
    public MyWalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        // mPresenter.start();
    }

    public static MyWalletFragment newInstance() {
        return new MyWalletFragment();
    }
    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {
        tv_mewallet_amount_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getAmount()));
        tv_mewallet_lockamount_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getLockAmount()));
    }
    @Override
    public void showMyRooms(MyJoinRoomBean myJoinRoomBean) {

    }



    /**
     * 更新密码成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 提交反馈成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void feedBackSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 认证成功
     */
    @Override
    public void authorizedSuccess() {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mywallet;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        mPresenter.loadMyInfo();
    }
}
