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
import java.util.concurrent.BlockingDeque;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/7/27.
 */

public class NoLateActivity extends TakePhotoActivity {
    @BindView(R.id.tv_home_rooms_prove_person_act)
    TextView tv_home_rooms_prove_person_act;
    @BindView(R.id.et_home_room_notlatecontent_fmt)
    EditText et_home_room_notlatecontent_fmt;
    @BindView(R.id.iv_home_room_notlatephoto_fmt)
    ImageView iv_home_room_notlatephoto_fmt;
    @BindView(R.id.iv_home_room_notlatephoto_two_fmt)
    ImageView iv_home_room_notlatephoto_two_fmt;
    @BindView(R.id.iv_home_room_notlatephoto_three_fmt)
    ImageView iv_home_room_notlatephoto_three_fmt;
    @BindView(R.id.bt_home_room_notlate_fmt)
    Button bt_home_room_notlate_fmt;
    private String roomId;
    private int witchPhoto;
    private PopupWindow popupWindow;
    private Uri uriForFileApiN;
    private List<String> mNotLatePhotos = new ArrayList<>();
    private long otherId = 0;


    @OnClick({
            R.id.tv_home_rooms_prove_person_act,
            R.id.bt_home_room_notlate_fmt,
            R.id.iv_home_room_notlatephoto_fmt,
            R.id.iv_home_room_notlatephoto_two_fmt,
            R.id.iv_home_room_notlatephoto_three_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_home_rooms_prove_person_act:
                Intent getIntent = getIntent();
                roomId = getIntent.getStringExtra("roomId");
                Intent startIntent = new Intent(this, ChooseUserActivity.class);
                startIntent.putExtra("roomId", roomId);
                Logger.e("roomId" + roomId);
                startActivity(startIntent);
                break;
            case R.id.bt_home_room_notlate_fmt:
                if (otherId == 0){
                    ToastUtils.getToast(this,"请选择为你证明未迟到的用户");
                    break;
                }
                if (et_home_room_notlatecontent_fmt.getText().toString().isEmpty()){
                    ToastUtils.getToast(this,"请输入未签到理由");
                    break;
                }
                Gson gson = new Gson();
                String s = gson.toJson(mNotLatePhotos);
                PTApplication.getRequestService().noLateWithReason(String.valueOf(otherId),s,et_home_room_notlatecontent_fmt.getText().toString().trim(),
                        roomId,PTApplication.userToken,PTApplication.userId)
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
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast(NoLateActivity.this, "操作成功！");
                                    finish();
                                } else {
                                    ToastUtils.getToast(NoLateActivity.this,noDataBean.getMsg());
                                }
                            }
                        });
                break;
            case R.id.iv_home_room_notlatephoto_fmt:
                witchPhoto = 1;
                takePhotoPopupWindow(R.id.iv_home_room_notlatephoto_fmt);
                break;
            case R.id.iv_home_room_notlatephoto_two_fmt:
                witchPhoto = 2;
                takePhotoPopupWindow(R.id.iv_home_room_notlatephoto_two_fmt);
                break;
            case R.id.iv_home_room_notlatephoto_three_fmt:
                witchPhoto = 3;
                takePhotoPopupWindow(R.id.iv_home_room_notlatephoto_three_fmt);
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.e("onstart");
        switch (witchPhoto) {
            case 1:
                iv_home_room_notlatephoto_two_fmt.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_home_room_notlatephoto_three_fmt.setVisibility(View.VISIBLE);
                break;
        }
    }
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_nolate;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(GameChatRoomBean.DataBean.JoinMembersBean membersBean) {
        otherId = membersBean.getId();
        String nickName = membersBean.getNickname();
        tv_home_rooms_prove_person_act.setText(nickName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
