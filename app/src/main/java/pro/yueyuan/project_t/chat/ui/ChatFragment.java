package pro.yueyuan.project_t.chat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.chat.IChatContract;

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
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @OnClick({R.id.rb_gold_chat_fmt,
            R.id.rb_blue_chat_fmt,
            R.id.rb_green_chat_fmt,
            R.id.rb_gray_chat_fmt,
            R.id.rb_red_chat_fmt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_gold_chat_fmt:
                break;
            case R.id.rb_blue_chat_fmt:
                break;
            case R.id.rb_green_chat_fmt:
                break;
            case R.id.rb_gray_chat_fmt:
                break;
            case R.id.rb_red_chat_fmt:
                break;
        }
    }
}
