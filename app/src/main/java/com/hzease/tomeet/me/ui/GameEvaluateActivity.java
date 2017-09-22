package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.WaitEvaluateAdapterV3;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/9/19.
 */

public class GameEvaluateActivity extends NetActivity {
    @BindView(R.id.rv_gameevaluate_show_fmt)
    RecyclerView rv_gameevaluate_show_fmt;
    @BindView(R.id.bt_evaluate_submit_fmt)
    Button bt_evaluate_submit_fmt;

    private WaitEvaluateAdapterV3 adapter;
    private long roomId;
    private WaitEvaluateV2Bean.DataBean newDatas;
    @OnClick({
            R.id.bt_evaluate_submit_fmt,
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_evaluate_submit_fmt:
                newDatas.setToken(PTApplication.userToken);
                newDatas.setUserId(PTApplication.userId);
                newDatas.setRoomId(String.valueOf(roomId));
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String s = gson.toJson(newDatas);
                Logger.e(s);
                PTApplication.getRequestService().evaluateFriendsV2(s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e("noDatabean" + noDataBean.toString());
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast("提交成功");
                                    finish();
                                } else {
                                    ToastUtils.getToast(noDataBean.getMsg());
                                }
                            }
                        });
                break;
        }
    }
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_evaluate;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomId = intent.getLongExtra(AppConstants.TOMEET_ROOM_ID,0);
        Logger.e("roomId      " + roomId);
        rv_gameevaluate_show_fmt.setLayoutManager(new LinearLayoutManager(this));
        rv_gameevaluate_show_fmt.addItemDecoration(new SpacesItemDecoration(20));
        Logger.e("userId" + PTApplication.userId+"\ntoken:"+PTApplication.userToken + "\nroomId" + roomId);
        PTApplication.getRequestService().toBeEvaluationFriendsV2(roomId,PTApplication.userToken,PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WaitEvaluateV2Bean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WaitEvaluateV2Bean waitEvaluateV2Bean) {
                        if (waitEvaluateV2Bean.isSuccess()){
                            showWaitEvaluateMember(waitEvaluateV2Bean);
                        }
                    }
                });
    }


    private void showWaitEvaluateMember(WaitEvaluateV2Bean data) {
        for (int i = 0; i < data.getData().getEvaluations().size(); i++) {
            if (data.getData().getEvaluations().get(i).getFriendPoint().equals("0")){
                data.getData().getEvaluations().get(i).setIsfriend(false);
            }else{
                data.getData().getEvaluations().get(i).setIsfriend(true);
            }
        }
        Logger.e(data.toString());
        newDatas = data.getData();
        adapter = new WaitEvaluateAdapterV3(this,newDatas.getEvaluations());
        rv_gameevaluate_show_fmt.setAdapter(adapter);
    }
}
