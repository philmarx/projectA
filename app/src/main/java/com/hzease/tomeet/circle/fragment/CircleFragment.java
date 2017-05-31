package com.hzease.tomeet.circle.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.CircleOfFriendsAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.IExtensionClickListener;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.plugin.IPluginModule;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/18.
 * 2017年5月23日 12:32:04 Key重写
 */

public class CircleFragment extends BaseFragment implements ICircleContract.View, IExtensionClickListener, CircleActivity.OnBackPressedListener {
    @BindView(R.id.fab_circle_of_friends_fmt)
    FloatingActionButton fab_circle_of_friends_fmt;

    @BindView(R.id.rv_circle_of_friends_fmt)
    RecyclerView rv_circle_of_friends_fmt;

    @BindView(R.id.srl_circle_of_friends_fmt)
    SwipeRefreshLayout srl_circle_of_friends_fmt;

    @BindView(R.id.rc_extension_circle_of_friends_fmt)
    RongExtension rc_extension_circle_of_friends_fmt;

    private ICircleContract.Presenter mPresenter;

    private CircleOfFriendsAdapter circleOfFriendsAdapter;

    // 喊话ID
    private long mDeclaration;
    // 回复给谁的ID，0为不指定
    private long mToUserId;
    // 刷新第几条
    private int position;

    private int page = 0;

    private final int LOAD_SIZE = 15;
    private InputMethodManager imm;

    public CircleFragment() {
        // 保留空构造
    }

