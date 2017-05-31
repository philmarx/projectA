package com.hzease.tomeet.game.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.KeyRadioGroupV1;
import com.hzease.tomeet.widget.adapters.ChooseUserAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.eventbus.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseUserActivity extends NetActivity {

    /*@BindView(R.id.lv_home_room_choose_aty)
    ListView lv_home_room_choose_aty;*/
    @BindView(R.id.bt_home_room_select_fmt)
    Button bt_home_room_select_fmt;
    @BindView(R.id.rg_home_room_choose_act)
    KeyRadioGroupV1 rg_home_room_choose_act;
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
                        Logger.e(gameChatRoomBean.isSuccess()+"");
                        if (gameChatRoomBean.isSuccess()) {
                            for (final GameChatRoomBean.DataBean.JoinMembersBean membersBean : gameChatRoomBean.getData().getJoinMembers()) {
                                final View view =  View.inflate(rg_home_room_choose_act.getContext(),R.layout.item_roommembers, null);
                                Logger.e("view:  " + view);
                                CircleImageView memberIcon = (CircleImageView) view.findViewById(R.id.civ_home_rooms_icon_item);
                                //头像
                                Glide.with(ChooseUserActivity.this)
                                        .load(AppConstants.YY_PT_OSS_USER_PATH + membersBean.getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                                        .bitmapTransform(new CropCircleTransformation(ChooseUserActivity.this))
                                        .signature(new StringSignature(membersBean.getAvatarSignature()))
                                        .into(memberIcon);
                                TextView memberName = (TextView) view.findViewById(R.id.tv__home_rooms_name_item);
                                memberName.setText(membersBean.getNickname());
                                view.setTag(membersBean);
                                /*view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        Logger.e("" + isChecked);
                                        if (isChecked){
                                            rg_home_room_choose_act.setTag(membersBean);
                                        }
                                    }
                                });*/
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((RadioButton)view.findViewById(R.id.rb_home_rooms_choosemember_item)).setChecked(true);
                                    }
                                });
                                rg_home_room_choose_act.addView(view);
                            }
                        }
                    }
                });
        bt_home_room_select_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(((GameChatRoomBean.DataBean.JoinMembersBean) findViewById(rg_home_room_choose_act.getCheckedRadioButtonId()).getTag()).getNickname());
                EventBus.getDefault().post((GameChatRoomBean.DataBean.JoinMembersBean) findViewById(rg_home_room_choose_act.getCheckedRadioButtonId()).getTag());
                finish();
            }
        });
    }

    private void initMemberList(List<GameChatRoomBean.DataBean.JoinMembersBean> joinMembers) {
        mDatas = joinMembers;
        adapter = new ChooseUserAdapter(joinMembers, this);
        postion = adapter.key;
        joinMembersBean = mDatas.get(Integer.valueOf(postion));
        //lv_home_room_choose_aty.setAdapter(adapter);
        //ToastUtils.getToast(ChooseUserActivity.this, "onClick" + postion + "nickName" + joinMembersBean.getNickname());
        Logger.e("one1");
    }

}
