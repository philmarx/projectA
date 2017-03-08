package pro.yueyuan.project_t;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import butterknife.BindView;
import pro.yueyuan.project_t.bidding.ui.BiddingActivity;
import pro.yueyuan.project_t.chat.ui.ChatActivity;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.me.ui.MeActivity;
import pro.yueyuan.project_t.ranking.ui.RankingActivity;

/**
 * Created by Key on 2017/3/8 13:36
 * email: MrKey.K@gmail.com
 * description:
 */

public abstract class NavigationActivity extends NetActivity {

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
                        if (getContentViewId() != R.layout.activity_chat) {
                            startActivity(new Intent(NavigationActivity.this, ChatActivity.class));
                            finish();
                            return true;
                        }
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
                            startActivity(new Intent(NavigationActivity.this, MeActivity.class));
                            finish();
                            return true;
                        }
                }
                return false;
            }
        });
    }
}
