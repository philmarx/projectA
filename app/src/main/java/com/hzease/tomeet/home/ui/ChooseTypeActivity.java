package com.hzease.tomeet.home.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CreateRoomBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/10/31.
 */

public class ChooseTypeActivity extends NetActivity {
    @BindView(R.id.ll_choose)
    LinearLayout ll_choose;
    private boolean isAnonyity = false;

    //点击事件
    @OnClick({
            R.id.ll_create_chatroom,
            R.id.ll_create_activity,
            R.id.rl_chose_activity_bg
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_create_chatroom:
                //创建聊天室
                ll_choose.setVisibility(View.GONE);
                createChatRoomPop(view);
                break;
            case R.id.ll_create_activity:
                // 创建活动
                Intent intent = new Intent(this, CreateRoomBeforeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("circleId", 0);
                bundle.putBoolean("isOpen", true);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_chose_activity_bg:
                finish();
                break;

        }
    }

    //打开创建聊天室
    private void createChatRoomPop(View view) {

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_create_chatroom, null);
        final PopupWindow popupWindow4CreateChatRoom = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow4CreateChatRoom.setFocusable(true);
        popupWindow4CreateChatRoom.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow4CreateChatRoom.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
        //聊天室名称
        final EditText chatRoomName = contentView.findViewById(R.id.et_createchatroom_name_pop);
        //开始匿名
        final CheckBox anonymity = contentView.findViewById(R.id.cb_createchatroom_anonymity_pop);
        //关闭匿名
        final CheckBox noAnonymity = contentView.findViewById(R.id.cb_createchatroom_noanonymity_pop);
        //取消
        Button cancel = contentView.findViewById(R.id.bt_createchatroom_cancel);
        //创建
        final Button createChatRoom = contentView.findViewById(R.id.bt_createachatroom_pop);

        /**
         * 点击事件
         */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow4CreateChatRoom.dismiss();
            }
        });

        anonymity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    noAnonymity.setChecked(false);
                    isAnonyity = true;
                }
            }
        });

        noAnonymity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    anonymity.setChecked(false);
                    isAnonyity = false;
                }
            }
        });

        //创建
        createChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatRoomName.getText().toString().trim().isEmpty()){
                    ToastUtils.getToast("请输入聊天室名称");
                    return;
                }
                if (anonymity.isChecked() == false && noAnonymity.isChecked() == false){
                    ToastUtils.getToast("请设置是否匿名");
                    return;
                }
                PTApplication.getRequestService().createChatRoom(isAnonyity,chatRoomName.getText().toString().trim(),"",PTApplication.userToken,PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<CreateRoomBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(CreateRoomBean createRoomBean) {
                                if (createRoomBean.isSuccess()){
                                    //TODO 创建聊天室成功，进入聊天室
                                    popupWindow4CreateChatRoom.dismiss();
                                    ToastUtils.getToast("创建聊天室成功");
                                }else{
                                    ToastUtils.getToast(createRoomBean.getMsg());
                                }
                            }
                        });
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow4CreateChatRoom.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow4CreateChatRoom.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chose;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {

    }
}
