package com.hzease.tomeet.utils.autoUpdate;

import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.AppVersionBean;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import io.rong.eventbus.EventBus;

public class AutoUpdateService extends AVersionService {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("AutoUpdateService自动更新 - Not yet implemented");
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        Logger.e("response: " + response);

        AppVersionBean appVersionBean = new Gson().fromJson(response, AppVersionBean.class);
        // TODO: 2017/9/14 切割两个点进行对比
        if (appVersionBean.isSuccess() && !TextUtils.isEmpty(PTApplication.appVersion) && !appVersionBean.getData().getVersion().equals(PTApplication.appVersion)) {
            long time = SpUtils.getLongValue(PTApplication.getInstance(), AppConstants.TOMMET_SP_UPDATE_TIME);
            long currentTimeMillis = System.currentTimeMillis();
            if (appVersionBean.getData().isRemain() || (currentTimeMillis - time) > 1000 * 60 * 60 * 24) {
                getVersionParams().setAppVersion(appVersionBean.getData().getVersion());
                getVersionParams().setForceUpdate(appVersionBean.getData().isForce());
                service.showVersionDialog(appVersionBean.getData().getDownUrl(), "检测到新版本", appVersionBean.getData().getMessage());
                SpUtils.saveLong(PTApplication.getInstance(), AppConstants.TOMMET_SP_UPDATE_TIME, currentTimeMillis);
            }
        } else {
            if (AVersionService.MANUAL.equals(getType())) {
                ToastUtils.getToast("当前已是最新版");
                EventBus.getDefault().post(new EventBean.updateCheckFinish());
            }
        }
    }
}