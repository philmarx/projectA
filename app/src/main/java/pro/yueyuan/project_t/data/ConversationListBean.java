package pro.yueyuan.project_t.data;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Key on 2017/3/24 22:38
 * email: MrKey.K@gmail.com
 * description:
 */

public class ConversationListBean {

    private List<RealmFriendBean> goldList;
    private List<RealmFriendBean> blueList;
    private List<RealmFriendBean> greenList;
    private List<RealmFriendBean> grayList;
    private List<RealmFriendBean> redList;
    private List<RealmFriendBean> blankList;

    public ConversationListBean() {
        this.goldList = new ArrayList<>();
        this.blueList = new ArrayList<>();
        this.greenList = new ArrayList<>();
        this.grayList = new ArrayList<>();
        this.redList = new ArrayList<>();
        this.blankList = new ArrayList<>();
    }

    public List<RealmFriendBean> getList(String type) {
        switch (type) {
            case "gold":
                Logger.i("gold:  " + type);
                return getGoldList();
            case "blue":
                Logger.i("blue:  " + type);
                return getBlueList();
            case "green":
                Logger.i("green:  " + type);
                return getGreenList();
            case "gray":
                Logger.i("gray:  " + type);
                return getGrayList();
            case "red":
                Logger.i("red:  " + type);
                return getRedList();
            case "blank":
                Logger.i("blank:  " + type);
                return getBlankList();
        }
        return null;
    }

    public List<RealmFriendBean> getGoldList() {
        return goldList;
    }

    public void setGoldList(List<RealmFriendBean> goldList) {
        this.goldList = goldList;
    }

    public List<RealmFriendBean> getBlueList() {
        return blueList;
    }

    public void setBlueList(List<RealmFriendBean> blueList) {
        this.blueList = blueList;
    }

    public List<RealmFriendBean> getGreenList() {
        return greenList;
    }

    public void setGreenList(List<RealmFriendBean> greenList) {
        this.greenList = greenList;
    }

    public List<RealmFriendBean> getGrayList() {
        return grayList;
    }

    public void setGrayList(List<RealmFriendBean> grayList) {
        this.grayList = grayList;
    }

    public List<RealmFriendBean> getRedList() {
        return redList;
    }

    public void setRedList(List<RealmFriendBean> redList) {
        this.redList = redList;
    }

    public List<RealmFriendBean> getBlankList() {
        return blankList;
    }

    public void setBlankList(List<RealmFriendBean> blankList) {
        this.blankList = blankList;
    }

    @Override
    public String toString() {
        return "ConversationListBean{" +
                "金:" + goldList +
                ", 蓝:" + blueList +
                ", 绿:" + greenList +
                ", 灰:" + grayList +
                ", 红:" + redList +
                ", 不显示的单相思:" + blankList +
                '}';
    }
}
