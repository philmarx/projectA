package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
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

public class FeedBackFragment extends BaseFragment implements IMeContract.View {
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;

    MeActivity meActivity;
    @BindView(R.id.bt_me_feedback_fmt)
    Button bt_me_feedback_fmt;
    @BindView(R.id.et_me_feedback_fmt)
    EditText et_me_feedback_fmt;
    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }
    @OnClick({
            R.id.bt_me_feedback_fmt
    })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_me_feedback_fmt:
                String content = et_me_feedback_fmt.getText().toString().trim();
                mPresenter.feedBack(content,PTApplication.userToken,PTApplication.userId);
                break;
        }
    }

    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
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
        if (isSuccess){
            ToastUtils.getToast(PTApplication.getInstance(),"提交成功");
            meActivity.getSupportFragmentManager().popBackStack();
        }else{
            ToastUtils.getToast(PTApplication.getInstance(),"提交失败");
        }
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
        return R.layout.fragment_feedback;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
    }
}
