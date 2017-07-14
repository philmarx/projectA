package com.hzease.tomeet.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameTypeBean;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.utils.ToastUtils;
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

public class CreateRoomBeforeActivity extends NetActivity {
    public static final String KEY_PICKED_CITY = "素质十连";
    public static final String KEY_GAME_ID = "mmp";
    @BindView(R.id.lv_selectgames_one)
    ListView lv_selectgames_one;
    @BindView(R.id.rv_selectgames_two)
    RecyclerView rv_selectgames_twos;
    @BindView(R.id.iv_recommend)
    ImageView iv_recommend;
    List<GameTypeBean.ChildrenBean> list;
    TypeOneAdapter typeOneAdapter = null;
    TypeTwoAdapter typeTwoAdapter = null;
    @BindView(R.id.game_icon)
    ImageView game_icon;
    @BindView(R.id.game_name)
    TextView game_name;

    private String gameName;
    private int gameId;
    private long circleId = 0;
    private boolean isOpen = true;


    @Override
    protected void netInit(Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_selectorgametype;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        game_icon.setVisibility(View.GONE);
        game_name.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();

        circleId = bundle.getLong("circleId", 0);
        isOpen = bundle.getBoolean("isOpen", true);

        String gameType = SpUtils.getStringValue(this, AppConstants.TOMEET_SP_GAME_TYPE);
        if (TextUtils.isEmpty(gameType)) {
            PTApplication.getRequestService().getActivityType("tomeet")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GameTypeBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError: " + e.getMessage());
                        }

                        @Override
                        public void onNext(GameTypeBean gameTypeBean) {
                            if (gameTypeBean.isSuccess()) {
                                list = gameTypeBean.getData();
                                typeOneAdapter = new TypeOneAdapter(gameTypeBean.getData());
                                lv_selectgames_one.setAdapter(typeOneAdapter);
                                Logger.e(new Gson().toJson(gameTypeBean));
                                SpUtils.saveString(CreateRoomBeforeActivity.this, AppConstants.TOMEET_SP_GAME_TYPE, new Gson().toJson(gameTypeBean));
                            } else {
                                ToastUtils.getToast(CreateRoomBeforeActivity.this, "网络连接失败，请重试！");
                            }
                        }
                    });
        } else {
            list = new Gson().fromJson(gameType, GameTypeBean.class).getData();
            typeOneAdapter = new TypeOneAdapter(list);
            lv_selectgames_one.setAdapter(typeOneAdapter);
        }

        lv_selectgames_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    iv_recommend.setY(-120);
                    iv_recommend.setVisibility(View.VISIBLE);
                }else{
                    iv_recommend.setVisibility(View.GONE);
                }
                if (position == 4) {
                    Logger.e("其他活动");
                    gameName = list.get(position).getName();
                    gameId = list.get(position).getId();
                    Logger.e(gameName + gameId);
                    gone(gameName, gameId);
                } else {
                    float y = view.getY();
                    float y1 = lv_selectgames_one.getY();
                    rv_selectgames_twos.setY(y + y1);
                    typeTwoAdapter = new TypeTwoAdapter(list, position, PTApplication.getInstance());
                    final int type = position;
                    typeTwoAdapter.setOnItemClickLitener(new TypeTwoAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            gameName = list.get(type).getChildren().get(position).getName();
                            gameId = list.get(type).getChildren().get(position).getId();
                            Logger.e(gameName + gameId);
                            gone(gameName, gameId);
                        }
                    });
                    rv_selectgames_twos.setLayoutManager(new StaggeredGridLayoutManager(
                            3, StaggeredGridLayoutManager.VERTICAL));
                    rv_selectgames_twos.setAdapter(typeTwoAdapter);
                }
            }
        });

    }

    private void gone(String gameName, int gameId) {
        Intent data = new Intent(CreateRoomBeforeActivity.this, CreateRoomActivity.class);
        data.putExtra(KEY_PICKED_CITY, gameName);
        data.putExtra(KEY_GAME_ID, gameId);
        data.putExtra("circleId", circleId);
        data.putExtra("isOpen", isOpen);
        startActivity(data);
        finish();
    }

    @OnClick(R.id.all_bg_selector_game_type)
    public void onViewClicked() {
        finish();
    }
}
