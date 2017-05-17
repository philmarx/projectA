package com.hzease.tomeet.circle.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentConfig;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.utils.CommonUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.DivItemDecoration;
import com.hzease.tomeet.widget.adapters.CircleAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/18.
 */

public class CircleFragment extends BaseFragment implements ICircleContract.View {

    CircleActivity mCircleActivity;
    @BindView(R.id.create_speech)
    ImageView createSpeech;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    LinearLayout editTextBodyLl;
    private ICircleContract.Presenter mPresenter;
    private CommentConfig config;
    private LinearLayoutManager layoutManager;
    private List<CommentItemBean.DataBean> mDatas;
    private List<CommentItemBean.DataBean> footDatas;
    private CircleAdapter adapter;
    private int page = 0;

    @OnClick({
            R.id.create_speech
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_speech:
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
    protected void initView(Bundle savedInstanceState) {
        mCircleActivity = (CircleActivity) getActivity();
        editTextBodyLl = (LinearLayout) mCircleActivity.findViewById(R.id.editTextBodyLl);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        mPresenter.getDeclaration("杭州市", 0, 10, false);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.getDeclaration("杭州市", 0, 10, false);
                    }
                }, 2000);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(adapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            footDatas = new ArrayList<CommentItemBean.DataBean>();
                            mPresenter.getDeclaration("杭州市", ++page, 10, true);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
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
                String contentMsg = content.getText().toString().trim();
                mPresenter.createDeclare("杭州市", contentMsg, PTApplication.userToken, PTApplication.userId);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    /**
     * 创建圈子成功
     */
    @Override
    public void createSuccess() {

    }

    /**
     * 展示喊话内容
     *
     * @param commentItemBean
     */
    @Override
    public void showDeclaration(CommentItemBean commentItemBean, boolean isLoadMore) {
        if (isLoadMore) {
            footDatas.addAll(commentItemBean.getData());
            adapter.AddFooterItem(footDatas);
            //设置回到上拉加载更多
            adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
        } else {
            mDatas = commentItemBean.getData();
            adapter = new CircleAdapter(mDatas, getContext());
            recyclerView.setAdapter(adapter);
            swiperefreshlayout.setRefreshing(false);
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visible, CommentConfig config) {
        this.config = config;
        editTextBodyLl.setVisibility(visible);
        //measureCircleItemHighAndCommentItemOffset(config);
        if (View.VISIBLE == visible) {
            editTextBodyLl.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editTextBodyLl.getContext(), editTextBodyLl);

        } else if (View.GONE == visible) {
            //隐藏键盘
            CommonUtils.hideSoftInput(editTextBodyLl.getContext(), editTextBodyLl);
        }
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
        } else {
            ToastUtils.getToast(PTApplication.getInstance(), msg);
        }
    }

    @Override
    public void showRecommandCircle(List<CircleInfoBean.DataBean> data) {

    }

    /**
     * 显示附近圈子
     *
     * @param data
     */
    @Override
    public void showNeayByCircle(List<CircleInfoBean.DataBean> data) {

    }

    @Override
    public void showCircleInfo(EnterCircleInfoBean.DataBean data) {

    }

    @Override
    public void joinCircleSuccess(String msg) {

    }

    /**
     * 退出圈子成功
     *
     * @param msg
     */
    @Override
    public void signOutCircleSuccess(String msg) {

    }

    /**
     * 修改圈子公告成功
     *
     * @param msg
     */
    @Override
    public void modifitySuccess(String msg) {

    }

    /**
     * 显示圈内房间
     *
     * @param data
     */
    @Override
    public void showRoomsByCircle(List<HomeRoomsBean.DataBean> data) {

    }

    @Override
    public void showMyCircle(List<CircleInfoBean.DataBean> data) {

    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    /*private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - circle_head.getHeight();
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        return listviewOffset;
    }*/

    /*private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }*/
    /**
     * 获取状态栏高度
     * @return
     */
   /* private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }*/
}
