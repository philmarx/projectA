package com.hzease.tomeet.data;

import com.google.gson.annotations.Expose;
import com.hzease.tomeet.PTApplication;

import java.util.List;

/**
 * Created by Key on 2017/8/21 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class WaitEvaluateV2Bean {

    @Override
    public String toString() {
        return "WaitEvaluateV2Bean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

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
                    "room=" + room +
                    ", users=" + evaluations +
                    '}';
        }

        @Expose
        private List<EvaluationsBean> evaluations;
        @Expose
        private String userId = PTApplication.userId;
        @Expose
        private String token = PTApplication.userToken;
        @Expose
        private String roomId;


        public List<EvaluationsBean> getEvaluations() {
            return evaluations;
        }

        public void setEvaluations(List<EvaluationsBean> evaluations) {
            this.evaluations = evaluations;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        private RoomBean room;

        public RoomBean getRoom() {
            return room;
        }

        public void setRoom(RoomBean room) {
            this.room = room;
        }

        public static class RoomBean {

            @Override
            public String toString() {
                return "RoomBean{" +
                        "isScoring=" + isScoring +
                        '}';
            }

            private boolean isScoring;

            public boolean isIsScoring() {
                return isScoring;
            }

            public void setIsScoring(boolean isScoring) {
                this.isScoring = isScoring;
            }
        }

        public static class EvaluationsBean {
            @Override
            public String toString() {
                return "EvaluationsBean{" +
                        "friendId=" + friendId +
                        ", friendPoint=" + friendPoint +
                        ", roomEvaluationPoint='" + roomEvaluationPoint + '\'' +
                        ", label='" + label + '\'' +
                        ", labels=" + labels +
                        ", nickname='" + nickname + '\'' +
                        ", avatarSignature='" + avatarSignature + '\'' +
                        ", signed=" + signed +
                        '}';
            }

            @Expose
            private String friendId;

            @Expose
            private String friendPoint;

            @Expose
            private String roomEvaluationPoint = "5";

            @Expose
            private String label = "";

            private List<String> labels;

            private String nickname;
            private String avatarSignature;
            private boolean signed;

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

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(String avatarSignature) {
                this.avatarSignature = avatarSignature;
            }

            public boolean isSigned() {
                return signed;
            }

            public void setSigned(boolean signed) {
                this.signed = signed;
            }

            public List<String> getLabels() {
                return labels;
            }

            public void setLabels(List<String> labels) {
                this.labels = labels;
            }
        }
    }
}
