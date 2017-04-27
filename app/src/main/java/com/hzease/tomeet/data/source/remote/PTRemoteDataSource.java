package com.hzease.tomeet.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import com.hzease.tomeet.data.source.IPTDataSource;

/**
 * Created by Key on 2016/11/28 22:37
 * email: MrKey.K@gmail.com
 * description: 服务器数据
 */
@Singleton
public class PTRemoteDataSource implements IPTDataSource {

    //  Context mContext;

    public PTRemoteDataSource(@NonNull Context context) {
        // mContext = checkNotNull(context);
    }


    /**
     * 获取头像
     *
     * @param userId 用户ID
     */
    @Override
    public void getAvatar(@NonNull String userId) {

    }

    /**
     * 保存用户名密码,直接从PTApplication里面读,不需要传参数
     */
    @Override
    public void saveUserIdAndToken() {
        // 只是本地方法
    }

    /**
     * 读取用户信息,先本地,后网络
     */
    @Override
    public void loadUserIdAndToken() {

    }
}
