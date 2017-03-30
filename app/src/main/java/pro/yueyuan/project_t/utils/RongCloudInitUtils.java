package pro.yueyuan.project_t.utils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.List;

import io.realm.Realm;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.FriendListBean;
import pro.yueyuan.project_t.data.RealmFriendBean;
import pro.yueyuan.project_t.widget.MyRongReceiveMessageListener;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/3/21 12:33
 * email: MrKey.K@gmail.com
 * description:
 */

public class RongCloudInitUtils {

    /**
     * 判断后初始化融云
     */
    public void RongCloudInit() {
        if (!PTApplication.isRongCloudInit && !TextUtils.isEmpty(PTApplication.userId) && !TextUtils.isEmpty(PTApplication.userToken)) {
            // 初始化
            RongIM.init(PTApplication.getInstance());

            // Rong 接收消息监听 this在主线程
            RongIM.setOnReceiveMessageListener(new MyRongReceiveMessageListener());
            // Rong 发送消息监听(最好还是写在Activity里面,为了更新画面,和注销)

            // 建立连接
            RongIM.connect(PTApplication.userToken, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Logger.e("onTokenIncorrect", "onTokenIncorrect");
                }

                @Override
                public void onSuccess(String s) {
                    PTApplication.isRongCloudInit = true;
                    Logger.i("userid: " + s + "   PTApplication.isRongCloudInit:  " + PTApplication.isRongCloudInit);
                    PTApplication.getRequestService().getFriendList(PTApplication.userId, PTApplication.userToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<FriendListBean>() {
                                @Override
                                public void onCompleted() {
                                    Logger.i("读取好友完毕");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e(e.getMessage());
                                }

                                @Override
                                public void onNext(FriendListBean friendListBean) {
                                    if (friendListBean.isSuccess() && friendListBean.getData().size() > 0) {
                                        // 如果第一次登录,有好友.无会话消息,先把所有好友插进数据库,再把更新会话信息
                                        for (final RealmFriendBean friendBean : friendListBean.getData()) {
                                            Realm realm = Realm.getDefaultInstance();
                                            try {
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        // 增删改
                                                        realm.insertOrUpdate(friendBean);
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                realm.close();
                                            }
                                        }

                                        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                                            @Override
                                            public void onSuccess(List<Conversation> conversations) {
                                                if (conversations != null && conversations.size() > 0) {
                                                    for (final Conversation conversation : conversations) {
                                                        Realm realm = Realm.getDefaultInstance();
                                                        try {
                                                            realm.executeTransaction(new Realm.Transaction() {
                                                                @Override
                                                                public void execute(Realm realm) {
                                                                    // 增删改
                                                                    // 有没有可能,有会话,没好友?还是判断下吧
                                                                    RealmFriendBean first = realm.where(RealmFriendBean.class).equalTo("id", Long.valueOf(conversation.getTargetId())).findFirst();
                                                                    if (first != null) {
                                                                        first.setUnreadCount(conversation.getUnreadMessageCount());
                                                                        first.setLastMessage(new TextMessage(conversation.getLatestMessage().encode()).getContent());
                                                                        first.setLastTime(conversation.getReceivedTime());
                                                                    }
                                                                }
                                                            });
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        } finally {
                                                            realm.close();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {
                                                Logger.e(errorCode.getMessage());
                                            }
                                        });
                                    }
                                }
                            });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.e("onError", errorCode.getMessage());
                }
            });

            /**
             * 取消 SDK 默认的 ExtensionModule，注册自定义的 ExtensionModule
             * 聊天消息的扩展,为了使用发送位置
             */
            //List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
            //IExtensionModule defaultModule = null;
            /*if (moduleList != null) {
                for (IExtensionModule module : moduleList) {
                    if (module instanceof DefaultExtensionModule) {
                        defaultModule = module;
                        break;
                    }
                }
                if (defaultModule != null) {
                    RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                    RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
                }
            }*/
        }
    }
}
