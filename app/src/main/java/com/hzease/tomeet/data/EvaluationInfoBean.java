package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/9/18.
 */

public class EvaluationInfoBean {

    /**
     * success : true
     * msg :
     * data : [{"id":287,"content":"看看","createTime":1505456135000,"senderId":10000000003,"senderName":"西瓜🔥","senderAvatarSignature":"1504834132891","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"许愿氤氲之息","declarationId":213},{"id":266,"content":"奋斗到底","createTime":1505208862000,"senderId":10000000001,"senderName":"唐僧洗头爱飘柔😎","senderAvatarSignature":"1505721523650","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"今天几号啊","declarationId":210},{"id":265,"content":"回复你了","createTime":1505208837000,"senderId":10000000001,"senderName":"唐僧洗头爱飘柔😎","senderAvatarSignature":"1505721523650","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"今天几号啊","declarationId":210},{"id":257,"content":"我觉得毒奶粉才是我的归宿","createTime":1504797463000,"senderId":10000000003,"senderName":"西瓜🔥","senderAvatarSignature":"1504834132891","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"许愿氤氲之息","declarationId":213},{"id":255,"content":"周末一起玩王者哦哦哦","createTime":1504776232000,"senderId":10000000001,"senderName":"唐僧洗头爱飘柔😎","senderAvatarSignature":"1505721523650","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"许愿氤氲之息","declarationId":213},{"id":254,"content":"哈哈","createTime":1504776199000,"senderId":10000000001,"senderName":"唐僧洗头爱飘柔😎","senderAvatarSignature":"1505721523650","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"许愿氤氲之息","declarationId":213},{"id":253,"content":"嘿嘿","createTime":1504751630000,"senderId":10000000003,"senderName":"西瓜🔥","senderAvatarSignature":"1504834132891","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"许愿氤氲之息","declarationId":213},{"id":246,"content":"💩","createTime":1504493628000,"senderId":10000002076,"senderName":"rainco","senderAvatarSignature":"1504492957456","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"守望先锋 lol 毒奶粉的来哈哈哈","declarationId":184},{"id":245,"content":"💩","createTime":1504493620000,"senderId":10000002076,"senderName":"rainco","senderAvatarSignature":"1504492957456","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"守望先锋 lol 毒奶粉的来哈哈哈","declarationId":184},{"id":212,"content":"嘿嘿","createTime":1503300572000,"senderId":10000000003,"senderName":"西瓜🔥","senderAvatarSignature":"1504834132891","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"晚上有没有要一起去浙大跑步的呢","declarationId":172},{"id":211,"content":"紫金港校区","createTime":1503299415000,"senderId":10000000003,"senderName":"西瓜🔥","senderAvatarSignature":"1504834132891","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"晚上有没有要一起去浙大跑步的呢","declarationId":172},{"id":210,"content":"跑步锻炼身体","createTime":1503299395000,"senderId":10000000003,"senderName":"西瓜🔥","senderAvatarSignature":"1504834132891","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"晚上有没有要一起去浙大跑步的呢","declarationId":172},{"id":206,"content":"哪个校区","createTime":1503144917000,"senderId":10000001587,"senderName":"mengyuan","senderAvatarSignature":"1503144828915","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"晚上有没有要一起去浙大跑步的呢","declarationId":172},{"id":191,"content":"哪个校区","createTime":1503047612000,"senderId":10000001016,"senderName":"胖又又","senderAvatarSignature":"1502546948814","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"晚上有没有要一起去浙大跑步的呢","declarationId":172},{"id":184,"content":"你怎么天天跑步","createTime":1503032295000,"senderId":10000000010,"senderName":"丸子","senderAvatarSignature":"1505223845460","declarerId":10000000003,"declarerName":"西瓜🔥","declarationContent":"晚上有没有要一起去浙大跑步的呢","declarationId":172}]
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
         * id : 287
         * content : 看看
         * createTime : 1505456135000
         * senderId : 10000000003
         * senderName : 西瓜🔥
         * senderAvatarSignature : 1504834132891
         * declarerId : 10000000003
         * declarerName : 西瓜🔥
         * declarationContent : 许愿氤氲之息
         * declarationId : 213
         */

        private int id;
        private String content;
        private long createTime;
        private long senderId;
        private String senderName;
        private String senderAvatarSignature;
        private long declarerId;
        private String declarerName;
        private String declarationContent;
        private int declarationId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getSenderId() {
            return senderId;
        }

        public void setSenderId(long senderId) {
            this.senderId = senderId;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getSenderAvatarSignature() {
            return senderAvatarSignature;
        }

        public void setSenderAvatarSignature(String senderAvatarSignature) {
            this.senderAvatarSignature = senderAvatarSignature;
        }

        public long getDeclarerId() {
            return declarerId;
        }

        public void setDeclarerId(long declarerId) {
            this.declarerId = declarerId;
        }

        public String getDeclarerName() {
            return declarerName;
        }

        public void setDeclarerName(String declarerName) {
            this.declarerName = declarerName;
        }

        public String getDeclarationContent() {
            return declarationContent;
        }

        public void setDeclarationContent(String declarationContent) {
            this.declarationContent = declarationContent;
        }

        public int getDeclarationId() {
            return declarationId;
        }

        public void setDeclarationId(int declarationId) {
            this.declarationId = declarationId;
        }
    }
}
