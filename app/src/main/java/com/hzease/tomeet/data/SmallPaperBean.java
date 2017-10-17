package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/13.
 */

public class SmallPaperBean {

    /**
     * success : true
     * msg :
     * data : [{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507788099000,"nickname":"唐僧洗头爱飘柔😎","id":574,"state":5,"content":"方法"},{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507788017000,"nickname":"唐僧洗头爱飘柔😎","id":572,"state":4,"content":"痘痘"},{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507787824000,"nickname":"唐僧洗头爱飘柔😎","id":570,"state":5,"content":"反复发"},{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507780686000,"nickname":"唐僧洗头爱飘柔😎","id":548,"state":2,"content":"学习学习"},{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507778234000,"nickname":"唐僧洗头爱飘柔😎","id":546,"state":2,"content":"哥哥哥哥"},{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507778212000,"nickname":"唐僧洗头爱飘柔😎","id":545,"state":0,"content":"呃呃呃呃"},{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507778108000,"nickname":"唐僧洗头爱飘柔😎","id":543,"state":2,"content":"收拾收拾"},{"avatarSignature":"1505721523650","senderId":10000000001,"createTime":1507778053000,"nickname":"唐僧洗头爱飘柔😎","id":542,"state":5,"content":"许许多多"},{"avatarSignature":"1502434206820","senderId":10000000002,"createTime":1507775488000,"nickname":"xml","id":540,"state":0,"content":"vhbb"},{"avatarSignature":"1502434206820","senderId":10000000002,"createTime":1507775451000,"nickname":"xml","id":539,"state":5,"content":"cggg"}]
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
         * avatarSignature : 1505721523650
         * senderId : 10000000001
         * createTime : 1507788099000
         * nickname : 唐僧洗头爱飘柔😎
         * id : 574
         * state : 5
         * content : 方法
         */

        private String avatarSignature;
        private long senderId;
        private long createTime;
        private String nickname;
        private int id;
        private int state;
        private String content;

        public long getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(long receiverId) {
            this.receiverId = receiverId;
        }

        private long receiverId;

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public long getSenderId() {
            return senderId;
        }

        public void setSenderId(long senderId) {
            this.senderId = senderId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
