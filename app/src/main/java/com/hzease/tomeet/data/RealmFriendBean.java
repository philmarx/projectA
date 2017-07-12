package com.hzease.tomeet.data;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Key on 2017/3/28 16:22
 * email: MrKey.K@gmail.com
 * description:
 */

public class RealmFriendBean extends RealmObject {
    @PrimaryKey
    @Index
    private long id;

    private String nickname;
    private int point;
    private String lastMessage;
    private long lastTime;
    private String avatarSignature;
    private int unreadCount;
    @Ignore
    private boolean isChoose;
    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }


    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    private boolean vip;
    public String getAvatarSignature() {
        return avatarSignature;
    }

    public void setAvatarSignature(String avatarSignature) {
        this.avatarSignature = avatarSignature;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void receiveMessage() {
        this.unreadCount++;
    }

    @Override
    public String toString() {
        return "RealmFriendBean{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", point=" + point +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastTime=" + lastTime +
                ", avatarSignature='" + avatarSignature + '\'' +
                ", unreadCount=" + unreadCount +
                ", isChoose=" + isChoose +
                ", vip=" + vip +
                '}';
    }
}
