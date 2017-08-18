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

public class SelectGameTypeActivity extends NetActivity {
    public static final String SELECT_GAME_TYPE = "SELECT_GAME_TYPE";

    @BindView(R.id.lv_selectgames_one)
    ListView lv_selectgames_one;

    @BindView(R.id.rv_selectgames_two)
    RecyclerView rv_selectgames_twos;
    @BindView(R.id.iv_recommend)
    ImageView iv_recommend;
    List<GameTypeBean.ChildrenBean> list;
    TypeOneAdapter typeOneAdapter = null;
    TypeTwoAdapter typeTwoAdapter = null;

    @OnClick({
            R.id.game_icon,
            R.id.all_bg_selector_game_type
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_icon:
                GameTypeBean.ChildrenBean childrenBean = new GameTypeBean.ChildrenBean();
                childrenBean.setId(0);
                childrenBean.setName("全部分类");
                back(childrenBean);
                break;
            case R.id.all_bg_selector_game_type:
                // 点击空白处
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
                                SpUtils.saveString(SelectGameTypeActivity.this, AppConstants.TOMEET_SP_GAME_TYPE, new Gson().toJson(gameTypeBean));
                            } else {
                                ToastUtils.getToast("网络连接失败，请重试！");
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
                if (position == 0) {
                    iv_recommend.setVisibility(View.VISIBLE);
                } else {
                    iv_recommend.setVisibility(View.GONE);
                }
                if (position == 4) {
                    back(list.get(position));
                } else {
                    final int bigType = position;
                    rv_selectgames_twos.setY(view.getY() + lv_selectgames_one.getY());
                    typeTwoAdapter = new TypeTwoAdapter(list, position, PTApplication.getInstance());
                    typeTwoAdapter.setOnItemClickLitener(new TypeTwoAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            back(list.get(bigType).getChildren().get(position));
                        }
                    });
                    rv_selectgames_twos.setLayoutManager(new StaggeredGridLayoutManager(
                            3, StaggeredGridLayoutManager.VERTICAL));
                    rv_selectgames_twos.setAdapter(typeTwoAdapter);
                }
            }
        });

    }

    private void back(GameTypeBean.ChildrenBean game) {
        Logger.e(game.toString());
        setResult(RESULT_OK, new Intent().putExtra(SELECT_GAME_TYPE, game));
        finish();
    }
}
