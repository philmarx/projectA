package com.hzease.tomeet.me.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.TakePhotoActivity;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 * 2017年7月28日 13:56:16
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
    @BindView(R.id.iv_feedback_photo_one)
    ImageView iv_feedback_photo;
    @BindView(R.id.iv_feedback_photo_two)
    ImageView iv_feedback_photo_two;
    @BindView(R.id.iv_feedback_photo_three)
    ImageView iv_feedback_photo_three;

    List<String> mFeedBackPhotos = new ArrayList<>();
    private int witchPhoto;
    private long millis;

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    @OnClick({
            R.id.bt_me_feedback_fmt,
            R.id.iv_feedback_photo_one,
            R.id.iv_feedback_photo_two,
            R.id.iv_feedback_photo_three
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_me_feedback_fmt:
                String content = et_me_feedback_fmt.getText().toString().trim();
                Gson gson = new Gson();
                String s = gson.toJson(mFeedBackPhotos);
                Logger.e("millis" + s);
                mPresenter.feedBack(content, PTApplication.userToken, PTApplication.userId, s);
                break;
            case R.id.iv_feedback_photo_one:
                witchPhoto = 1;
                millis = System.currentTimeMillis();
                mFeedBackPhotos.add(((TakePhotoActivity) getActivity()).imageName.get(view.getId()).replaceFirst("/",""));
                ((TakePhotoActivity) getActivity()).takePhotoPopupWindow(view.getId());
                break;
            case R.id.iv_feedback_photo_two:
                witchPhoto = 2;
                millis = System.currentTimeMillis();
                mFeedBackPhotos.add(((TakePhotoActivity) getActivity()).imageName.get(view.getId()).replaceFirst("/",""));
                ((TakePhotoActivity) getActivity()).takePhotoPopupWindow(view.getId());
                break;
            case R.id.iv_feedback_photo_three:
                witchPhoto = 3;
                millis = System.currentTimeMillis();
                mFeedBackPhotos.add(((TakePhotoActivity) getActivity()).imageName.get(view.getId()).replaceFirst("/",""));
                ((TakePhotoActivity) getActivity()).takePhotoPopupWindow(view.getId());
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.e("onstart");
        switch (witchPhoto) {
            case 1:
                iv_feedback_photo_two.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_feedback_photo_three.setVisibility(View.VISIBLE);
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
        if (isSuccess) {
            ToastUtils.getToast(mContext, "提交成功");
            meActivity.getSupportFragmentManager().popBackStack();
        } else {
            ToastUtils.getToast(mContext, "提交失败");
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
    public void showBuyPropsResult(int index, boolean success, String msg) {
    }

    @Override
    public void initResult(NoDataBean noDataBean) {

    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
    }


 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        ((TakePhotoActivity) getActivity()).onActivityResult(requestCode, resultCode, intent);
        Logger.e("requestCode" + requestCode + "\nresultCode" + resultCode);
        if (requestCode == AppConstants.REQUEST_CODE_CROP && resultCode == Activity.RESULT_OK) {
            mFeedBackPhotos.add(((TakePhotoActivity) getActivity()).imageName.get(((TakePhotoActivity) getActivity()).imageViewCheckedId).substring(1));
            if (iv_feedback_photo_two.getVisibility() != View.VISIBLE && ((TakePhotoActivity) getActivity()).imageViewCheckedId == R.id.iv_feedback_photo_one) {
                iv_feedback_photo_two.setVisibility(View.VISIBLE);
            } else if (iv_feedback_photo_two.getVisibility() == View.VISIBLE && ((TakePhotoActivity) getActivity()).imageViewCheckedId == R.id.iv_feedback_photo_two) {
                if (iv_feedback_photo_three.getVisibility() != View.VISIBLE) {
                    iv_feedback_photo_three.setVisibility(View.VISIBLE);
                }
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getActivity().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
