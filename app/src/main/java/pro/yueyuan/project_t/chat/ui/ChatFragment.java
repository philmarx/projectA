package pro.yueyuan.project_t.chat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.chat.IChatContract;
import pro.yueyuan.project_t.data.ConversationListBean;

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
    private List<ConversationListBean.FriendBean> friends;


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
        friends = PTApplication.mConversationListBean.getGrayList();

        rv_conversation_list_chat_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_conversation_list_chat_fmt.setAdapter(new CommonAdapter<ConversationListBean.FriendBean>(mContext, R.layout.item_conversation_chat_fmt, friends) {
            @Override
            protected void convert(ViewHolder holder, ConversationListBean.FriendBean friendBean, int position) {
                holder.setText(R.id.tv_name_item_conversation_chat_fmt, friendBean.getNickname());
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + friendBean.getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(((ImageView) holder.getView(R.id.iv_avatar_item_conversation_chat_fmt)));

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
                Logger.i("切换到绿色");
                friends = PTApplication.mConversationListBean.getGreenList();
                rv_conversation_list_chat_fmt.getAdapter().notifyDataSetChanged();
                break;
            case R.id.rb_gray_chat_fmt:
                break;
            case R.id.rb_red_chat_fmt:
                break;
        }
    }
}
