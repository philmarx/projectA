package com.hzease.tomeet.data;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuq on 2017/4/6.
 */

public class UserOrderBean implements Serializable {

    @Override
    public String toString() {
        return "UserOrderBean{" +
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
                    "avatarList=" + avatarList +
                    ", avatarSignature='" + avatarSignature + '\'' +
                    ", imageSignatures=" + imageSignatures +
                    '}';
        }

        private List<String> avatarList;
        private String avatarSignature;
        private boolean gender;
        private boolean isVip;
        private String nickname;
        private Map<String, String> imageSignatures;
        private long id;
        private List<OrdersBean> orders;
        private List<String> labels;
        private String birthday;
        private List<CirclesBean> circles;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public List<CirclesBean> getCircles() {
            return circles;
        }

        public void setCircles(List<CirclesBean> circles) {
            this.circles = circles;
        }

        public boolean isVip() {
            return isVip;
        }

        public void setVip(boolean vip) {
            isVip = vip;
        }

        public List<String> getAvatarList() {
            avatarList = new ArrayList<>(imageSignatures.values());
            avatarList.add(0, avatarSignature);
            Iterator iter = avatarList.iterator();
            while (iter.hasNext()){
                if (iter.next().equals("")) {
                    iter.remove();
                }
            }
            Logger.e(avatarList.toString());
            return avatarList;
        }

        public void setAvatarList(List<String> avatarList) {
            this.avatarList = avatarList;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Map<String, String> getImageSignatures() {
            return imageSignatures;
        }

        public void setImageSignatures(Map<String, String> imageSignatures) {
            this.imageSignatures = imageSignatures;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public Map<String, String> removeNullValue() {
            imageSignatures.put("avatarSignature", avatarSignature);
            Set<Map.Entry<String, String>> entries = new HashSet<>(imageSignatures.entrySet());
            for (Map.Entry<String, String> entry : entries) {
                if (TextUtils.isEmpty(entry.getValue())) {
                    imageSignatures.remove(entry.getKey());
                }
            }
            return imageSignatures;
        }

        public static class CirclesBean {
            /**
             * id : 100000000000
             * name : 张国文的圈子一
             * experience : 572
             */

            private long id;
            private String name;
            private int experience;

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

            public int getExperience() {
                return experience;
            }

            public void setExperience(int experience) {
                this.experience = experience;
            }
        }

        public static class OrdersBean {
            /**
             * point : 42153
             * ranking : 4
             * gameId : 10
             * gameName : 象棋
             * count : 1
             */

            private int point;
            private int ranking;
            private int gameId;
            private String gameName;
            private int count;

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

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
    }
}
