package pro.yueyuan.project_t.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Key on 2017/3/28 16:22
 * email: MrKey.K@gmail.com
 * description:
 */

public class RealmFriendBean extends RealmObject {
    @PrimaryKey
    private long id;

    private String nickname;
    private int point;
    private String lastMessage;
    private long lastTime;
    private int unreadCount;

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

    @Override
    public String toString() {
        return "FriendBean{" +
                "昵称:'" + nickname + '\'' +
                ", id=" + id +
                ", 好感度:" + point +
                ", 最后一条消息:'" + lastMessage + '\'' +
                ", 最后一条消息的发送时间:" + lastTime +
                ", 未拉取消息:" + unreadCount +
                '}';
    }
}
