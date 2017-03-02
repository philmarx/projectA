package pro.yueyuan.project_t.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.bidding.ui.BiddingActivity;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.me.ui.MineActivity;
import pro.yueyuan.project_t.ranking.ui.RankingActivity;

public class ChatActivity extends NetActivity implements View.OnClickListener{
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        mHomeActivity.setOnClickListener(this);
        mBiddingView.setOnClickListener(this);
        mRankingView.setOnClickListener(this);
        mMineView.setOnClickListener(this);
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_view:
                startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                finish();
                break;
            case R.id.bidding_view:
                startActivity(new Intent(ChatActivity.this, BiddingActivity.class));
                finish();
                break;
            case R.id.ranking_view:
                startActivity(new Intent(ChatActivity.this, RankingActivity.class));
                finish();
                break;
            case R.id.mine_view:
                startActivity(new Intent(ChatActivity.this, MineActivity.class));
                finish();
                break;
        }
    }
}
