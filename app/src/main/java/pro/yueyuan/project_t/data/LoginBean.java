package pro.yueyuan.project_t.data;

/**
 * Created by xuq on 2017/3/22.
 */

public class LoginBean {
    /**
     * success : true
     * msg : 注册成功
     * data : {"id":10000000013,"isInit":false,"token":"LUmBDofE+xnttWYJIsR0dRK177rl3SZPaIa3/+JhJAq63fcWeQajIJFicNuUuOqfeQtaIM6mnzOYModBLY5O8A=="}
     */

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
        /**
         * id : 10000000013
         * isInit : false
         * token : LUmBDofE+xnttWYJIsR0dRK177rl3SZPaIa3/+JhJAq63fcWeQajIJFicNuUuOqfeQtaIM6mnzOYModBLY5O8A==
         */

        private String id;
        private boolean isInit;
        private String token;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsInit() {
            return isInit;
        }

        public void setIsInit(boolean isInit) {
            this.isInit = isInit;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
