package pro.yueyuan.project_t.chat.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.R;

public class ChatActivity extends NavigationActivity{

    private Fragment mConversationList;

    private List<Fragment> mFragment = new ArrayList<>();//存贮界面

    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;//将页面持久化在内存中

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
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragment.add(mConversationList);//有几种页面就往这个集合里面添加几个fragment
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);

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

    private Fragment initConversationList(){
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


}
