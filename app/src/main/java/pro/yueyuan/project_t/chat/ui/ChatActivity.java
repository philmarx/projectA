package pro.yueyuan.project_t.chat.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import butterknife.BindView;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.utils.ActivityUtils;

public class ChatActivity extends NavigationActivity implements View.OnClickListener {

    private Fragment mConversationList;

    private List<Fragment> mFragment;
    @BindView(R.id.one)
    LinearLayout mPrefect;
    @BindView(R.id.two)
    LinearLayout mGreat;
    @BindView(R.id.three)
    LinearLayout mGood;
    @BindView(R.id.four)
    LinearLayout mBad;
    @BindView(R.id.five)
    LinearLayout mMiss;

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
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
        return R.layout.activity_chat;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        mConversationList = initConversationList();//获取融云会话的列表对象
        navigation_bottom.getMenu().findItem(R.id.navigation_chat).setChecked(true).setEnabled(false);
        //初始化fragment
        if (mFragment == null || mFragment.size() != 1) {
            mFragment = new ArrayList<>();
            mFragment.add(AFragment.getInstance());
            mFragment.add(mConversationList);
            mFragment.add(BFragment.getInstance());
            mFragment.add(CFragment.getInstance());
            mFragment.add(DFragment.getInstance());
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment.get(1), R.id.fl_content_chatlist);
        }

        // 模拟
        for (int i = 0; i < 10; i++) {
            // 不置顶,让会话出现
            RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, Long.valueOf("20000800015") + i + "", false, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    Logger.d(aBoolean);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.d(errorCode);
                }
            });
        }
    }

    private Fragment initConversationList() {
        ConversationListFragment listFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")//设置私聊会话界面是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置私聊会话界面是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                .build();
        listFragment.setUri(uri);
        return listFragment;
    }


    @Override
    public void onClick(View v) {

    }
}