    @OnClick({
            R.id.fab_circle_of_friends_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_circle_of_friends_fmt:
                initPopupWindos(v);
                break;
        }
    }

    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static CircleFragment newInstance() {
        return new CircleFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_circleoffriends;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 2017/5/23 就算用DIFF也会产生脚标对不上号。无法试试刷新
        Logger.i("onResume");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //AndroidBug5497WorkaroundActivity.assistActivity(getActivity());
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        rc_extension_circle_of_friends_fmt.setExtensionClickListener(this);
        rc_extension_circle_of_friends_fmt.setFragment(this);
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            @Override
            public void onEmojiClick(String emoji) {
                EditText inputEditText = rc_extension_circle_of_friends_fmt.getInputEditText();
                inputEditText.getText().insert(inputEditText.getSelectionStart(), emoji);
            }

            @Override
            public void onDeleteClick() {
                rc_extension_circle_of_friends_fmt.getInputEditText().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        rc_extension_circle_of_friends_fmt.addEmoticonTab(emojiTab, "1");

        circleOfFriendsAdapter = new CircleOfFriendsAdapter();
        circleOfFriendsAdapter.setOnItemClickLitener(new CircleOfFriendsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int innerPosition, CommentItemBean.DataBean dataBean, CommentItemBean.DataBean.EvaluationsBean.SenderBean senderBean) {
                position = innerPosition;

                // 输入框显示
                if (rc_extension_circle_of_friends_fmt.isExtensionExpanded()) {
                    rc_extension_circle_of_friends_fmt.collapseExtension();
                }
                if (rc_extension_circle_of_friends_fmt.getVisibility() == View.GONE) {
                    rc_extension_circle_of_friends_fmt.setVisibility(View.VISIBLE);
                }
                // fab 和 导航栏 隐藏
                if (((CircleActivity) getActivity()).navigation_bottom.getVisibility() == View.VISIBLE) {
                    ((CircleActivity) getActivity()).navigation_bottom.setVisibility(View.GONE);
                }
                if (fab_circle_of_friends_fmt.getVisibility() == View.VISIBLE) {
                    fab_circle_of_friends_fmt.setVisibility(View.GONE);
                }

                EditText inputEditText = rc_extension_circle_of_friends_fmt.getInputEditText();

                String who;
                if (senderBean == null) {
                    who = dataBean.getDeclareNickname();
                    mToUserId = 0;
                } else {
                    who = senderBean.getNickname();
                    mToUserId = senderBean.getId();
                }
                // 喊话ID
                mDeclaration = dataBean.getId();
                // "消息Id:  " + dataBean.getId() + "   消息发布者Id: " + dataBean.getDeclareId()
                inputEditText.setHint("回复  " + who);

                inputEditText.requestFocusFromTouch();


                Logger.e("active: " + imm.isActive());
                if (imm.isActive()) {
                    //imm.showSoftInput(inputEditText, InputMethodManager.SHOW_FORCED);
                    //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                    imm.toggleSoftInputFromWindow(inputEditText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        rv_circle_of_friends_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_circle_of_friends_fmt.setAdapter(circleOfFriendsAdapter);

        srl_circle_of_friends_fmt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        mPresenter.getDeclaration(PTApplication.cityName, page, LOAD_SIZE, false);
                    }
                }, 100);
            }
        });

        rv_circle_of_friends_fmt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastCompletelyVisibleItem;
            int firstCompletelyVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 隐藏
                hintKbTwo();
                if (fab_circle_of_friends_fmt.getVisibility() == View.GONE) {
                    fab_circle_of_friends_fmt.setVisibility(View.VISIBLE);
                }
                if (((CircleActivity) getActivity()).navigation_bottom.getVisibility() == View.GONE) {
                    ((CircleActivity) getActivity()).navigation_bottom.setVisibility(View.VISIBLE);
                }
                if (rc_extension_circle_of_friends_fmt.isExtensionExpanded()) {
                    rc_extension_circle_of_friends_fmt.collapseExtension();
                }
                if (rc_extension_circle_of_friends_fmt.getVisibility() == View.VISIBLE) {
                    rc_extension_circle_of_friends_fmt.setVisibility(View.GONE);
                }
                // 显示最后一条加载
                if (circleOfFriendsAdapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastCompletelyVisibleItem + 1 == circleOfFriendsAdapter.getItemCount()
                            && firstCompletelyVisibleItem != 0
                            && circleOfFriendsAdapter.getmLoadMoreStatus() != circleOfFriendsAdapter.NO_LOAD_MORE
                            ) {

                        circleOfFriendsAdapter.changeMoreStatus(circleOfFriendsAdapter.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.getDeclaration(PTApplication.cityName, ++page, LOAD_SIZE, true);
                            }
                        }, 200);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                // 第一个完全可见
                firstCompletelyVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });
        mPresenter.getDeclaration(PTApplication.cityName, 0, LOAD_SIZE, false);
    }

    private void initPopupWindos(View v) {
        Logger.e("initPopupWindows");
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_speech, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
            }
        });
        final EditText content = (EditText) contentView.findViewById(R.id.et_content_pop);
        Button declare = (Button) contentView.findViewById(R.id.bt_pop_declare);
        declare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentMsg = content.getText().toString().trim().replace("\n", "，");
                mPresenter.createDeclare(PTApplication.cityName, contentMsg, PTApplication.userToken, PTApplication.userId);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.bt_pop_declare_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }


    /**
     * 展示数据
     *
     * @param isSuccess   数据拉取
     * @param commentList 列表
     * @param isLoadMore  上下方向
     */
    @Override
    public void showDeclaration(boolean isSuccess, List<CommentItemBean.DataBean> commentList, boolean isLoadMore) {
        if (isSuccess) {
            if (isLoadMore) {
                circleOfFriendsAdapter.getmData().addAll(commentList);
                //设置回到上拉加载更多
                if (commentList.size() == LOAD_SIZE) {
                    Logger.i("date.size():  " + commentList.size());
                    circleOfFriendsAdapter.changeMoreStatus(circleOfFriendsAdapter.PULLUP_LOAD_MORE);
                } else {
                    circleOfFriendsAdapter.changeMoreStatus(circleOfFriendsAdapter.NO_LOAD_MORE);
                }
            } else {
                circleOfFriendsAdapter.setmData(commentList);
                rv_circle_of_friends_fmt.scrollToPosition(0);
            }
            circleOfFriendsAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.getToast(mContext, "数据加载失败，请重试");
        }
        if (srl_circle_of_friends_fmt != null) {
            srl_circle_of_friends_fmt.setRefreshing(false);
        }
        // 执行一次画面重置
        fab_circle_of_friends_fmt.setVisibility(View.VISIBLE);
        ((CircleActivity) getActivity()).navigation_bottom.setVisibility(View.VISIBLE);
        rc_extension_circle_of_friends_fmt.setVisibility(View.GONE);
    }


    /**
     * 完成喊话后的展示
     *
     * @param isSuccess
     */
    @Override
    public void showDeclareSucccess(boolean isSuccess, String msg) {
        if (isSuccess) {
            ToastUtils.getToast(PTApplication.getInstance(), "喊话成功");
            mPresenter.getDeclaration(PTApplication.cityName, 0, LOAD_SIZE, false);
        } else {
            ToastUtils.getToast(PTApplication.getInstance(), msg);
        }
    }

    /**
     * 回复后刷新单条消息
     *
     * @param dataBean
     */
    @Override
    public void refreshOneDeclaration(CommentItemBean.DataBean dataBean) {
        circleOfFriendsAdapter.getmData().set(position, dataBean);
        circleOfFriendsAdapter.notifyItemChanged(position);
    }


    //------------别的fragment里用的----------------------------------------------
    @Override
    public void createSuccess() {
    }

    @Override
    public void showRecommandCircle(List<CircleInfoBean.DataBean> data) {
    }

    @Override
    public void showNeayByCircle(List<CircleInfoBean.DataBean> data) {
    }

    @Override
    public void showCircleInfo(EnterCircleInfoBean.DataBean data) {
    }

    @Override
    public void joinCircleSuccess(String msg) {
    }

    @Override
    public void signOutCircleSuccess(String msg) {
    }

    @Override
    public void modifitySuccess(String msg) {
    }

    @Override
    public void showRoomsByCircle(List<HomeRoomsBean.DataBean> data) {
    }

    @Override
    public void showMyCircle(List<CircleInfoBean.DataBean> data) {
    }
    //----------------------------------------------------------------------------------------------


    //-------------融云扩展插件接口--------------------------------------------------------------------
    @Override
    public void onSendToggleClick(View view, String s) {
        // 发送逻辑
        if (TextUtils.isEmpty(s.trim())) {
            ToastUtils.getToast(mContext, "不可以回复空消息");
        } else {
            mPresenter.commentWho(s.trim().replace("\n", "，"), mDeclaration, mToUserId);
            // 隐藏
            fab_circle_of_friends_fmt.setVisibility(View.VISIBLE);
            ((CircleActivity) getActivity()).navigation_bottom.setVisibility(View.VISIBLE);
            rc_extension_circle_of_friends_fmt.setVisibility(View.GONE);
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
    }

    @Override
    public void onVoiceInputToggleTouch(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onEmoticonToggleClick(View view, ViewGroup viewGroup) {
        hintKbTwo();
    }

    @Override
    public void onPluginToggleClick(View view, ViewGroup viewGroup) {
    }

    @Override
    public void onMenuClick(int i, int i1) {

    }

    @Override
    public void onEditTextClick(EditText editText) {

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
        ((CircleActivity) getActivity()).navigation_bottom.setVisibility(View.GONE);
    }

    @Override
    public void onPluginClicked(IPluginModule iPluginModule, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean myOnBackPressed() {
        if (rc_extension_circle_of_friends_fmt.getVisibility() == View.VISIBLE) {
            if (rc_extension_circle_of_friends_fmt.isExtensionExpanded()) {
                rc_extension_circle_of_friends_fmt.collapseExtension();
            }
            rc_extension_circle_of_friends_fmt.setVisibility(View.GONE);
            fab_circle_of_friends_fmt.setVisibility(View.VISIBLE);
            ((CircleActivity) getActivity()).navigation_bottom.setVisibility(View.VISIBLE);
            return false;
        } else {
            return true;
        }
    }

    private void hintKbTwo() {
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
