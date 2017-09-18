package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/9/18.
 */

public class EvaluationInfoBean {
    /**
     * success : true
     * msg :
     * data : [{"sender":{"avatarSignature":"1504834132891","nickname":"西瓜🔥","id":10000000003,"content":"看看"},"createTime":1505456135000,"declaration":{"avatarSignature":"1504834132891","nickname":"西瓜🔥","id":213,"content":"许愿氤氲之息"}},{"sender":{"avatarSignature":"1503632516374","nickname":"唐僧洗头爱飘柔😎","id":10000000001,"content":"哈哈"},"createTime":1504776199000,"declaration":{"avatarSignature":"1504834132891","nickname":"西瓜🔥","id":213,"content":"许愿氤氲之息"}},{"sender":{"avatarSignature":"1503288627005","nickname":"倩大大i","id":10000000064,"content":"你不是改玩DNF了吗！"},"createTime":1502671427000,"declaration":{"avatarSignature":"1502547016254","nickname":"好吃的五花肉","id":149,"content":"有人一起玩守望先锋吗？娱乐局😃\n"}},{"sender":{"avatarSignature":"1503632516374","nickname":"唐僧洗头爱飘柔😎","id":10000000001,"content":"打打杀杀"},"createTime":1501836533000,"declaration":{"avatarSignature":"1501153679772","nickname":"大美妞","id":109,"content":"找老公！"}},{"sender":{"avatarSignature":"1503632516374","nickname":"唐僧洗头爱飘柔😎","id":10000000001,"content":"个广告"},"createTime":1501834776000,"declaration":{"avatarSignature":"1504695287291","nickname":"普通村民","id":110,"content":"的确很久了"}},{"sender":{"avatarSignature":"1503632516374","nickname":"唐僧洗头爱飘柔😎","id":10000000001,"content":"你是谁"},"createTime":1501813639000,"declaration":{"avatarSignature":"1504695287291","nickname":"普通村民","id":110,"content":"的确很久了"}},{"sender":{"avatarSignature":"1504834132891","nickname":"西瓜🔥","id":10000000003,"content":"哦"},"createTime":1501746927000,"declaration":{"avatarSignature":"1504695287291","nickname":"普通村民","id":110,"content":"的确很久了"}},{"sender":{"avatarSignature":"1502450409071","nickname":"😶无敌太寂寞","id":10000000000,"content":"sex更好"},"createTime":1500020010000,"declaration":{"avatarSignature":"1504695287291","nickname":"普通村民","id":67,"content":"散财童子JQL 🤑"}},{"sender":{"avatarSignature":"1502450409071","nickname":"😶无敌太寂寞","id":10000000000,"content":"新娘不是你吗？😜😜😜"},"createTime":1499492259000,"declaration":{"avatarSignature":"1503632516374","nickname":"唐僧洗头爱飘柔😎","id":35,"content":"杭州举行婚礼"}},{"sender":{"avatarSignature":"1502450409071","nickname":"😶无敌太寂寞","id":10000000000,"content":"托儿茶"},"createTime":1499492205000,"declaration":{"avatarSignature":"1504695287291","nickname":"普通村民","id":40,"content":"要不要去买杯一点点呢"}}]
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
         * sender : {"avatarSignature":"1504834132891","nickname":"西瓜🔥","id":10000000003,"content":"看看"}
         * createTime : 1505456135000
         * declaration : {"avatarSignature":"1504834132891","nickname":"西瓜🔥","id":213,"content":"许愿氤氲之息"}
         */

        private SenderBean sender;
        private long createTime;
        private DeclarationBean declaration;

        public SenderBean getSender() {
            return sender;
        }

        public void setSender(SenderBean sender) {
            this.sender = sender;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public DeclarationBean getDeclaration() {
            return declaration;
        }

        public void setDeclaration(DeclarationBean declaration) {
            this.declaration = declaration;
        }

        public static class SenderBean {
            /**
             * avatarSignature : 1504834132891
             * nickname : 西瓜🔥
             * id : 10000000003
             * content : 看看
             */

            private String avatarSignature;
            private String nickname;
            private long id;
            private String content;

            public String getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(String avatarSignature) {
                this.avatarSignature = avatarSignature;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class DeclarationBean {
            /**
             * avatarSignature : 1504834132891
             * nickname : 西瓜🔥
             * id : 213
             * content : 许愿氤氲之息
             */

            private String avatarSignature;
            private String nickname;
            private int id;
            private String content;

            public String getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(String avatarSignature) {
                this.avatarSignature = avatarSignature;
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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
