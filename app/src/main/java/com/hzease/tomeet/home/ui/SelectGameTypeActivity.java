package com.hzease.tomeet.home.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.ActivityTypeBean;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.widget.adapters.TypeOneAdapter;
import com.hzease.tomeet.widget.adapters.TypeTwoAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/5/2.
 */

public class SelectGameTypeActivity extends NetActivity {
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

    @OnClick({
      R.id.game_icon,
      R.id.all_bg_selector_game_type
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.game_icon:
                back("全部分类",0);
                break;
            case R.id.all_bg_selector_game_type:
                finish();
                break;
        }
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_selectorgametype;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        String gameType = SpUtils.getStringValue(this, AppConstants.TOMEET_SP_GAME_TYPE);
        if (TextUtils.isEmpty(gameType)) {
            PTApplication.getRequestService().getActivityType("tomeet")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ActivityTypeBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError: " + e.getMessage());
                        }

                        @Override
                        public void onNext(ActivityTypeBean activityTypeBean) {
                            if (activityTypeBean.isSuccess()) {
                                list = activityTypeBean.getData();
                                typeOneAdapter = new TypeOneAdapter(activityTypeBean.getData());
                                lv_selectgames_one.setAdapter(typeOneAdapter);
                                Logger.e(new Gson().toJson(activityTypeBean));
                                SpUtils.saveString(SelectGameTypeActivity.this, AppConstants.TOMEET_SP_GAME_TYPE, new Gson().toJson(activityTypeBean));
                            } else {

                            }
                        }
                    });
        } else {
            list = new Gson().fromJson(gameType, ActivityTypeBean.class).getData();
            typeOneAdapter = new TypeOneAdapter(list);
            lv_selectgames_one.setAdapter(typeOneAdapter);
        }

        lv_selectgames_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int type = position;
                if (position == 4){
                    gameName = list.get(type).getName();
                    gameId = list.get(type).getId();
                    back(gameName,gameId);
                }else{
                    rv_selectgames_twos.setY(view.getY() + lv_selectgames_one.getY());
                    typeTwoAdapter = new TypeTwoAdapter(list,position,PTApplication.getInstance());
                    typeTwoAdapter.setOnItemClickLitener(new TypeTwoAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            gameName = list.get(type).getChildren().get(position).getName();
                            gameId = list.get(type).getChildren().get(position).getId();
                            Logger.e(gameName+gameId);
                            back(gameName,gameId);
                        }
                    });
                    rv_selectgames_twos.setLayoutManager(new StaggeredGridLayoutManager(
                            3, StaggeredGridLayoutManager.VERTICAL));
                    rv_selectgames_twos.setAdapter(typeTwoAdapter);
                }

                Logger.e(position+"");
            }
        });

    }
    private void back(String gameName,int gameId){
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, gameName);
        data.putExtra(KEY_GAME_ID,gameId);
        setResult(RESULT_OK, data);
        SharedPreferences sp = getSharedPreferences("game_name", Context.MODE_PRIVATE);
        sp.edit().putString("gamename", gameName).putInt("gameId",gameId).commit();
        finish();
    }
}
