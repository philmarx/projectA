package com.hzease.tomeet.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Key on 2017/5/8 20:02
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomBean {

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

        private long id;
        private String name;
        private String place;
        private ManagerBean manager;
        private boolean open;
        private String beginTime;
        private String endTime;
        private String createTime;
        private int state;
        private boolean locked;
        private GameBean game;
        private int money;
        private int joinMember;
        private int joinManMember;
        private int joinWomanMember;
        private int memberCount;
        private int manCount;
        private int womanCount;
        private String description;
        private double longitude;
        private double latitude;
        private String prepareTime;
        private String city;
        private List<JoinMembersBean> joinMembers;

        public boolean isAnonymous() {
            return anonymous;
        }

        public void setAnonymous(boolean anonymous) {
            this.anonymous = anonymous;
        }

        private boolean anonymous;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public ManagerBean getManager() {
            return manager;
        }

        public void setManager(ManagerBean manager) {
            this.manager = manager;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public GameBean getGame() {
            return game;
        }

        public void setGame(GameBean game) {
            this.game = game;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getJoinMember() {
            return joinMember;
        }

        public void setJoinMember(int joinMember) {
            this.joinMember = joinMember;
        }

        public int getJoinManMember() {
            return joinManMember;
        }

        public void setJoinManMember(int joinManMember) {
            this.joinManMember = joinManMember;
        }

        public int getJoinWomanMember() {
            return joinWomanMember;
        }

        public void setJoinWomanMember(int joinWomanMember) {
            this.joinWomanMember = joinWomanMember;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public int getManCount() {
            return manCount;
        }

        public void setManCount(int manCount) {
            this.manCount = manCount;
        }

        public int getWomanCount() {
            return womanCount;
        }

        public void setWomanCount(int womanCount) {
            this.womanCount = womanCount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getPrepareTime() {
            return prepareTime;
        }

        public void setPrepareTime(String prepareTime) {
            this.prepareTime = prepareTime;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<JoinMembersBean> getJoinMembers() {
            return joinMembers;
        }

        public void setJoinMembers(List<JoinMembersBean> joinMembers) {
            this.joinMembers = joinMembers;
        }

        public static class ManagerBean {
            /**
             * id : 10000000020
             * nickname : 我才是徐强
             * avatarSignature : 1494207144605
             * labels : ["标签","hehe"]
             */

            private long id;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

        }

        public static class GameBean {
            /**
             * id : 22
             * name : 电影
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class JoinMembersBean {

            private long id;
            private String nickname;
            private boolean ready;
            private String avatarSignature;
            // 签到
            private boolean signed;
            // 是否在房间
            private boolean online;
            private boolean vip;
            // 出发
            private boolean attend;
            private boolean isChoose;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public boolean isReady() {
                return ready;
            }

            public void setReady(boolean ready) {
                this.ready = ready;
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

            public boolean isOnline() {
                return online;
            }

            public void setOnline(boolean online) {
                this.online = online;
            }

            public boolean isVip() {
                return vip;
            }

            public void setVip(boolean vip) {
                this.vip = vip;
            }

            public boolean isAttend() {
                return attend;
            }

            public void setAttend(boolean attend) {
                this.attend = attend;
            }

            public boolean isChoose() {
                return isChoose;
            }

            public void setChoose(boolean choose) {
                isChoose = choose;
            }

            @Override
            public String toString() {
                return "JoinMembersBean{" +
                        "id=" + id +
                        ", nickname='" + nickname + '\'' +
                        ", ready=" + ready +
                        ", avatarSignature='" + avatarSignature + '\'' +
                        ", signed=" + signed +
                        ", online=" + online +
                        ", vip=" + vip +
                        ", attend=" + attend +
                        ", isChoose=" + isChoose +
                        '}';
            }
        }
    }
}
