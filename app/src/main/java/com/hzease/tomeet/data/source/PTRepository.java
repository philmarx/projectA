package com.hzease.tomeet.data.source;

import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by Key on 2016/11/28 22:36
 * email: MrKey.K@gmail.com
 * description:
 */

public class PTRepository implements IPTDataSource {

    private final IPTDataSource mPTLocalDataSource;

    private final IPTDataSource mPTRemoteDataSource;

    @Inject
    public PTRepository(@Local IPTDataSource mPTLocalDataSource, @Remote IPTDataSource mPTRemoteDataSource) {
        this.mPTLocalDataSource = mPTLocalDataSource;
        this.mPTRemoteDataSource = mPTRemoteDataSource;
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
        mPTLocalDataSource.saveUserIdAndToken();
    }

    /**
     * 读取用户信息,先本地,后网络
     */
    @Override
    public void loadUserIdAndToken() {
        mPTLocalDataSource.loadUserIdAndToken();
    }
}
