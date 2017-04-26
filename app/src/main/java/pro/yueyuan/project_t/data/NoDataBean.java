package pro.yueyuan.project_t.data;

/**
 * Created by Key on 2017/4/26 13:44
 * email: MrKey.K@gmail.com
 * description: 适用于任何没有data返回值的接口
 */

public class NoDataBean {

    @Override
    public String toString() {
        return "NoDataBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * msg : "空空空空空如也"
     * data : null
     */

    private boolean success;
    private String msg;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
