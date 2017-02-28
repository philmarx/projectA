package pro.yueyuan.project_t.data;

/**
 * Created by Key on 2017/2/28 20:14
 * email: MrKey.K@gmail.com
 * description:
 */

public class SMSCodeBean {

    /**
     * msg_id : 06890980-6789-4054-bba9-90fb66ab2fce
     * error : {"code":50002,"message":"auth failed"}
     * is_valid : false
     */

    private String msg_id;
    private ErrorBean error;
    private boolean is_valid;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    public static class ErrorBean {
        /**
         * code : 50002
         * message : auth failed
         */

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "ErrorBean{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SMSCodeBean{" +
                "msg_id='" + msg_id + '\'' +
                ", error=" + error.toString() +
                ", is_valid=" + is_valid +
                '}';
    }
}
