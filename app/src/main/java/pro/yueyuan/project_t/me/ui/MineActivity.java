package pro.yueyuan.project_t.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.bidding.ui.BiddingActivity;
import pro.yueyuan.project_t.chat.ui.ChatActivity;
import pro.yueyuan.project_t.home.DaggerIHomeComponent;
import pro.yueyuan.project_t.home.HomePresenterModule;
import pro.yueyuan.project_t.home.IHomeContract;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.home.ui.HomeFragment;
import pro.yueyuan.project_t.login.ui.LoginActivity;
import pro.yueyuan.project_t.me.DaggerIMineComponent;
import pro.yueyuan.project_t.me.IMineContract;
import pro.yueyuan.project_t.me.MinePresenterModule;
import pro.yueyuan.project_t.ranking.ui.RankingActivity;
import pro.yueyuan.project_t.utils.ActivityUtils;

public class MineActivity extends NetActivity implements View.OnClickListener {
    @BindView(R.id.home_view)
    FrameLayout mHomeActivity;
    @BindView(R.id.chat_view)
    FrameLayout mChatActivity;
    @BindView(R.id.bidding_view)
    FrameLayout mBiddingView;
    @BindView(R.id.ranking_view)
    FrameLayout mRankingView;
    @BindView(R.id.mine_view)
    FrameLayout mMineView;

    /**
     * fragment的集合
     */
    private ArrayList<Fragment> mFragmentList;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        mHomeActivity.setOnClickListener(this);
        mChatActivity.setOnClickListener(this);
        mRankingView.setOnClickListener(this);
        mBiddingView.setOnClickListener(this);
        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            MineFragment mineFragment = MineFragment.newInstance();
            mFragmentList.add(mineFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_home_activity);
        }

//        // dagger2
           DaggerIMineComponent.builder()
                   .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
               // .homePresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                   .minePresenterModule(new MinePresenterModule(((IMineContract.View) (mFragmentList.get(0)))))
                   .build().inject(this);
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_view:
                startActivity(new Intent(MineActivity.this, HomeActivity.class));
                finish();
                break;
            case R.id.chat_view:
                startActivity(new Intent(MineActivity.this, ChatActivity.class));
                finish();
                break;
            case R.id.ranking_view:
                startActivity(new Intent(MineActivity.this, RankingActivity.class));
                finish();
                break;
            case R.id.bidding_view:
                startActivity(new Intent(MineActivity.this, BiddingActivity.class));
                finish();
                break;
        }
    }
}
