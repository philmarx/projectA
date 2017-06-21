package com.hzease.tomeet.me.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.CountDownButtonHelper;
import com.hzease.tomeet.utils.ToastUtils;

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

public class ChangePhoneFragment extends BaseFragment implements IMeContract.View{

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    @BindView(R.id.et_setting_phone_fmt)
    EditText et_setting_phone_fmt;
    @BindView(R.id.tv_setting_bindphone_fmt)
    TextView tv_setting_bindphone_fmt;
    @BindView(R.id.et_setting_bindphone_smsCode_fmt)
    EditText et_setting_bindphone_smsCode_fmt;
    private String phoneMum;
    /**
     * 倒计时开关
     */
    private CountDownButtonHelper helper;
    @OnClick({
            R.id.tv_setting_bindphone_fmt,
            R.id.bt_me_commitphone_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_setting_bindphone_fmt:
                phoneMum = et_setting_phone_fmt.getText().toString().trim();
                PTApplication.getRequestService().getSMSCode(phoneMum)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<StringDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                smsCodeCountdown(new StringDataBean(false, "网络错误获取失败", ""));
                            }

                            @Override
                            public void onNext(StringDataBean stringDataBean) {
                                smsCodeCountdown(stringDataBean);
                            }
                        });
                break;
            case R.id.bt_me_commitphone_fmt:
                String smsCode = et_setting_bindphone_smsCode_fmt.getText().toString().trim();
                PTApplication.getRequestService().bindPhone(phoneMum,smsCode,PTApplication.userToken,PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()){
                                    ToastUtils.getToast(PTApplication.getInstance(),"绑定成功!!!");
                                    SharedPreferences sp = getActivity().getSharedPreferences("game_name", Context.MODE_PRIVATE);
                                    sp.edit().putString("BindingPhone",phoneMum).commit();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            }
                        });
                break;

        }
    }

    private void smsCodeCountdown(StringDataBean stringDataBean) {
        if (stringDataBean.isSuccess()) {
            //成功后开始倒计时
            ToastUtils.getToast(getContext(), "发送验证码成功");
            tv_setting_bindphone_fmt.setTextColor(Color.rgb(184, 184, 184));
            helper = new CountDownButtonHelper(tv_setting_bindphone_fmt, "发送验证码", 60, 1);
            helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                @Override
                public void finish() {
                    tv_setting_bindphone_fmt.setTextColor(Color.rgb(3, 181, 227));
                }
            });
            helper.start();
        } else {
            // 失败提示
            ToastUtils.getToast(getContext(), "获取失败,请稍候重试");
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
    public void showBuyPropsResult(int index,boolean success, String msg) {

    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_changealipay;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    /**
     * 当fragment被销毁的时候，取消计时器
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (helper != null) {
            helper.stop();
        }
    }
}
