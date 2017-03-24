package pro.yueyuan.project_t.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Key on 2017/3/24 22:38
 * email: MrKey.K@gmail.com
 * description:
 */

public class ConversationListBean {

    private List<FriendBean> goldList;
    private List<FriendBean> blueList;
    private List<FriendBean> greenList;
    private List<FriendBean> grayList;
    private List<FriendBean> redList;

    public ConversationListBean() {
        this.goldList = new ArrayList<>();
        this.blueList = new ArrayList<>();
        this.greenList = new ArrayList<>();
        this.grayList = new ArrayList<>();
        this.redList = new ArrayList<>();
    }

    public List<FriendBean> getGoldList() {
        return goldList;
    }

    public void setGoldList(List<FriendBean> goldList) {
        this.goldList = goldList;
    }

    public List<FriendBean> getBlueList() {
        return blueList;
    }

    public void setBlueList(List<FriendBean> blueList) {
        this.blueList = blueList;
    }

    public List<FriendBean> getGreenList() {
        return greenList;
    }

    public void setGreenList(List<FriendBean> greenList) {
        this.greenList = greenList;
    }

    public List<FriendBean> getGrayList() {
        return grayList;
    }

    public void setGrayList(List<FriendBean> grayList) {
        this.grayList = grayList;
    }

    public List<FriendBean> getRedList() {
        return redList;
    }

    public void setRedList(List<FriendBean> redList) {
        this.redList = redList;
    }

    @Override
    public String toString() {
        return "ConversationListBean{" +
                "金:" + goldList +
                ", 蓝:" + blueList +
                ", 绿:" + greenList +
                ", 灰:" + grayList +
                ", 红:" + redList +
                '}';
    }

    public static class FriendBean {
        /**
         * nickname : test12
         * id : 10000000009
         * point : 5
         */

        private String nickname;
        private long id;
        private int point;

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
                    "昵称='" + nickname + '\'' +
                    ", id=" + id +
                    ", 好感度=" + point +
                    '}';
        }
    }
}
