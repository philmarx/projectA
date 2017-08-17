package com.hzease.tomeet.game.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.TakePhotoActivity;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ImageCropUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/5/31.
 */

public class ComplaintActivity extends TakePhotoActivity {

    @BindView(R.id.tv_home_rooms_complaint_name_act)
    TextView tv_home_rooms_complaint_name_act;
    @BindView(R.id.et_home_room_complaintcontent_fmt)
    EditText et_home_room_complaintcontent_fmt;
    @BindView(R.id.iv_home_room_complaintphoto_fmt)
    ImageView iv_home_room_complaintphoto_fmt;
    @BindView(R.id.iv_home_room_complaintphoto_two_fmt)
    ImageView iv_home_room_complaintphoto_two_fmt;
    @BindView(R.id.iv_home_room_complaintphoto_three_fmt)
    ImageView iv_home_room_complaintphoto_three_fmt;
    private long otherId;
    private String roomId;
    private PopupWindow popupWindow;
    int witchPhoto;
    private Uri uriForFileApiN;
    private List<String> mComplaintPhotos;
    private long millis;

    @OnClick({
            //选择投诉用户
            R.id.rl_home_room_select_fmt,
            //提交投诉
            R.id.bt_home_room_comitcomplaint_fmt,
            R.id.iv_home_room_complaintphoto_fmt,
            R.id.iv_home_room_complaintphoto_two_fmt,
            R.id.iv_home_room_complaintphoto_three_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_room_select_fmt:
                Intent getIntent = getIntent();
                roomId = getIntent.getStringExtra("roomId");
                Intent startIntent = new Intent(this, ChooseUserActivity.class);
                startIntent.putExtra("roomId", roomId);
                Logger.e("roomId" + roomId);
                startActivity(startIntent);
                break;
            case R.id.iv_home_room_complaintphoto_fmt:
                witchPhoto = 1;
                //initPopupWindow();
                millis = System.currentTimeMillis();
                mComplaintPhotos.add(String.valueOf(millis));
                takePhotoPopupWindow(R.id.iv_home_room_complaintphoto_fmt,millis);
                break;
            case R.id.iv_home_room_complaintphoto_two_fmt:
                witchPhoto = 2;
                millis = System.currentTimeMillis();
                mComplaintPhotos.add(String.valueOf(millis));
                takePhotoPopupWindow(R.id.iv_home_room_complaintphoto_two_fmt,millis);
                //nitPopupWindow();

                break;
            case R.id.iv_home_room_complaintphoto_three_fmt:
                witchPhoto = 3;
                millis = System.currentTimeMillis();
                mComplaintPhotos.add(String.valueOf(millis));
                takePhotoPopupWindow(R.id.iv_home_room_complaintphoto_three_fmt,millis);
                //initPopupWindow();
                break;
            case R.id.bt_home_room_comitcomplaint_fmt:
                String complaintContent = et_home_room_complaintcontent_fmt.getText().toString().trim();
                if (complaintContent.isEmpty()) {
                    ToastUtils.getToast(this, "请输入投诉理由");
                    break;
                }
                Gson gson = new Gson();
                String s = gson.toJson(mComplaintPhotos);
                PTApplication.getRequestService().complaintOther(PTApplication.userToken, PTApplication.userId, roomId, otherId, complaintContent, s)
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
                                    ToastUtils.getToast(ComplaintActivity.this, "投诉成功！");
                                    finish();
                                } else {
                                    ToastUtils.getToast(ComplaintActivity.this, "投诉失败，请重新操作");
                                }
                            }
                        });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(GameChatRoomBean.DataBean.JoinMembersBean membersBean) {
        otherId = membersBean.getId();
        String nickName = membersBean.getNickname();
        tv_home_rooms_complaint_name_act.setText(nickName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.e("onstart");
        switch (witchPhoto) {
            case 1:
                iv_home_room_complaintphoto_two_fmt.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_home_room_complaintphoto_three_fmt.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_complaint;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mComplaintPhotos = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}