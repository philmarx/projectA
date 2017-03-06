package pro.yueyuan.project_t.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.bidding.ui.BiddingActivity;
import pro.yueyuan.project_t.chat.ui.ChatActivity;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.login.ui.LoginActivity;
import pro.yueyuan.project_t.ranking.ui.RankingActivity;

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
    //登录按钮,若用户登录则隐藏
    @BindView(R.id.mine_login)
    Button mineLogin;
    //用户名字
    @BindView(R.id.mine_userName)
    TextView mineUserName;
    //花草数量
    @BindView(R.id.mine_flowerNum)
    TextView mineFlowerNum;
    //设置按钮
    @BindView(R.id.mine_setting)
    ImageView mineSetting;
    //分享按钮
    @BindView(R.id.mine_share)
    ImageView mineShare;
    //组队信息
    @BindView(R.id.mine_teamInfo)
    ListView mineTeamInfo;
    //当没有组队信息时显示该信息
    @BindView(R.id.mine_noitem)
    TextView mineNoitem;
    //我的余额
    @BindView(R.id.mine_balance)
    TextView mineBalance;
    //我的保证金
    @BindView(R.id.mine_BZmoney)
    TextView mineBZmoney;
    //我的表白信
    @BindView(R.id.mine_letterNum)
    TextView mineLetterNum;
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
        mineLogin.setOnClickListener(this);
        mineTeamInfo.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });
        mineTeamInfo.setEmptyView(mineNoitem);
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
            case R.id.mine_login:
                startActivity(new Intent(MineActivity.this, LoginActivity.class));
                break;
        }
    }
}
