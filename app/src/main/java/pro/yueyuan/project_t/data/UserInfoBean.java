package pro.yueyuan.project_t.data;

/**
 * Created by xuq on 2017/3/3.
 */

public class UserInfoBean {

    /**
     * success : true
     * msg : 登录成功
     * data : td2zVCXLpu9gl/YdVsLRQ8xD5FGrofxyCE5yNilJQw89VqDq3XvQuaxrVsZdDNaaRgnUJyHYkC6YiMpdKifck6nht9/FV9TN
     */

    private boolean success;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
