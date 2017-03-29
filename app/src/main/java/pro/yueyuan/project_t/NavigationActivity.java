package pro.yueyuan.project_t;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import pro.yueyuan.project_t.bidding.ui.BiddingActivity;
import pro.yueyuan.project_t.chat.ui.ChatVersion2Activity;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.login.ui.LoginActivity;
import pro.yueyuan.project_t.me.ui.MeActivity;
import pro.yueyuan.project_t.ranking.ui.RankingActivity;

/**
 * Created by Key on 2017/3/8 13:36
 * email: MrKey.K@gmail.com
 * description:
 */

public abstract class NavigationActivity extends NetActivity {

    private boolean backFlag = false;

    @BindView(R.id.navigation_bottom)
    protected BottomNavigationView navigation_bottom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
                        if (!TextUtils.isEmpty(PTApplication.userId) && !TextUtils.isEmpty(PTApplication.userToken)) {
                            startActivity(new Intent(NavigationActivity.this, ChatVersion2Activity.class));
                            finish();
                        } else {
                            startActivityForResult(new Intent(NavigationActivity.this, LoginActivity.class), AppConstants.YY_PT_NAVIGATION_CHAT_REQUEST_CODE);
                            return false;
                        }
                        return true;
                    case R.id.navigation_bidding:
                        if (getContentViewId() != R.layout.activity_bidding) {
                            startActivity(new Intent(NavigationActivity.this, BiddingActivity.class));
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
                            if (!TextUtils.isEmpty(PTApplication.userId) && !TextUtils.isEmpty(PTApplication.userToken)) {
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

    /**
     * 重写返回键,两次返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (backFlag) {
            //退出
            super.onBackPressed();
        } else {
            //单击一次提示信息
            Toast.makeText(this, "双击退出", Toast.LENGTH_SHORT).show();
            backFlag = true;
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //3秒之后，修改flag的状态
                    backFlag = false;
                }
            }.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(requestCode + "  ::  " + resultCode + "  :  " + data);
        if (resultCode == AppConstants.YY_PT_LOGIN_SUCCEED) {
            switch (requestCode) {
                case AppConstants.YY_PT_NAVIGATION_CHAT_REQUEST_CODE:
                    startActivity(new Intent(NavigationActivity.this, ChatVersion2Activity.class));
                    finish();
                    break;
                case AppConstants.YY_PT_NAVIGATION_ME_REQUEST_CODE:
                    startActivity(new Intent(NavigationActivity.this, MeActivity.class));
                    finish();
                    break;
            }
        }
    }


}
