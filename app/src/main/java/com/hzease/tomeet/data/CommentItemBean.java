package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/4/28.
 */

public class CommentItemBean {

    @Override
    public String toString() {
        return "CommentItemBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * msg :
     * data : [{"id":2,"declareNickname":"张国文","content":"我发布了一个喊话ok覅就覅噢飞机哦是佛萨芬","evaluations":[{"id":2,"sender":{"id":10000000025,"nickname":"马健原"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建元的评论"},{"id":3,"sender":{"id":10000000025,"nickname":"马健原"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建元的评论22"},{"id":5,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建院极好"},{"id":6,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"几号回不是"},{"id":8,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建你好啊"},{"id":1,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"我是一个评论"},{"id":4,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"我是第二个评论"},{"id":7,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"我是帅哥"}],"declareId":10000000023,"avatarSignature":"1493342425239","createTime":1493351709000},{"id":3,"declareNickname":"张国文","content":"我是个帅哥","evaluations":[],"declareId":10000000023,"avatarSignature":"1493342425239","createTime":1493191807000}]
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", declareNickname='" + declareNickname + '\'' +
                    ", content='" + content + '\'' +
                    ", declareId=" + declareId +
                    ", avatarSignature='" + avatarSignature + '\'' +
                    ", createTime=" + createTime +
                    ", evaluations=" + evaluations +
                    '}';
        }

        /**
         * id : 2
         * declareNickname : 张国文
         * content : 我发布了一个喊话ok覅就覅噢飞机哦是佛萨芬
         * evaluations : [{"id":2,"sender":{"id":10000000025,"nickname":"马健原"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建元的评论"},{"id":3,"sender":{"id":10000000025,"nickname":"马健原"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建元的评论22"},{"id":5,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建院极好"},{"id":6,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"几号回不是"},{"id":8,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"马建你好啊"},{"id":1,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"我是一个评论"},{"id":4,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"我是第二个评论"},{"id":7,"sender":{"id":10000000023,"nickname":"张国文"},"receiver":{"id":10000000025,"nickname":"马健原"},"content":"我是帅哥"}]
         * declareId : 10000000023
         * avatarSignature : 1493342425239
         * createTime : 1493351709000
         */

        private long id;
        private String declareNickname;
        private String content;
        private long declareId;
        private String avatarSignature;
        private long createTime;
        private List<EvaluationsBean> evaluations;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getDeclareNickname() {
            return declareNickname;
        }

        public void setDeclareNickname(String declareNickname) {
            this.declareNickname = declareNickname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getDeclareId() {
            return declareId;
        }

        public void setDeclareId(long declareId) {
            this.declareId = declareId;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public List<EvaluationsBean> getEvaluations() {
            return evaluations;
        }

        public void setEvaluations(List<EvaluationsBean> evaluations) {
            this.evaluations = evaluations;
        }

        public static class EvaluationsBean {

            @Override
            public String toString() {
                return "EvaluationsBean{" +
                        "id=" + id +
                        ", sender=" + sender +
                        ", receiver=" + receiver +
                        ", content='" + content + '\'' +
                        '}';
            }

            /**
             * id : 2
             * sender : {"id":10000000025,"nickname":"马健原"}
             * receiver : {"id":10000000025,"nickname":"马健原"}
             * content : 马建元的评论
             */

            private long id;
            private SenderBean sender;
            private ReceiverBean receiver;
            private String content;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public SenderBean getSender() {
                return sender;
            }

            public void setSender(SenderBean sender) {
                this.sender = sender;
            }

            public ReceiverBean getReceiver() {
                return receiver;
            }

            public void setReceiver(ReceiverBean receiver) {
                this.receiver = receiver;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public static class SenderBean {
                /**
                 * id : 10000000025
                 * nickname : 马健原
                 */

                private long id;
                private String nickname;

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
            }

            public static class ReceiverBean {
                /**
                 * id : 10000000025
                 * nickname : 马健原
                 */

                private long id;
                private String nickname;

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
            }
        }
    }
}
