package com.hzease.tomeet.game;

import android.support.v7.util.DiffUtil;

import com.hzease.tomeet.data.GameChatRoomBean;

import java.util.List;

/**
 * Created by Key on 2017/5/8 22:14
 * email: MrKey.K@gmail.com
 * description:
 */

public class MemberDiffCallback extends DiffUtil.Callback {

    private List<GameChatRoomBean.DataBean.JoinMembersBean> oldList;
    private List<GameChatRoomBean.DataBean.JoinMembersBean> newList;

    public MemberDiffCallback(List<GameChatRoomBean.DataBean.JoinMembersBean> oldList, List<GameChatRoomBean.DataBean.JoinMembersBean> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).isReady() == newList.get(newItemPosition).isReady()
                && oldList.get(oldItemPosition).isAttend() == newList.get(newItemPosition).isAttend()
                && oldList.get(oldItemPosition).isSigned() == newList.get(newItemPosition).isSigned();
    }
}
