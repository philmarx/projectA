package com.hzease.tomeet.circle.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/25.
 */

public class CreateCircleFragmentFirst extends BaseFragment implements ICircleContract.View {
    //创建fragment事务管理器对象
    FragmentTransaction transaction;
    CircleActivity mCircleActivity;
    //下一步
    @BindView(R.id.bt_createcircle_next_fmt)
    Button bt_createcircle_next_fmt;
    //未满足实名认证
    @BindView(R.id.tv_createcircle_notrealName_fmt)
    TextView tv_createcircle_notrealName_fmt;
    //满足实名认证
    @BindView(R.id.ll_createcircle_isrealName_fmt)
    LinearLayout ll_createcircle_isrealName_fmt;
    //没有足够的money
    @BindView(R.id.tv_createcircle_notenoughmoney_fmt)
    TextView tv_createcircle_notenoughmoney_fmt;
    //有足够的money
    @BindView(R.id.ll_createcircle_enoughmoney_fmt)
    LinearLayout ll_createcircle_enoughmoney_fmt;

    private ICircleContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    AutoRelativeLayout rl_circle_head;

    @OnClick({
            R.id.bt_createcircle_next_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_createcircle_next_fmt:
                if (tv_createcircle_notrealName_fmt.getVisibility() == View.VISIBLE || tv_createcircle_notenoughmoney_fmt.getVisibility() == View.VISIBLE){
                    ToastUtils.getToast(mContext,"创建圈子条件不足！");
                }else{
                    transaction.replace(R.id.fl_content_bidding_activity, mCircleActivity.mFragmentList.get(5));
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
        }
    }

    public static CreateCircleFragmentFirst newInstance() {
        return new CreateCircleFragmentFirst();
    }

    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_createcircle;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        mCircleActivity = (CircleActivity) getActivity();
        transaction = mCircleActivity.getSupportFragmentManager().beginTransaction();
        rl_circle_head = (AutoRelativeLayout) mCircleActivity.findViewById(R.id.circle_head);
        rl_circle_head.setVisibility(View.GONE);
        bottomNavigationView = (BottomNavigationView) mCircleActivity.findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        if (PTApplication.myInfomation.getData().isAuthorized()){
            ll_createcircle_isrealName_fmt.setVisibility(View.VISIBLE);
            tv_createcircle_notrealName_fmt.setVisibility(View.GONE);
        }else{
            ll_createcircle_isrealName_fmt.setVisibility(View.GONE);
            tv_createcircle_notrealName_fmt.setVisibility(View.VISIBLE);
        }
        if (PTApplication.myInfomation.getData().getAmount() / 100 > 100){
            ll_createcircle_enoughmoney_fmt.setVisibility(View.VISIBLE);
            tv_createcircle_notenoughmoney_fmt.setVisibility(View.GONE);
        }else{
            ll_createcircle_enoughmoney_fmt.setVisibility(View.GONE);
            tv_createcircle_notenoughmoney_fmt.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 创建圈子成功
     */
    @Override
    public void createSuccess(long circleId) {

    }

    /**
     * 展示喊话内容
     *
     * @param isSuccess
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
