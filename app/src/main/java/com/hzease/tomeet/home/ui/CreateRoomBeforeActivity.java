package com.hzease.tomeet.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.ActivityTypeBean;
import com.hzease.tomeet.widget.adapters.RecycleViewTestAdapter;
import com.hzease.tomeet.widget.adapters.TypeOneAdapter;
import com.hzease.tomeet.widget.adapters.TypeTwoAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/5/2.
 */

public class CreateRoomBeforeActivity extends NetActivity {
    public static final String KEY_PICKED_CITY = "素质十连";
    public static final String KEY_GAME_ID = "mmp";
    @BindView(R.id.lv_selectgames_one)
    ListView lv_selectgames_one;
    @BindView(R.id.rv_selectgames_two)
    RecyclerView rv_selectgames_twos;
    List<ActivityTypeBean.DataBean> list;
    TypeOneAdapter typeOneAdapter = null;
    TypeTwoAdapter typeTwoAdapter = null;
    private String gameName;
    private int gameId;


    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_selectorgametype;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) throws ExecutionException, InterruptedException {
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL);
        rv_selectgames_twos.setLayoutManager(mLayoutManager);
        PTApplication.getRequestService().getActivityType("secret","app.yueyuan.pro")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ActivityTypeBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                    }

                    @Override
                    public void onNext(ActivityTypeBean activityTypeBean) {
                        Logger.e("onNext");
                        list = activityTypeBean.getData();
                        typeOneAdapter = new TypeOneAdapter(activityTypeBean.getData());
                        lv_selectgames_one.setAdapter(typeOneAdapter);
                    }
                });

        lv_selectgames_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeTwoAdapter = new TypeTwoAdapter(list,position,PTApplication.getInstance());
                final int type = position;
                typeTwoAdapter.setOnItemClickLitener(new RecycleViewTestAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        gameName = list.get(type).getChildren().get(position).getName();
                        gameId = list.get(type).getChildren().get(position).getId();
                        Logger.e(gameName+gameId);
                        gone(gameName,gameId);
                    }
                });
                rv_selectgames_twos.setAdapter(typeTwoAdapter);
                Logger.e(position+"");
            }
        });

    }
    private void gone(String gameName,int gameId){
        Intent data = new Intent(CreateRoomBeforeActivity.this,CreateRoomActivity.class);
        data.putExtra(KEY_PICKED_CITY, gameName);
        data.putExtra(KEY_GAME_ID,gameId);
        startActivity(data);
        finish();
    }
}
