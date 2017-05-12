package com.hzease.tomeet.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Key on 2017/5/11 15:01
 * email: MrKey.K@gmail.com
 * description:
 */

public class EvaluteBean {

    /**
     * userId : 11111111111
     * token : sss
     * roomId : 1000111111111
     * evaluations : [{"friendId":10000000000,"friendPoint":10,"labels":["hello"],"roomEvaluationPoint":5},{"friendId":100000022222,"friendPoint":5,"roomEvaluationPoint":5}]
     */

    private String userId;
    private String token;
    private String roomId;
    private List<EvaluationsBean> evaluations = new ArrayList<>();


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<EvaluationsBean> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationsBean> evaluations) {
        this.evaluations = evaluations;
    }

    @Override
    public String toString() {
        return "EvaluteBean{" +
                "userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", roomId='" + roomId + '\'' +
                ", evaluations=" + evaluations +
                '}';
    }

    public static class EvaluationsBean {
        /**
         * friendId : 10000000000
         * friendPoint : 10
         * labels : ["hello"]
         * roomEvaluationPoint : 5
         */

        private String friendId;
        // 好感度
        private String friendPoint = "5";
        // 活动评分
        private String roomEvaluationPoint = "5";
        private List<String> labels = new ArrayList<>();

        public String getFriendId() {
            return friendId;
        }

        public void setFriendId(String friendId) {
            this.friendId = friendId;
        }

        public String getFriendPoint() {
            return friendPoint;
        }

        public void setFriendPoint(String friendPoint) {
            this.friendPoint = friendPoint;
        }

        public String getRoomEvaluationPoint() {
            return roomEvaluationPoint;
        }

        public void setRoomEvaluationPoint(String roomEvaluationPoint) {
            this.roomEvaluationPoint = roomEvaluationPoint;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        @Override
        public String toString() {
            return "EvaluationsBean{" +
                    "friendId='" + friendId + '\'' +
                    ", friendPoint='" + friendPoint + '\'' +
                    ", roomEvaluationPoint='" + roomEvaluationPoint + '\'' +
                    ", labels=" + labels +
                    '}';
        }
    }
}
