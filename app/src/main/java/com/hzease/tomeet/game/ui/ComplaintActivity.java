package com.hzease.tomeet.game.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.OnClick;
import io.rong.eventbus.EventBus;

/**
 * Created by xuq on 2017/5/31.
 */

public class ComplaintActivity extends NetActivity {

    @OnClick({
            //选择投诉用户
            R.id.rl_home_room_select_fmt,
            //提交投诉
            R.id.bt_home_room_comitcomplaint_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_home_room_select_fmt:
                Intent getIntent = getIntent();
                String roomId = getIntent.getStringExtra("roomId");
                Intent startIntent = new Intent(this,ChooseUserActivity.class);
                startIntent.putExtra("roomId",roomId);
                Logger.e("roomId" + roomId);
                startActivity(startIntent);
                break;
        }
    }

    public void onEventMainThread(GameChatRoomBean.DataBean.JoinMembersBean membersBean) {
        Logger.e(membersBean.getNickname());
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
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("requestcode:" + requestCode + "resultCode" + resultCode);
        if (requestCode == 233){
            if (data != null){
                String userId = data.getStringExtra("userId");
                String nickName = data.getStringExtra("nickName");
                ToastUtils.getToast(this,userId + nickName);
            }
        }
    }*/
}
