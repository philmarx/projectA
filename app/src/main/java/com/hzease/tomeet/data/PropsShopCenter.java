package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/10/19.
 */

public class PropsShopCenter {
    /**
     * success : true
     * msg : 1
     * data : [{"unit":"叶子","originalMoney":-1,"uniqueId":0,"name":"note","title":"小纸条","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/note.png","description":"","badge":5,"money":-1,"discountType":"无","show":true},{"unit":"叶子","originalMoney":-1,"uniqueId":1,"name":"clearLabelCard","title":"标签消除卡","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/clearLable.png","description":"","badge":5,"money":-1,"discountType":"无","show":true},{"unit":"叶子","originalMoney":-1,"uniqueId":2,"name":"changeNameCard","title":"改名卡","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/changeName.png","description":"","badge":60,"money":-1,"discountType":"无","show":true},{"unit":"叶子","originalMoney":-1,"uniqueId":3,"name":"signAgainCard","title":"补签卡","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/signAgain.png","description":"","badge":5,"money":-1,"discountType":"无","show":true},{"unit":"人民币","originalMoney":1800,"uniqueId":4,"name":"vip1","title":"一个月会员","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/vip.png","description":"","badge":-1,"money":1800,"discountType":"hot","show":true},{"unit":"人民币","originalMoney":5400,"uniqueId":5,"name":"vip3","title":"三个月会员","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/vip.png","description":"","badge":-1,"money":5000,"discountType":"time","show":true},{"unit":"人民币","originalMoney":21600,"uniqueId":6,"name":"vip12","title":"一年会员","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/vip.png","description":"","badge":-1,"money":18800,"discountType":"time","show":true},{"unit":"叶子","originalMoney":0,"uniqueId":7,"name":"friendCard","title":"同伴卡","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/friend.png","description":"","badge":50,"money":-1,"discountType":"无","show":true},{"unit":"无","originalMoney":0,"uniqueId":8,"name":"movieTicket","title":"观影券","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/movieTicket.png","description":"","badge":50,"money":-1,"discountType":"无","show":true},{"unit":"无","originalMoney":0,"uniqueId":9,"name":"roomTicket","title":"入场券","photoUrl":"https://ease.oss-cn-hangzhou.aliyuncs.com/prop/roomTicket.png","description":"入场券","badge":0,"money":-1,"discountType":"无","show":false}]
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
         * unit : 叶子
         * originalMoney : -1
         * uniqueId : 0
         * name : note
         * title : 小纸条
         * photoUrl : https://ease.oss-cn-hangzhou.aliyuncs.com/prop/note.png
         * description :
         * badge : 5
         * money : -1
         * discountType : 无
         * show : true
         */

        private String unit;
        private int originalMoney;
        private int uniqueId;
        private String name;
        private String title;
        private String photoUrl;
        private String description;
        private int badge;
        private int money;
        private String discountType;
        private boolean show;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getOriginalMoney() {
            return originalMoney;
        }

        public void setOriginalMoney(int originalMoney) {
            this.originalMoney = originalMoney;
        }

        public int getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(int uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getDiscountType() {
            return discountType;
        }

        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }
    }
}
