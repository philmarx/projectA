package pro.yueyuan.project_t.data;

import java.util.List;

/**
 * Created by Key on 2017/3/24 17:01
 * email: MrKey.K@gmail.com
 * description:
 */

public class FriendListBean {

    /**
     * success : true
     * msg : 请先登录
     * data : [{"nickname":"test12","id":10000000009,"point":5},{"nickname":"test56","id":10000000008,"point":4},{"nickname":"test2","id":10000000007,"point":3},{"nickname":"test5","id":10000000001,"point":1}]
     */

    private boolean success;
    private String msg;
    private List<ConversationListBean.FriendBean> data;

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

    public List<ConversationListBean.FriendBean> getData() {
        return data;
    }

    public void setData(List<ConversationListBean.FriendBean> data) {
        this.data = data;
    }

}
