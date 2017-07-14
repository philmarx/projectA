package com.hzease.tomeet.game.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.ChooseUserAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseUserActivity extends NetActivity {

    @BindView(R.id.bt_home_room_select_fmt)
    Button bt_home_room_select_fmt;
    @BindView(R.id.lv_home_room_choose_act)
    ListView lv_home_room_choose_act;
    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDatas;
    private GameChatRoomBean.DataBean.JoinMembersBean joinMembersBean = new GameChatRoomBean.DataBean.JoinMembersBean();
    private String postion;
    private boolean isSuccess = false;
    private ChooseUserAdapter adapter;

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }


    /**
     * 将选择投诉的用户信息返回给上一个界面
     *
     * @param joinMembersBean
     */
    private void back(GameChatRoomBean.DataBean.JoinMembersBean joinMembersBean) {
        Intent in = new Intent();
        in.putExtra("nickName", joinMembersBean.getNickname());
        in.putExtra("userId", joinMembersBean.getId());
        Logger.e("nickName" + joinMembersBean.getNickname() + "userId" + joinMembersBean.getId());
        setResult(RESULT_OK, in);
        Logger.e("nickName" + joinMembersBean.getNickname() + "userId" + joinMembersBean.getId());
        finish();
    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_choose_user;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent getIntent = getIntent();
        String roomId = getIntent.getStringExtra("roomId");
        PTApplication.getRequestService().getGameChatRoomInfo(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameChatRoomBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(GameChatRoomBean gameChatRoomBean) {
                        Logger.e(gameChatRoomBean.isSuccess() + "");
                        if (gameChatRoomBean.isSuccess()) {
                            initMemberList(gameChatRoomBean.getData().getJoinMembers());
                        }
                    }
                });
        bt_home_room_select_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                back(joinMembersBean);
                EventBus.getDefault().post(joinMembersBean);
                finish();
            }
        });
    }

    private void initMemberList(List<GameChatRoomBean.DataBean.JoinMembersBean> joinMembers) {
        mDatas = joinMembers;
        adapter = new ChooseUserAdapter(joinMembers, this);
        lv_home_room_choose_act.setAdapter(adapter);
        lv_home_room_choose_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.e("onItemClick" + position);
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).setChoose(false);
                }
                mDatas.get(position).setChoose(true);
                joinMembersBean = mDatas.get(position);
                adapter.setData(mDatas);
                ToastUtils.getToast(ChooseUserActivity.this, "onClick" + postion + "nickName" + joinMembersBean.getNickname());
            }
        });
        Logger.e("one1");
    }

}
