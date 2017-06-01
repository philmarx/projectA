package com.hzease.tomeet.game.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/5/31.
 */

public class ComplaintActivity extends NetActivity {

    @BindView(R.id.tv_home_rooms_complaint_name_act)
    TextView tv_home_rooms_complaint_name_act;
    @BindView(R.id.et_home_room_complaintcontent_fmt)
    EditText et_home_room_complaintcontent_fmt;
    private long otherId;
    private String roomId;

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
                roomId = getIntent.getStringExtra("roomId");
                Intent startIntent = new Intent(this,ChooseUserActivity.class);
                startIntent.putExtra("roomId",roomId);
                Logger.e("roomId" + roomId);
                startActivity(startIntent);
                break;
            case R.id.bt_home_room_comitcomplaint_fmt:
                String complaintContent = et_home_room_complaintcontent_fmt.getText().toString().trim();
                if (complaintContent.isEmpty()){
                    ToastUtils.getToast(this,"请输入投诉理由");
                    break;
                }
                PTApplication.getRequestService().complaintOther(PTApplication.userToken,PTApplication.userId,roomId, otherId,complaintContent)
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
                                if (noDataBean.isSuccess()){
                                    ToastUtils.getToast(ComplaintActivity.this,"投诉成功！");
                                    finish();
                                }else{
                                    ToastUtils.getToast(ComplaintActivity.this,"投诉失败，请重新操作");
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
        ToastUtils.getToast(this, otherId + nickName);
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
}
