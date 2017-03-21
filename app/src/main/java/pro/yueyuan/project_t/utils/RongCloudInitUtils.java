package pro.yueyuan.project_t.utils;

import com.orhanobut.logger.Logger;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import pro.yueyuan.project_t.MyExtensionModule;
import pro.yueyuan.project_t.PTApplication;

/**
 * Created by Key on 2017/3/21 12:33
 * email: MrKey.K@gmail.com
 * description:
 */

public class RongCloudInitUtils {

    /**
     * 融云初始化
     */
    public void RongCloudInit() {
        RongIM.init(PTApplication.getInstance());
        RongIM.connect(PTApplication.userToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Logger.e("onTokenIncorrect", "onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                Logger.e("userid: " + s);
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
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
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
        }
    }
}
