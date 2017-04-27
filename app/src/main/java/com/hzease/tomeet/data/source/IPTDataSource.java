package com.hzease.tomeet.data.source;

import android.support.annotation.NonNull;

/**
 * Created by Key on 2016/11/28 22:34
 * email: MrKey.K@gmail.com
 * description:
 */

public interface IPTDataSource {

    /**
     * 获取头像
     * @param userId 用户ID
     */
    void getAvatar(@NonNull String userId);

    /**
     *保存用户名密码,直接从PTApplication里面读,不需要传参数
     */
    void saveUserIdAndToken();

    /**
     * 读取用户信息,先本地,后网络
     */
    void loadUserIdAndToken();
}
