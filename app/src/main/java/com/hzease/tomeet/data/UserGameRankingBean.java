package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/5/17 14:13
 * email: MrKey.K@gmail.com
 * description:
 */

public class UserGameRankingBean {

    /**
     * success : true
     * msg :
     * data : {"userId":10000000001,"nickname":"😏😏😏","point":2000,"ranking":2,"gameId":6,"gameName":"狼人杀","avatarSignature":"1493193505823","count":1,"gender":true}
     */

    private boolean success;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        @Override
        public String toString() {
            return "DataBean{" +
                    "userId=" + userId +
                    ", nickname='" + nickname + '\'' +
                    ", point=" + point +
                    ", ranking=" + ranking +
                    ", gameId=" + gameId +
                    ", gameName='" + gameName + '\'' +
                    ", avatarSignature='" + avatarSignature + '\'' +
                    ", count=" + count +
                    ", gender=" + gender +
                    '}';
        }

        /**
         * userId : 10000000001
         * nickname : 😏😏😏
         * point : 2000
         * ranking : 2
         * gameId : 6
         * gameName : 狼人杀
         * avatarSignature : 1493193505823
         * count : 1
         * gender : true
         */

        private long userId;
        private String nickname;
        private int point;
        private int ranking;
        private int gameId;
        private String gameName;
        private String avatarSignature;
        private int count;
        private boolean gender;

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

        public boolean getGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }
    }
}
