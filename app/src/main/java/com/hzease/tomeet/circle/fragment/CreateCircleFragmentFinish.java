package com.hzease.tomeet.circle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.ShareLocationActivity;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.JoinCircleBean;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/25.
 */

public class CreateCircleFragmentFinish extends BaseFragment implements ICircleContract.View {

    private static final int RESULT_PLACE = 10086;
    //创建fragment事务管理器对象
    FragmentTransaction transaction;
    CircleActivity mCircleActivity;
    @BindView(R.id.et_createcircle_circlename_fmt)
    EditText et_createcircle_circlename_fmt;
    @BindView(R.id.ll_createcircle_location_fmt)
    AutoLinearLayout ll_createcircle_location_fmt;
    @BindView(R.id.bt_createcircle_finish_fmt)
    Button bt_createcircle_finish_fmt;
    @BindView(R.id.tv_createcircle_location_fmt)
    TextView tv_createcircle_location_fmt;
    private ICircleContract.Presenter mPresenter;
    private PopupWindow popupWindow;
    private double mLongitude;
    private double mLatitude;
    private String cityCode;
    private String cityName;

    private String mPlaceName;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    AutoRelativeLayout rl_circle_head;

    @OnClick({
            R.id.bt_createcircle_finish_fmt,
            R.id.ll_createcircle_location_fmt,
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_createcircle_location_fmt:
                Intent openSend = new Intent(getActivity(), ShareLocationActivity.class);
                Logger.e(mLongitude + "");
                Logger.e(mLatitude + "");
                Logger.e(cityCode + "");
                Logger.e(cityName + "");
                Logger.e(PTApplication.userToken + "token");
                openSend.putExtra("lon", mLongitude);
                openSend.putExtra("lat", mLatitude);
                openSend.putExtra("cityCode", cityCode);
                openSend.putExtra("cityName", cityName);
                startActivityForResult(openSend, RESULT_PLACE);
                break;
            case R.id.bt_createcircle_finish_fmt:
                String circleName = et_createcircle_circlename_fmt.getText().toString().trim();
                //创建圈子
                Logger.e("dian");
                mPresenter.createCircle(cityName, mLatitude, mLongitude, circleName, "", mPlaceName, PTApplication.userToken, PTApplication.userId);
                break;
        }
    }

    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static CreateCircleFragmentFinish newInstance() {
        return new CreateCircleFragmentFinish();
    }


    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_createcircle_next;
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
        initLogLat();

    }

    private void initLogLat() {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                mLongitude = aMapLocation.getLongitude();
                mLatitude = aMapLocation.getLatitude();
                cityCode = aMapLocation.getCityCode();
                cityName = aMapLocation.getCity();
            }
        });
    }


    /**
     * 创建圈子成功
     */
    @Override
    public void createSuccess(JoinCircleBean joinCircleBean) {
        //new OssUtils().setCircleImageToView(AppConstants.YY_PT_OSS_CIRCLE_AVATAR, String.valueOf(circleId));
        //ActivityUtils.addFragmentToActivity(mCircleActivity.getSupportFragmentManager(), mCircleActivity.mFragmentList.get(2), R.id.fl_content_bidding_activity);
        /*Intent intent = new Intent(getActivity(), CircleInfoActivity.class);
        intent.putExtra("circleId", circleId);
        startActivity(intent);
        getActivity().finish();*/
        if (joinCircleBean.isSuccess()){
            PTApplication.aliyunOss = null;
            Intent intent = new Intent(getActivity(),CreateCircleActivityFinishInfo.class);
            intent.putExtra("circleId", joinCircleBean.getData().getId());
            startActivity(intent);
            getActivity().finish();
        }else{
            ToastUtils.getToast(joinCircleBean.getMsg());
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PLACE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                String place = data.getStringExtra(ShareLocationActivity.PLACE_NAME);
                mPlaceName = place;
                tv_createcircle_location_fmt.setText(place);
                mLongitude = data.getDoubleExtra(ShareLocationActivity.LONGITUDE, 0);
                mLatitude = data.getDoubleExtra(ShareLocationActivity.LATITUDE, 0);
            }
        }
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
}
