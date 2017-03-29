package pro.yueyuan.project_t.chat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.InternalModuleManager;
import io.rong.imkit.model.Event;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.chat.IChatContract;
import pro.yueyuan.project_t.data.RealmFriendBean;
import pro.yueyuan.project_t.utils.MyTextUtils;
import pro.yueyuan.project_t.widget.adapters.ConversationAdapter;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/3/24 17:10
 * email: MrKey.K@gmail.com
 * description:
 */

public class ChatFragment extends BaseFragment implements IChatContract.View {

    @BindView(R.id.rv_conversation_list_chat_fmt)
    RecyclerView rv_conversation_list_chat_fmt;


    private IChatContract.Presenter mPresenter;
    private ConversationAdapter conversationAdapter;


    public ChatFragment() {
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void setPresenter(@NonNull IChatContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_chat;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        InternalModuleManager.getInstance().onLoaded();

        //
        conversationAdapter = new ConversationAdapter(mContext);
        rv_conversation_list_chat_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_conversation_list_chat_fmt.setAdapter(conversationAdapter);
    }

    public void onEventMainThread(final Event.OnReceiveMessageEvent event) {
        Logger.e("onEventMainThread : " + new String(event.getMessage().getContent().encode()) + "  getTargetId: " + event.getMessage().getTargetId());
        Realm realm = Realm.getDefaultInstance();
        final RealmFriendBean first = realm.where(RealmFriendBean.class).equalTo("id", Long.valueOf(event.getMessage().getTargetId())).findFirst();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(final Realm realm) {
                    if (first != null) {
                        first.setLastMessage(MyTextUtils.getMessageToString(event.getMessage().getContent()));
                        first.setLastTime(event.getMessage().getReceivedTime());
                        RongIM.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, event.getMessage().getTargetId(), new RongIMClient.ResultCallback<Integer>() {
                            @Override
                            public void onSuccess(final Integer integer) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        first.setUnreadCount(integer);
                                    }
                                });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }
        conversationAdapter.updateFriends();
    }

    @OnClick({R.id.rb_gold_chat_fmt,
            R.id.rb_blue_chat_fmt,
            R.id.rb_green_chat_fmt,
            R.id.rb_gray_chat_fmt,
            R.id.rb_red_chat_fmt})
    public void onClick(View view) {
        // 默认金色,注释掉金色,那没有符合的case就是金色
        int[] type = AppConstants.GOLD_POINT;
        switch (view.getId()) {
            /*case R.id.rb_gold_chat_fmt:
                Logger.i("切换到金色");
                type = AppConstants.GOLD_POINT;
                break;*/
            case R.id.rb_blue_chat_fmt:
                // Logger.i("切换到蓝色");
                type = AppConstants.BLUE_POINT;
                break;
            case R.id.rb_green_chat_fmt:
                // Logger.i("切换到绿色");
                type = AppConstants.GREEN_POINT;
                break;
            case R.id.rb_gray_chat_fmt:
                // Logger.i("切换到灰色");
                type = AppConstants.GRAY_POINT;
                break;
            case R.id.rb_red_chat_fmt:
                // Logger.i("切换到红色");
                type = AppConstants.RED_POINT;
                break;
        }
        conversationAdapter.switchFriends(type);
    }
}
