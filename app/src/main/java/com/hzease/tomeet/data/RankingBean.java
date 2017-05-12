package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/4/5.
 */

public class RankingBean {

    /**
     * success : true
     * msg :
     * data : [{"userId":10000000025,"nickname":"马健原","point":1000,"ranking":1,"gameId":7,"gameName":"三国杀","avatarSignature":"1494220981231","count":1}]
     */

    private boolean success;
    private String msg;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 10000000025
         * nickname : 马健原
         * point : 1000
         * ranking : 1
         * gameId : 7
         * gameName : 三国杀
         * avatarSignature : 1494220981231
         * count : 1
         */

        private long userId;
        private String nickname;
        private int point;
        private int ranking;
        private int gameId;
        private String gameName;
        private String avatarSignature;
        private int count;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
