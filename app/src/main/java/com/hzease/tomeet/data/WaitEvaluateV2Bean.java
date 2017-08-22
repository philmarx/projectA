package com.hzease.tomeet.data;

import com.google.gson.annotations.Expose;

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
                    ", users=" + users +
                    '}';
        }

        private RoomBean room;
        private List<EvaluationsBean> users;

        public RoomBean getRoom() {
            return room;
        }

        public void setRoom(RoomBean room) {
            this.room = room;
        }

        public List<EvaluationsBean> getUsers() {
            return users;
        }

        public void setUsers(List<EvaluationsBean> users) {
            this.users = users;
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
            private long friendId;

            @Expose
            private int friendPoint;

            @Expose
            private int roomEvaluationPoint = 5;

            @Expose
            private String label = "";

            private List<String> labels;

            private String nickname;
            private String avatarSignature;
            private boolean signed;


            public long getFriendId() {
                return friendId;
            }

            public void setFriendId(long friendId) {
                this.friendId = friendId;
            }

            public int getFriendPoint() {
                return friendPoint;
            }

            public void setFriendPoint(int friendPoint) {
                this.friendPoint = friendPoint;
            }

            public int getRoomEvaluationPoint() {
                return roomEvaluationPoint;
            }

            public void setRoomEvaluationPoint(int roomEvaluationPoint) {
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
