package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/23.
 */

public class HavaBZmoneyRoomBean {


    /**
     * success : true
     * msg :
     * data : [{"gameId":5,"money":2000,"id":1855,"time":"2017-07-14 09:00","state":0,"title":"～(￣▽￣～)~周五签到"},{"gameId":5,"money":2000,"id":1854,"time":"2017-07-13 09:00","state":0,"title":"周四了happy吗？"},{"gameId":5,"money":2000,"id":1853,"time":"2017-07-12 09:00","state":0,"title":"周三签到"},{"gameId":5,"money":2000,"id":1852,"time":"2017-07-11 09:00","state":0,"title":"周二来签到"}]
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
         * gameId : 5
         * money : 2000
         * id : 1855
         * time : 2017-07-14 09:00
         * state : 0
         * title : ～(￣▽￣～)~周五签到
         */

        private int gameId;
        private int money;
        private int id;
        private String time;
        private int state;
        private String title;

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
