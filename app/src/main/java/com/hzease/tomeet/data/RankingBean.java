package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/4/5.
 */

public class RankingBean {
    @Override
    public String toString() {
        return "RankingBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * msg :
     * data : [{"userId":10000000007,"nickname":"test2","point":382590,"ranking":1},{"userId":10000000009,"nickname":"张国文","point":325028,"ranking":2},{"userId":10000000008,"nickname":"test56","point":308523,"ranking":3},{"userId":10000000010,"nickname":"test54","point":251514,"ranking":4},{"userId":10000000012,"nickname":"test75","point":203237,"ranking":5},{"userId":10000000011,"nickname":"test77","point":196650,"ranking":6},{"userId":10000000001,"nickname":"test5","point":167146,"ranking":7},{"userId":10000000000,"nickname":"test4","point":94218,"ranking":8},{"userId":10000000020,"nickname":"徐强","point":75996,"ranking":9}]
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
        @Override
        public String toString() {
            return "DataBean{" +
                    "userId=" + userId +
                    ", nickname='" + nickname + '\'' +
                    ", point=" + point +
                    ", ranking=" + ranking +
                    '}';
        }

        /**
         * userId : 10000000007
         * nickname : test2
         * point : 382590
         * ranking : 1
         */

        private long userId;
        private String nickname;
        private int point;
        private int ranking;

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
    }
}
