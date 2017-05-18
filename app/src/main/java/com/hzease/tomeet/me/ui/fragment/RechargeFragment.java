package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/5/18.
 */

public class RechargeFragment extends BaseFragment implements IMeContract.View {
    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    @BindView(R.id.cb_recharge_alipay_fmt)
    CheckBox cb_recharge_alipay_fmt;
    @BindView(R.id.cb_recharge_wxpay_fmt)
    CheckBox cb_recharge_wxpay_fmt;
    private MeActivity meActivity;
    private IMeContract.Presenter mPresenter;
    private String pay;


    @OnClick({
            R.id.bt_recharge_success_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_recharge_success_fmt:
                ToastUtils.getToast(getContext(),pay);
                meActivity.getSupportFragmentManager().popBackStack();
                break;
        }
    }
    public static RechargeFragment newInstance() {
        return new RechargeFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示 或 刷新 我的信息
     */
    @Override
    public void showMyInfo() {

    }

    /**
     * 显示我的房间
     *
     * @param myJoinRoomBean
     * @param isLoadMore
     */
    @Override
    public void showMyRooms(MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {

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

    /**
     * 显示结束房间信息
     *
     * @param data
     */
    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {

    }

    /**
     * 显示待评价成员
     *
     * @param data
     */
    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {

    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_recharge;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        cb_recharge_alipay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_recharge_wxpay_fmt.setChecked(false);
                    pay = "支付宝支付成功";
                }
            }
        });
        cb_recharge_wxpay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_recharge_alipay_fmt.setChecked(false);
                    pay = "微信支付成功";
                }
            }
        });
    }
}
