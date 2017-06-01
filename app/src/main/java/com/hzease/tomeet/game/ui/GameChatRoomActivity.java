package com.hzease.tomeet.game.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.game.DaggerIGameChatRoomComponent;
import com.hzease.tomeet.game.GameChatRoomPresenter;
import com.hzease.tomeet.game.GameChatRoomPresenterModule;
import com.hzease.tomeet.game.IGameChatRoomContract;
import com.hzease.tomeet.utils.ActivityUtils;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Key on 2017/5/4 14:19
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomActivity extends NetActivity {

    private OnBackPressedListener mListener;

    @Override
    public void onBackPressed() {
        if (mListener == null || mListener.myOnBackPressed()) {
            super.onBackPressed();
        }
    }

    public interface OnBackPressedListener {
        boolean myOnBackPressed();
    }

    @Inject
    GameChatRoomPresenter mGameChatRoomPresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;
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
        return R.layout.activity_gamechatroom;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            GameChatRoomFragment gameChatRoomFragment = GameChatRoomFragment.newInstance();
            mListener = gameChatRoomFragment;
            mFragmentList.add(gameChatRoomFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_gamechatroom_activity);
        }

            DaggerIGameChatRoomComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    // .chatPresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                    .gameChatRoomPresenterModule(new GameChatRoomPresenterModule(((IGameChatRoomContract.View) (mFragmentList.get(0)))))
                    .build().inject(this);
    }


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
