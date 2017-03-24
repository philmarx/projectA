package pro.yueyuan.project_t.utils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.ConversationListBean;
import pro.yueyuan.project_t.data.FriendListBean;
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
            RongIM.init(PTApplication.getInstance());
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
                                    Logger.i(PTApplication.mConversationListBean.toString());
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(FriendListBean friendListBean) {
                                    if (friendListBean.isSuccess()) {
                                        for (ConversationListBean.FriendBean friendBean : friendListBean.getData()) {
                                            switch(friendBean.getPoint()) {
                                                case 1:
                                                case 2:
                                                    PTApplication.mConversationListBean.getRedList().add(friendBean);
                                                    break;
                                                case 3:
                                                case 4:
                                                    PTApplication.mConversationListBean.getGrayList().add(friendBean);
                                                    break;
                                                case 5:
                                                case 6:
                                                    PTApplication.mConversationListBean.getGreenList().add(friendBean);
                                                    break;
                                                case 7:
                                                case 8:
                                                    PTApplication.mConversationListBean.getBlueList().add(friendBean);
                                                    break;
                                                case 9:
                                                case 10:
                                                    PTApplication.mConversationListBean.getGoldList().add(friendBean);
                                                    break;
                                            }
                                        }
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
