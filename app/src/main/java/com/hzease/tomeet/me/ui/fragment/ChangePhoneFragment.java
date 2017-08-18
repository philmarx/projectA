package com.hzease.tomeet.me.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.CountDownButtonHelper;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class ChangePhoneFragment extends BaseFragment implements IMeContract.View {

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    @BindView(R.id.et_setting_phone_fmt)
    EditText et_setting_phone_fmt;
    BottomNavigationView bottomNavigationView;
    private FragmentTransaction transaction;
    private MeActivity meActivity;

    /**
     * 倒计时开关
     */
    @OnClick({
            R.id.bt_me_commitphone_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_me_commitphone_fmt:
                final String phoneNum = et_setting_phone_fmt.getText().toString().trim();
                if (MatchUtils.isPhoneNumber(phoneNum)) {
                    PTApplication.getRequestService().getSMSCode(phoneNum)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<StringDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(StringDataBean stringDataBean) {
                                    if (stringDataBean.isSuccess()){
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone", phoneNum);
                                        SmsCodeFragment smsCodeFragment = SmsCodeFragment.newInstance();
                                        smsCodeFragment.setArguments(bundle);
                                        transaction.replace(R.id.fl_content_me_activity, smsCodeFragment);
                                        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                                        //transaction.addToBackStack(null);
                                        transaction.commit();
                                    }else{
                                        ToastUtils.getToast(mContext,stringDataBean.getMsg());
                                    }
                                }
                            });

                }
                /*PTApplication.getRequestService().bindPhone(phoneMum, smsCode, PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("onError" + e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast(PTApplication.getInstance(), "绑定成功!!!");
                                    SharedPreferences sp = getActivity().getSharedPreferences("game_name", Context.MODE_PRIVATE);
                                    sp.edit().putString("BindingPhone", phoneMum).commit();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                } else {
                                    ToastUtils.getToast(mContext, noDataBean.getMsg());
                                    Logger.e(noDataBean.getMsg());
                                }
                            }
                        });*/
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static ChangePhoneFragment newInstance() {
        return new ChangePhoneFragment();
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

    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {

    }

    /**
     * 显示道具数量
     *
     * @param data
     */
    @Override
    public void showPropsMum(PropsMumBean.DataBean data) {

    }

    /**
     * 修改昵称成功
     */
    @Override
    public void showChangeNameSuccess() {

    }

    /**
     * 显示购买道具结果
     *
     * @param success
     * @param msg
     */
    @Override
    public void showBuyPropsResult(int index, boolean success, String msg) {

    }

    @Override
    public void initResult(NoDataBean noDataBean) {

    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_changephone;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
