package com.hzease.tomeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.ranking.ui.RankingActivity;
import com.hzease.tomeet.widget.BottomNavigationViewEx;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by Key on 2017/3/8 13:36
 * email: MrKey.K@gmail.com
 * description:
 */

public abstract class NavigationActivity extends TakePhotoActivity {

    @BindView(R.id.navigation_bottom)
    public BottomNavigationViewEx navigation_bottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 打开过导航页
        PTApplication.liveNavigationActivity += 1;
        PTApplication.isBackground = true;

        // 需要一个全局未读数
        PTApplication.badge
                .setBadgeNumber(PTApplication.unReadNumber)
                .setGravityOffset(12, 2, true)
                .bindTarget(navigation_bottom.getBottomNavigationItemView(1));

        //navigation_bottom.clearAnimation();
        //disableShiftMode(navigation_bottom);

        // 开始或关闭导航条位移模式
        navigation_bottom.enableShiftingMode(false);
        // 禁止所有动画效果
        navigation_bottom.enableAnimation(false);
        navigation_bottom.enableShiftingMode(false);
        navigation_bottom.enableItemShiftingMode(false);

        navigation_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // 已经设置为当前页无效点击.可以不写判断
                        if (getContentViewId() != R.layout.activity_home) {
                            startActivity(new Intent(NavigationActivity.this, HomeActivity.class));
                            finish();
                            return true;
                        }
                    case R.id.navigation_chat:
                        // 判断是否登录
                        if (PTApplication.myInfomation != null) {
                            RongIM.getInstance().startSubConversationList(NavigationActivity.this, Conversation.ConversationType.GROUP);
                            finish();
                        } else {
                            startActivityForResult(new Intent(NavigationActivity.this, LoginActivity.class), AppConstants.YY_PT_NAVIGATION_CHAT_REQUEST_CODE);
                            return false;
                        }
                        return true;
                    case R.id.navigation_circle:
                        if (getContentViewId() != R.layout.activity_circle) {
                            Intent intent = new Intent(NavigationActivity.this, CircleActivity.class);
                            intent.putExtra("flag", 1);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                    case R.id.navigation_ranking:
                        if (getContentViewId() != R.layout.activity_ranking) {
                            startActivity(new Intent(NavigationActivity.this, RankingActivity.class));
                            finish();
                            return true;
                        }
                    case R.id.navigation_me:
                        if (getContentViewId() != R.layout.activity_me) {
                            if (PTApplication.myInfomation != null) {
                                startActivity(new Intent(NavigationActivity.this, MeActivity.class));
                                finish();
                            } else {
                                startActivityForResult(new Intent(NavigationActivity.this, LoginActivity.class), AppConstants.YY_PT_NAVIGATION_ME_REQUEST_CODE);
                                return false;
                            }
                            return true;
                        }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (navigation_bottom.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            // 进后台
            moveTaskToBack(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(requestCode + "  ::  " + resultCode + "  :  " + data);
        if (resultCode == AppConstants.YY_PT_LOGIN_SUCCEED) {
            switch (requestCode) {
                case AppConstants.YY_PT_NAVIGATION_CHAT_REQUEST_CODE:
                    RongIM.getInstance().startSubConversationList(NavigationActivity.this, Conversation.ConversationType.GROUP);
                    finish();
                    break;
                case AppConstants.YY_PT_NAVIGATION_ME_REQUEST_CODE:
                    startActivity(new Intent(NavigationActivity.this, MeActivity.class));
                    finish();
                    break;
            }
        }
    }

    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Logger.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Logger.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PTApplication.liveNavigationActivity -= 1;
    }
}
