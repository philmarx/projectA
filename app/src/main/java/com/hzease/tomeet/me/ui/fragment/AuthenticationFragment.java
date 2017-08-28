package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.widget.AlertDialog;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 *
 */

public class AuthenticationFragment extends BaseFragment implements IMeContract.View {

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

    @BindView(R.id.et_me_realname_fmt)
    EditText et_me_realname_fmt;
    @BindView(R.id.et_me_IDCard_fmt)
    EditText et_me_IDCard_fmt;
    @BindView(R.id.bt_me_submit_fmt)
    Button bt_me_submit_fmt;
    private String realName;
    private String IDCard;


    @OnClick(R.id.bt_me_submit_fmt)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_me_submit_fmt:
                realName = et_me_realname_fmt.getText().toString().trim();
                IDCard = et_me_IDCard_fmt.getText().toString().trim();
                MatchUtils.isRealName(realName);
                if (MatchUtils.isIDCard(IDCard)){
                    Logger.e("身份证号码输入正确");
                    final AlertDialog dialog = new AlertDialog(mContext);
                    dialog.builder().setTitle("实名认证信息通过后无法修改，冒用他人信息会导致账号被停封。是否提交")
                            .setPositiveButton("确认提交", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mPresenter.authorized(IDCard,realName,PTApplication.userToken,PTApplication.userId);
                                }
                            })
                            .setNegativeButton("修改信息", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            }).show();

//                    transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(2));
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
//                    transaction.commit();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static AuthenticationFragment newInstance() {
        return new AuthenticationFragment();
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

    @Override
    public void authorizedSuccess() {
        SpUtils.saveString(mContext,"IDCard",IDCard);
        SpUtils.saveString(mContext,"realName",realName);
        EventBus.getDefault().post(realName);
        //meActivity.getSupportFragmentManager().popBackStack();
        //进入认证成功界面
        transaction.replace(R.id.fl_content_me_activity, AuthenSuccess.newInstance());
        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
        //transaction.addToBackStack(null);
        transaction.commit();

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
        return R.layout.fragment_authentication;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
    }
}

