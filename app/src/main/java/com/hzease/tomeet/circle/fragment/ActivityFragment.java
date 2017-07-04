package com.hzease.tomeet.circle.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.me.ui.GameFinishActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.HomeRoomsAdapter;
import com.hzease.tomeet.widget.adapters.MyJoinRoomsAdapter;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by xuq on 2017/5/10.
 */

public class ActivityFragment extends Fragment {


    long circleId;
    private RecyclerView recyclerView;
    private HomeRoomsAdapter adapter;
    private SwipeRefreshLayout srl_circle_rooms_fmt;
    private int Page = 0;

    public ActivityFragment() {
    }

    public ActivityFragment(long circleId) {
        this.circleId = circleId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gameincircle, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_circlegame_item);
        srl_circle_rooms_fmt = (SwipeRefreshLayout) view.findViewById(R.id.srl_circle_rooms_fmt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        adapter = new HomeRoomsAdapter();
        recyclerView.setAdapter(adapter);
        /*adapter.setOnItemClickLitener(new MyJoinRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, MyJoinRoomsBean.DataBean roomBean) {
                if (PTApplication.myInfomation != null) {
                    String roomId = String.valueOf(roomBean.getId());
                    if (roomBean.isLocked()) {
                        for (MyJoinRoomsBean.DataBean.JoinMembersBean joinMembersBean : roomBean.getJoinMembers()) {
                            if (joinMembersBean.getId() == PTApplication.myInfomation.getData().getId()) {
                                joinRooms(roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD);
                                return;
                            }
                        }
                        initPopupWindow(view, roomId);
                    } else {
                        joinRooms(roomId, "");
                    }
                } else {
                    ToastUtils.getToast(getContext(), "请先登录！");
                }

            }
        });*/
        adapter.setOnItemClickLitener(new HomeRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, HomeRoomsBean.DataBean roomBean) {
                if (PTApplication.myInfomation != null) {
                    String roomId = String.valueOf(roomBean.getId());
                    int state = roomBean.getState();
                    Logger.e("state:" + state);
                    switch (state) {
                        case 0:
                            if (roomBean.isLocked()) {
                                for (HomeRoomsBean.DataBean.JoinMembersBean joinMembersBean : roomBean.getJoinMembers()) {
                                    if (joinMembersBean.getId() == PTApplication.myInfomation.getData().getId()) {
                                        joinRooms(roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD);
                                        return;
                                    }
                                }
                                initPopupWindow(view, roomId);
                            } else {
                                joinRooms(roomId, "");
                            }
                            break;
                        case 1:
                        case 2:
                        case 3:
                            Logger.e("state:" + state);
                            ToastUtils.getToast(getContext(), "活动已开始,无法加入聊天室");
                            break;
                        case 4:
                            Logger.e("state:" + state);
                            Intent intent = new Intent(getContext(), GameFinishActivity.class);
                            intent.putExtra("roomId", roomBean.getId());
                            startActivity(intent);
                            break;
                    }

                } else {
                    ToastUtils.getToast(getContext(), "请先登录！");
                }
            }
        });

        // 下啦刷新
        srl_circle_rooms_fmt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatas(0, 15, false);
                    }
                }, 10);
            }
        });
        //上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastCompletelyVisibleItem;
            int firstCompletelyVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastCompletelyVisibleItem + 1 == adapter.getItemCount()
                            && firstCompletelyVisibleItem != 0
                            && adapter.getmLoadMoreStatus() != adapter.NO_LOAD_MORE
                            ) {
                        adapter.changeMoreStatus(adapter.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initDatas(++Page, 15, true);
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
        initDatas(0, 15, false);
        return view;
    }

    private void joinRooms(final String roomId, String pwd) {
        PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        Logger.w(noDataBean.toString());
                        if (noDataBean.isSuccess()) {
                            startActivity(new Intent(getContext(), GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
                        } else {
                            ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                        }
                    }
                });
    }

    private void initDatas(int page, int size, boolean isLoadMore) {
        if (isLoadMore) {
            PTApplication.getRequestService().findRoomsByCircle(circleId, page, size, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HomeRoomsBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError", e.getMessage());
                        }

                        @Override
                        public void onNext(HomeRoomsBean myJoinRoomBean) {
                            if (myJoinRoomBean.isSuccess()) {
                                adapter.getList().addAll(myJoinRoomBean.getData());
                                if (myJoinRoomBean.getData().size() == 15) {
                                    adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
                                } else {
                                    adapter.changeMoreStatus(adapter.NO_LOAD_MORE);
                                }
                            }
                        }
                    });
            adapter.notifyDataSetChanged();
        } else {
            PTApplication.getRequestService().findRoomsByCircle(circleId, page, size, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HomeRoomsBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError", e.getMessage());
                        }

                        @Override
                        public void onNext(HomeRoomsBean homeRoomsBean) {
                            if (homeRoomsBean.isSuccess()) {
                                adapter.setList(homeRoomsBean.getData());
                                adapter.notifyDataSetChanged();
                                srl_circle_rooms_fmt.setRefreshing(false);
                            }
                        }
                    });
        }

    }

    //弹出输入密码pop
    private void initPopupWindow(View view, final String roomId) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_inputpwd, null);
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
        final EditText pwdString = (EditText) contentView.findViewById(R.id.et_joinroom_pwd_pop);
        Button joinRoom = (Button) contentView.findViewById(R.id.bt_joinroom_join_fmt);
        Button cancel = (Button) contentView.findViewById(R.id.bt_joinroom_cancel_fmt);
        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = pwdString.getText().toString().trim();
                popupWindow.dismiss();
                joinRooms(roomId, pwd);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
