package pro.yueyuan.project_t.data;

/**
 * Created by Key on 2017/2/28 20:39
 * email: MrKey.K@gmail.com
 * description:
 */

public class SMSPhoneNumberBean {

    public SMSPhoneNumberBean(String mobile) {
        this.mobile = mobile;
        this.temp_id = 1;
    }

    /**
     * mobile : 18698569593
     * temp_id : 1
     */


    private String mobile;
    private int temp_id;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getTemp_id() {
        return temp_id;
    }

    public void setTemp_id(int temp_id) {
        this.temp_id = temp_id;
    }
}
