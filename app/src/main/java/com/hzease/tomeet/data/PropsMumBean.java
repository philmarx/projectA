package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/6/2.
 */

public class PropsMumBean {

    /**
     * success : true
     * msg :
     * data : {"noteCount":994,"labelClearCount":1,"changeNicknameCount":1,"canReachedMovieTicket":0,"needRecommends":3,"vipExpireDate":1508492623000,"movieTicket":0,"signCount":0}
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
        /**
         * noteCount : 994
         * labelClearCount : 1
         * changeNicknameCount : 1
         * canReachedMovieTicket : 0
         * needRecommends : 3
         * vipExpireDate : 1508492623000
         * movieTicket : 0
         * signCount : 0
         */

        private int noteCount;
        private int labelClearCount;
        private int changeNicknameCount;
        private int canReachedMovieTicket;
        private int needRecommends;
        private long vipExpireDate;
        private int movieTicket;
        private int signCount;

        public int getNoteCount() {
            return noteCount;
        }

        public void setNoteCount(int noteCount) {
            this.noteCount = noteCount;
        }

        public int getLabelClearCount() {
            return labelClearCount;
        }

        public void setLabelClearCount(int labelClearCount) {
            this.labelClearCount = labelClearCount;
        }

        public int getChangeNicknameCount() {
            return changeNicknameCount;
        }

        public void setChangeNicknameCount(int changeNicknameCount) {
            this.changeNicknameCount = changeNicknameCount;
        }

        public int getCanReachedMovieTicket() {
            return canReachedMovieTicket;
        }

        public void setCanReachedMovieTicket(int canReachedMovieTicket) {
            this.canReachedMovieTicket = canReachedMovieTicket;
        }

        public int getNeedRecommends() {
            return needRecommends;
        }

        public void setNeedRecommends(int needRecommends) {
            this.needRecommends = needRecommends;
        }

        public long getVipExpireDate() {
            return vipExpireDate;
        }

        public void setVipExpireDate(long vipExpireDate) {
            this.vipExpireDate = vipExpireDate;
        }

        public int getMovieTicket() {
            return movieTicket;
        }

        public void setMovieTicket(int movieTicket) {
            this.movieTicket = movieTicket;
        }

        public int getSignCount() {
            return signCount;
        }

        public void setSignCount(int signCount) {
            this.signCount = signCount;
        }
    }
}
