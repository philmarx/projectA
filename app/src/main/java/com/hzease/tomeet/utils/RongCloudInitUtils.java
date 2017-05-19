package com.hzease.tomeet.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.FriendListBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.widget.MyRongReceiveMessageListener;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

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

            // 初始化数据库配置文件
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                    .name(PTApplication.userId + ".realm")
                    .schemaVersion(1)
                    .build();
            Realm.setDefaultConfiguration(realmConfiguration);
            Logger.w("Realm名字: " + realmConfiguration.getRealmFileName() + "      path: " + realmConfiguration.getPath());

            // 极光测试别名
            JPushInterface.setAlias(PTApplication.getInstance(), PTApplication.userId, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    Logger.i("极光setAlias：  s: " + s + "  i:  " + i + "  set:  " + set);
                }
            });


            /*RongPushClient.registerHWPush(PTApplication.getInstance());
            RongPushClient.registerMiPush(PTApplication.getInstance(), "2882303761517473625", "5451747338625");
            try {
                RongPushClient.registerGCM(PTApplication.getInstance());
            } catch (RongException e) {
                e.printStackTrace();
            }*/



            // Rong 接收消息监听 this在主线程
            RongIM.setOnReceiveMessageListener(new MyRongReceiveMessageListener());
            // Rong 发送消息监听(最好还是写在Activity里面,为了更新画面,和注销)
            // RongIM.getInstance().setSendMessageListener(new MyRongSendMessageListener());

            // 建立连接
            RongIM.connect(PTApplication.userToken, new RongIMClient.ConnectCallback() {
                {Logger.e("创建RongIMClient.ConnectCallback()");}
                @Override
                public void onTokenIncorrect() {
                    Logger.e("RongIM.connect - Token错误");
                    PTApplication.userId = "";
                    PTApplication.userToken = "";
                    // 注销个人信息
                    PTApplication.myInfomation = null;
                    // 清空本地保存
                    SharedPreferences.Editor editor = PTApplication.getInstance().getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE).edit();
                    editor.putString("userId", String.valueOf(PTApplication.userId));
                    editor.putString("userToken", PTApplication.userToken);
                    editor.apply();
                    // 注销融云
                    if (PTApplication.isRongCloudInit) {
                        RongIM.getInstance().logout();
                        PTApplication.isRongCloudInit = false;
                    }
                    Realm.removeDefaultConfiguration();
                    // 注销阿里云OSS
                    PTApplication.aliyunOss = null;
                    PTApplication.aliyunOssExpiration = 0;
                    // 停止发送友盟用户信息
                    MobclickAgent.onProfileSignOff();
                }

                @Override
                public void onSuccess(String s) {
                    PTApplication.isRongCloudInit = true;
                    Logger.i("userid: " + s + "   融云是否初始化:  " + PTApplication.isRongCloudInit);
                    reflushFriends();
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
            /*List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
            IExtensionModule defaultModule = null;
            if (moduleList != null) {
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

    /**
     * 刷新好友列表
     */
    public static void reflushFriends() {
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
                        Logger.i(friendListBean.getData().toString());
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
}
