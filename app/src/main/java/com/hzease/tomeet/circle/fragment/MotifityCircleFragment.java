package com.hzease.tomeet.circle.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.JoinCircleBean;
import com.hzease.tomeet.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/21.
 */

public class MotifityCircleFragment extends BaseFragment implements ICircleContract.View {

    @BindView(R.id.et_circlenotices_content_fmt)
    EditText et_circlenotices_content_fmt;
    @BindView(R.id.bt_circlenotices_moditity_fmt)
    Button bt_circlenotices_moditity_fmt;
    private ICircleContract.Presenter mPresenter;
    CircleActivity mCircleActivity;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    private long circleId;

    @OnClick({
            R.id.bt_circlenotices_moditity_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_circlenotices_moditity_fmt:
                String notices = et_circlenotices_content_fmt.getText().toString().trim();
                mPresenter.moditityNotice(circleId,notices, PTApplication.userToken,PTApplication.userId);
                break;
        }
    }
    public static MotifityCircleFragment newInstance() {
        return new MotifityCircleFragment();
    }

    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_motifitycircle_first;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCircleActivity = (CircleActivity) getActivity();
        bottomNavigationView =  getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        String notices = getArguments().getString("notice");
        circleId = getArguments().getLong("circleId");
        et_circlenotices_content_fmt.setText(notices);
    }

    /**
     * 创建圈子成功
     */
    @Override
    public void createSuccess(JoinCircleBean joinCircleBean) {

    }

    /**
     * 展示喊话内容
     *  @param isSuccess
     * @param commentList
     */

    @Override
    public void showDeclaration(boolean isSuccess, List<CommentItemBean.DataBean> commentList, boolean isLoadMore) {

    }

    /**
     * 完成喊话后的展示
     *
     * @param isSuccess
     */
    @Override
    public void showDeclareSucccess(boolean isSuccess, String msg) {

    }

    @Override
    public void refreshOneDeclaration(CommentItemBean.DataBean dataBean) {

    }

    /**
     * 显示推荐圈子
     *
     * @param data
     */
    @Override
    public void showRecommandCircle(List<CircleInfoBean.DataBean> data) {

    }

    /**
     * 显示附近圈子
     *
     * @param data
     */
    @Override
    public void showNeayByCircle(List<CircleInfoBean.DataBean> data) {

    }

    @Override
    public void showCircleInfo(EnterCircleInfoBean.DataBean data) {

    }

    @Override
    public void joinCircleSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 退出圈子成功
     *
     * @param msg
     */
    @Override
    public void signOutCircleSuccess(String msg) {

    }

    /**
     * 修改圈子公告成功
     *
     * @param msg
     */
    @Override
    public void modifitySuccess(String msg) {
        ToastUtils.getToast("修改圈子成功!!!");
        mCircleActivity.getSupportFragmentManager().popBackStack();
    }

    /**
     * 显示圈内房间
     *
     * @param data
     */
    @Override
    public void showRoomsByCircle(List<HomeRoomsBean.DataBean> data) {

    }

    @Override
    public void showMyCircle(List<CircleInfoBean.DataBean> data) {

    }
}
