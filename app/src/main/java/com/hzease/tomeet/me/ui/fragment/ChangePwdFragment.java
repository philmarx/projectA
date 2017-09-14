package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.data.GameFinishBean;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class ChangePwdFragment extends BaseFragment implements IMeContract.View {

    @BindView(R.id.bt_me_changpwd_fmt)
    Button bt_me_changpwd_fmt;
    @BindView(R.id.et_me_olderpwd_fmt)
    EditText et_me_olderpwd_fmt;
    @BindView(R.id.et_me_newpwd_fmt)
    EditText et_me_newpwd_fmt;
    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;


    @OnClick({
            R.id.bt_me_changpwd_fmt,
            R.id.tv_forgetpwd_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_me_changpwd_fmt:
                if (TextUtils.isEmpty(PTApplication.myInfomation.getData().getPhone())) {
                    String olderPwd = et_me_olderpwd_fmt.getText().toString().trim();
                    String newPwd = et_me_newpwd_fmt.getText().toString().trim();
                    if (olderPwd.equals(newPwd)) {
                        PTApplication.getRequestService().updatePwdByToken(newPwd,PTApplication.userToken,PTApplication.userId)
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
                                            ToastUtils.getToast("修改密码成功!!!");
                                            meActivity.getSupportFragmentManager().popBackStack();
                                        }else{
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });
                    }else{
                        ToastUtils.getToast("密码不一致，请重新输入");
                    }
                } else {
                    String olderPwd = et_me_olderpwd_fmt.getText().toString().trim();
                    String newPwd = et_me_newpwd_fmt.getText().toString().trim();
                    mPresenter.updatePwd(olderPwd, newPwd, PTApplication.userToken, PTApplication.userId);
                }
                break;
            case R.id.tv_forgetpwd_fmt:
                transaction.replace(R.id.fl_content_me_activity, ForgetPwdFragmentV2.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static ChangePwdFragment newInstance() {
        return new ChangePwdFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

    }


    @Override
    public void showMyRooms(boolean isSuccess, MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {

    }

    /**
     * 更新密码成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {
        ToastUtils.getToast("修改密码成功!!!");
        meActivity.getSupportFragmentManager().popBackStack();
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
    public void showWaitEvaluateMember(WaitEvaluateV2Bean data) {

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
    public void showChangeNameSuccess(NoDataBean noDataBean) {

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
        return R.layout.fragment_changepwd;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        if (TextUtils.isEmpty(PTApplication.myInfomation.getData().getPhone())) {
            et_me_olderpwd_fmt.setHint("6-16的新密码");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            // 清空数据
            et_me_olderpwd_fmt.setText("");
            et_me_newpwd_fmt.setText("");
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
