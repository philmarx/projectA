package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;

import butterknife.BindView;
import butterknife.OnClick;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class ChangePwdFragment extends BaseFragment implements IMeContract.View{

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
    public void onClick(View v){
     switch (v.getId()){
         case R.id.bt_me_changpwd_fmt:
             String olderPwd = et_me_olderpwd_fmt.getText().toString().trim();
             String newPwd = et_me_newpwd_fmt.getText().toString().trim();
             mPresenter.updatePwd(olderPwd,newPwd, PTApplication.userToken,PTApplication.userId);
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
        ToastUtils.getToast(PTApplication.getInstance(),"修改密码成功!!!");
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
    }
}
