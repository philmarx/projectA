package com.hzease.tomeet.home.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.BottomNavigationView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.utils.AndroidBug5497Workaround;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import io.rong.common.RLog;
import io.rong.imkit.IExtensionClickListener;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.manager.AudioPlayManager;
import io.rong.imkit.manager.AudioRecordManager;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/11/2.
 */

public class ChatRoomFragment extends BaseFragment implements IHomeContract.View,IExtensionClickListener,HomeActivity.OnBackPressedListener {

    // 底部栏
    BottomNavigationView bottomNavigationView;

    HomeActivity meActivity;

    @BindView(R.id.rc_extension_gamechatroom_fmt)
    RongExtension mRongExtension;
    private IHomeContract.Presenter mPresenter;
    private Conversation.ConversationType mConversationType;
    private String chatRoomId;

    public static ChatRoomFragment newInstance(){
        return new ChatRoomFragment();
    }

    @Override
    public void setPresenter(IHomeContract.Presenter presenter) {

        mPresenter = checkNotNull(presenter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_chatroom;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (HomeActivity) getActivity();
        bottomNavigationView =  getActivity().findViewById(R.id.navigation_bottom);
        //隐藏底部布局
        if (bottomNavigationView.getVisibility() == View.VISIBLE){
            bottomNavigationView.setVisibility(View.GONE);
        }
        mConversationType = Conversation.ConversationType.CHATROOM;
        mRongExtension.setExtensionClickListener(this);
        mRongExtension.setFragment(this);
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            @Override
            public void onEmojiClick(String emoji) {
                EditText inputEditText = mRongExtension.getInputEditText();
                inputEditText.getText().insert(inputEditText.getSelectionStart(), emoji);
            }

            @Override
            public void onDeleteClick() {
                mRongExtension.getInputEditText().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        mRongExtension.addEmoticonTab(emojiTab, "1");
    }


    //其他fragment中的方法
    @Override
    public void initRoomsList(boolean isSuccess, List<HomeRoomsBean.DataBean> date, boolean isLoadMore) {

    }

    @Override
    public void setAvatarAndNickname() {

    }

    @Override
    public void joinTheRoom(NoDataBean noDataBean, String roomId, String password) {

    }

    @Override
    public void changeLoadView(boolean isShown) {

    }


    @Override
    public void onSendToggleClick(View view, String text) {
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim())) {
            TextMessage textMessage = TextMessage.obtain(text);
            MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
            if (mentionedInfo != null) {
                textMessage.setMentionedInfo(mentionedInfo);
            }

            Message message = Message.obtain(chatRoomId, this.mConversationType, textMessage);
            RongIM.getInstance().sendMessage(message, (String) null, (String) null, (IRongCallback.ISendMessageCallback) null);
        } else {
            RLog.e("ConversationFragment", "text content must not be null");
        }
    }

    @Override
    public void onImageResult(List<Uri> list, boolean b) {

    }

    @Override
    public void onLocationResult(double v, double v1, String s, Uri uri) {

    }

    @Override
    public void onSwitchToggleClick(View view, ViewGroup viewGroup) {
        Logger.e("onSwitchToggleClick");
    }

    // 语音
    private float mLastTouchY;
    private boolean mUpDirection;
    private float mOffsetLimit;
    @Override
    public void onVoiceInputToggleTouch(View v, MotionEvent event) {
        String[] permissions = new String[]{"android.permission.RECORD_AUDIO"};
        if (!PermissionCheckUtil.checkPermissions(this.getActivity(), permissions)) {
            if (event.getAction() == 0) {
                PermissionCheckUtil.requestPermissions(this, permissions, 100);
            }
        } else {
            if (event.getAction() == 0) {
                AudioPlayManager.getInstance().stopPlay();
                AudioRecordManager.getInstance().startRecord(v.getRootView(), this.mConversationType, this.chatRoomId);
                this.mLastTouchY = event.getY();
                this.mUpDirection = false;
                ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input_hover);
            } else if (event.getAction() == 2) {
                if (this.mLastTouchY - event.getY() > this.mOffsetLimit && !this.mUpDirection) {
                    AudioRecordManager.getInstance().willCancelRecord();
                    this.mUpDirection = true;
                    ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input);
                } else if (event.getY() - this.mLastTouchY > -this.mOffsetLimit && this.mUpDirection) {
                    AudioRecordManager.getInstance().continueRecord();
                    this.mUpDirection = false;
                    ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input_hover);
                }
            } else if (event.getAction() == 1 || event.getAction() == 3) {
                AudioRecordManager.getInstance().stopRecord();
                ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input);
            }

            if (this.mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
                RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.chatRoomId, "RC:VcMsg");
            }

        }
    }

    @Override
    public void onEmoticonToggleClick(View view, ViewGroup viewGroup) {
        Logger.e("onEmoticonToggleClick");
        //TODO 发送表情
        //rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        //rl_bottom_gamechatroom.setVisibility(View.VISIBLE);
        //rl_bottom_gamechatroom.setVisibility(View.GONE);
        //AndroidBug5497Workaround.assistActivity(mRootView);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(130);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_bottom_gamechatroom.setVisibility(View.GONE);
                    }
                });
            }
        }).start();*/
    }

    @Override
    public void onPluginToggleClick(View view, ViewGroup viewGroup) {

    }

    @Override
    public void onMenuClick(int i, int i1) {

    }

    @Override
    public void onEditTextClick(EditText editText) {
       // rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        /*if (rl_bottom_gamechatroom.getVisibility() == View.VISIBLE) {
            rl_bottom_gamechatroom.setVisibility(View.GONE);
            // 解决键盘不上浮问题
            //AndroidBug5497Workaround.assistActivity(mRootView);
        }*/
        AndroidBug5497Workaround.assistActivity(mRootView);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onExtensionCollapsed() {

    }

    @Override
    public void onExtensionExpanded(int i) {

    }

    @Override
    public void onPluginClicked(IPluginModule iPluginModule, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean myOnBackPressed() {
        if (mRongExtension.isExtensionExpanded()) {
            mRongExtension.collapseExtension();
            return false;
        } else {
            return true;
        }
    }
}
