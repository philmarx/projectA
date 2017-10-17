package com.hzease.tomeet.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.OneNoteData;
import com.hzease.tomeet.data.RoomStateBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.me.ui.GameEvaluateActivity;
import com.hzease.tomeet.me.ui.GameFinishActivity;
import com.hzease.tomeet.me.ui.ShareWebViewActivity;
import com.hzease.tomeet.utils.TimeUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/23.
 */

public class MyRongConversationListener implements RongIM.ConversationBehaviorListener {

    public MyRongConversationListener() {

    }

    /**
     * 点击头像后的处理
     *
     * @param context
     * @param conversationType
     * @param userInfo
     * @return
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        // 用户ID 不为11位的都不进个人中心
        if (userInfo.getUserId().length() != 11) {
            return false;
        } else {
            Intent intent = new Intent(context, PersonOrderInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("userId", Long.valueOf(userInfo.getUserId()));
            intent.putExtras(bundle);
            context.startActivity(intent);
            return true;
        }
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(final Context context, final View view, Message message) {
        // Logger.e("message: " + message.getObjectName());
        if (message.getObjectName().equals("RC:ImgTextMsg")) {
            RichContentMessage richContentMessage = new RichContentMessage(message.getContent().encode());
            if (!TextUtils.isEmpty(richContentMessage.getExtra())) {
                // 跳转到房间
                Uri uri = Uri.parse(richContentMessage.getExtra());
                Logger.e("uri: " + uri.toString() + "   action: " + uri.getQueryParameter("action"));
                switch (uri.getQueryParameter("action")) {
                    case "invited":
                        // roomId
                        final String roomId = uri.getQueryParameter("key1");
                        if (PTApplication.myInfomation != null) {
                            PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<NoDataBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e("joinRoom error: " + e.getMessage());
                                        }

                                        @Override
                                        public void onNext(NoDataBean noDataBean) {
                                            Logger.e(noDataBean.toString());
                                            if (noDataBean.isSuccess()) {
                                                PTApplication.getInstance().startActivity(new Intent(PTApplication.getInstance(), GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            } else {
                                                ToastUtils.getToast(noDataBean.getMsg());
                                            }
                                        }
                                    });
                        } else {
                            // 如果用户没登录
                            ToastUtils.getToast("请先登陆后再加入房间");
                        }
                        break;
                    case "share":
                        // userId
                        final String userId = uri.getQueryParameter("key1");
                        if (PTApplication.myInfomation != null) {
                            PTApplication.getRequestService().becameFriend(PTApplication.userToken, PTApplication.userId, userId, uri.getQueryParameter("key2"))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<NoDataBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e("becameFriend error: " + e.getMessage());
                                        }

                                        @Override
                                        public void onNext(NoDataBean noDataBean) {
                                            Logger.e(noDataBean.toString());
                                            if (!TextUtils.isEmpty(noDataBean.getMsg()))
                                                ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    });
                        } else {
                            // 如果用户没登录
                            ToastUtils.getToast("请先登陆后再点此链接");
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            } else {
                Logger.e(message.getObjectName());
                return false;
            }
        } else if (message.getObjectName().equals("RC:TxtMsg")) {
            String extra = ((TextMessage) message.getContent()).getExtra();
            if (!TextUtils.isEmpty(extra)) {
                try {
                    final JSONObject jsonObject = new JSONObject(extra);
                    String type = jsonObject.getString("type");
                    Logger.e(jsonObject.toString());
                    Logger.e(type);
                    switch (type) {
                        case "note":
                            // 点击小纸条
                            Logger.e(jsonObject.toString());
                            // TODO: 2017/9/15 显示小纸条
                            int noteId = jsonObject.getInt("id");
                            PTApplication.getRequestService().findOneNote(noteId, PTApplication.userToken, PTApplication.userId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<OneNoteData>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e("findOneNote error: " + e.getMessage());
                                        }

                                        @Override
                                        public void onNext(OneNoteData oneNoteData) {
                                            if (oneNoteData.isSuccess()) {
                                                initPopupWindow(view, context, oneNoteData, false);
                                            } else {
                                                ToastUtils.getToast(oneNoteData.getMsg());
                                            }
                                        }
                                    });
                            break;
                        case "room":
                            Logger.e(jsonObject.toString());
                            final String roomId = jsonObject.getString("id");
                            Logger.e(roomId);
                            PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<NoDataBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e("joinRoom error: " + e.getMessage());
                                        }

                                        @Override
                                        public void onNext(NoDataBean noDataBean) {
                                            if (noDataBean.isSuccess()) {
                                                context.startActivity(new Intent(context, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
                                            } else {
                                                Logger.e(noDataBean.getMsg());
                                                if (noDataBean.getMsg().equals("该房间ID不存在")) {
                                                    ToastUtils.getToast("房间已解散或者不存在");
                                                } else {
                                                    ToastUtils.getToast(noDataBean.getMsg());
                                                }
                                            }
                                        }
                                    });
                            break;
                        case "evaluation":
                            final long roomIdtoEvalutaion = jsonObject.getLong("id");
                            PTApplication.getRequestService().findRoomInfo(roomIdtoEvalutaion,PTApplication.userToken, PTApplication.userId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<RoomStateBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(RoomStateBean roomStateBean) {
                                            if (roomStateBean.isSuccess()){
                                                if (roomStateBean.getData().getState() == 3){
                                                    if (roomStateBean.getData().isEvaluated()){
                                                        ToastUtils.getToast("您已经评价过该房间了");
                                                    }else{
                                                        Intent intent = new Intent(context, GameEvaluateActivity.class);
                                                        intent.putExtra("roomId", roomIdtoEvalutaion);
                                                        context.startActivity(intent);
                                                    }
                                                }else{
                                                    Intent intent = new Intent(context,GameFinishActivity.class);
                                                    intent.putExtra("roomId",roomIdtoEvalutaion);
                                                    context.startActivity(intent);
                                                }
                                            }
                                        }
                                    });

                            break;
                        case "getBounty":
                            Intent intent = new Intent(context, ShareWebViewActivity.class);
                            intent.putExtra("url", jsonObject.getString("url"));
                            context.startActivity(intent);
                            break;
                        default:
                            return false;
                    }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.getToast("参数无效，本消息不可点击");
                    return false;
                }
            } else {
                return false;
            }
        } else {
            Logger.e(message.getObjectName());
            return false;
        }
    }

    private void initPopupWindow(View view, final Context context, final OneNoteData oneNoteData, boolean isSend) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_smallpaper_recevie, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        final Activity activity = ((Activity) view.getContext());
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = activity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        activity.getWindow().setAttributes(wlBackground);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                activity.getWindow().setAttributes(wlBackground);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        //两个按钮
        AutoLinearLayout two_state_pop = contentView.findViewById(R.id.two_state_pop);
        //按钮
        AutoLinearLayout all_state_pop = contentView.findViewById(R.id.all_state_pop);
        //发送者头像
        CircleImageView senderIcon = contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        TextView time = contentView.findViewById(R.id.tv_paper_time);
        time.setText(TimeUtils.calculateTime(oneNoteData.getData().getCreateTime()));
        senderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId", oneNoteData.getData().getSenderId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        //头像
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + oneNoteData.getData().getSenderId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(oneNoteData.getData().getAvatarSignature()))
                .into(senderIcon);
        //发送者的名字
        TextView senderName = contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        senderName.setText(oneNoteData.getData().getNickname());
        //纸条内容
        NoteEditor content = contentView.findViewById(R.id.ne_smallpager_content_fmt);
        content.setText(oneNoteData.getData().getContent());
        Button delete = contentView.findViewById(R.id.bt_smallpager_delete_pop);
        Button delete_bak = contentView.findViewById(R.id.bt_smallpager_delete_pop_bak);
        Button save_bak = contentView.findViewById(R.id.bt_smallpager_save_pop_bak);
        if (isSend) {
            two_state_pop.setVisibility(View.GONE);
            all_state_pop.setVisibility(View.GONE);
        } else {
            if (oneNoteData.getData().getState() != 0) {
                all_state_pop.setVisibility(View.GONE);
            } else {
                all_state_pop.setVisibility(View.VISIBLE);
                two_state_pop.setVisibility(View.GONE);
            }
            if (oneNoteData.getData().getState() == 4) {
                two_state_pop.setVisibility(View.VISIBLE);
            }
        }
        //另一个删除
        delete_bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().deleteNote(oneNoteData.getData().getId(), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        //删除小纸条
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().deleteNote(oneNoteData.getData().getId(), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        //收起放好
        Button read = contentView.findViewById(R.id.bt_smallpaper_read_pop);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().saveNote(oneNoteData.getData().getId(), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        Button reply = contentView.findViewById(R.id.bt_smallpager_reply_fmt);
        if (oneNoteData.getData().getState() == 2) {
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.getToast("您已经回复过改小纸条了");
                }
            });
        } else {
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    initPopupWindowReply(context, v, oneNoteData.getData().getSenderId(), oneNoteData.getData().getAvatarSignature(), oneNoteData.getData().getNickname(), oneNoteData.getData().getId());
                }
            });
        }
        //已读回复的小纸条
        save_bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().readReplyNote(String.valueOf(oneNoteData.getData().getSenderId()), PTApplication.userId, PTApplication.userToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e("onNext" + noDataBean.isSuccess());
                                if (noDataBean.isSuccess()) {
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //回复弹窗
    private void initPopupWindowReply(Context context, View view, final long userId, String avatarSignature, String nickName, final long noteId) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_smallpaper, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final Activity activity = ((Activity) view.getContext());
        final WindowManager.LayoutParams wlBackground = activity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        activity.getWindow().setAttributes(wlBackground);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                activity.getWindow().setAttributes(wlBackground);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        final NoteEditor content = contentView.findViewById(R.id.ne_smallpager_content_fmt);
        CircleImageView head = contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(avatarSignature))
                .into(head);
        TextView name = contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        name.setText(nickName);
        Button sendNote = contentView.findViewById(R.id.bt_smallpager_send_fmt);
        Button dismiss = contentView.findViewById(R.id.bt_smallpager_cancel_fmt);
        sendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送纸条
                Logger.e(userId + "");
                PTApplication.getRequestService().replyNote(content.getText().toString().trim(), String.valueOf(noteId), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {
                                Logger.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.getToast(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e(noDataBean.isSuccess() + "");
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast("回复纸条成功");
                                    popupWindow.dismiss();
                                } else {
                                    ToastUtils.getToast(noDataBean.getMsg());
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}
