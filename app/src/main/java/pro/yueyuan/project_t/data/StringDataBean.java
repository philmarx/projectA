package pro.yueyuan.project_t.data;

/**
 * Created by Key on 2017/3/21 10:54
 * email: MrKey.K@gmail.com
 * description:
 */

public class StringDataBean {
    private boolean success;
    private String msg;
    private String data;

    public StringDataBean(boolean success, String msg, String data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public StringDataBean() {
    }

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
