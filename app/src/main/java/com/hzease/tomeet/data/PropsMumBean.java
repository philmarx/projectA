package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/6/2.
 */

public class PropsMumBean {
    /**
     * success : true
     * msg :
     * data : {"noteCount":1,"labelClearCount":1,"changeNicknameCount":1,"vipExpireDate":1496472749769,"signCount":1}
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
         * noteCount : 1
         * labelClearCount : 1
         * changeNicknameCount : 1
         * vipExpireDate : 1496472749769
         * signCount : 1
         */

        private int noteCount;
        private int labelClearCount;
        private int changeNicknameCount;
        private long vipExpireDate;
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

        public long getVipExpireDate() {
            return vipExpireDate;
        }

        public void setVipExpireDate(long vipExpireDate) {
            this.vipExpireDate = vipExpireDate;
        }

        public int getSignCount() {
            return signCount;
        }

        public void setSignCount(int signCount) {
            this.signCount = signCount;
        }
    }
}
