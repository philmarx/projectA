package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/3/7 14:01
 * email: MrKey.K@gmail.com
 * description: OSS的Key,Secret,Token
 */

public class OssInfoBean {

    /**
     * success : true
     * msg : null
     * data : {"securityToken":"CAIS0AN1q6Ft5B2yfSjIq67eHczxtYd10PvedX/FgHkXT+dBmILyiTz2IHlIfHltCOAWsv8+lWBT5/celrh+W4NIX0rNaY5t9ZlN9wqkbtJ5ZH9hL/9W5qe+EE2/VjQdtq27OpflLr70fvOqdCqL9Etayqf7cjOPRkGsNYbz57dsctUQWHvfD19BH8wECQZ+j8UYOHDNT9zPVCTnmW3NFkFllxNhgGdkk8SFz9ab9wDVgS+0qKMcrJ+jJYO/PYs+Zc0nC4nlg7QtJ/Gain4LtUpS7qB0gadY5XW1RLj/bnBV5xKZSbu2lvRkMA5+YIUjBqdAt4KSvPZku+vV5c2VrhFWJrNtTjj4ToKty9emYOSyLYQVeLL2J3nq2NKCPYWPlWFCW38AMx5QcNcMM2J5DQdWKgvXMai64lvHEF3BE6GOy/MxyoEnjQexp4LSeQjQG+7Iin1CasRiPVsyMBwb1nD9NbIdfhYLIwM3V+bFD6cLNUoG8f244lWJB3Y7lCEM4ceTPa2G5votDqzkRY9D3IYnY5BLjnAnVVyfScj10xtMJDE0EeYKi/i8acTuueOfs+yXYP/bDPccoUldcj3XoCeLRGlZMSrr/NonZEoUEHFab0Q1mRqAAT5wGlxkCVuDLiK4soa74c2Kodrx2XCxlR/ijQoRERBqc21XTOFpqKk2BkkAWFBYCTrJBOc91rPdEKpVEK8A49o2heXOPKC0N1wA+aYHWV9joscoxK7L+3++jRAMJjHS30SNsK6RtzXNi6xqNf42CBW+ekyfUs3OOn9VPoVnj6mR","accessKeySecret":"J6eekALCWzwPSKbmwx9XyKCWHHe3h4XRAK9vMpCZ4YyH","accessKeyId":"STS.JmkVvEXXTg95wYtdyBChmwGYk","expiration":"2017-03-07T07:06:06Z"}
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

    public Object getMsg() {
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
         * securityToken : CAIS0AN1q6Ft5B2yfSjIq67eHczxtYd10PvedX/FgHkXT+dBmILyiTz2IHlIfHltCOAWsv8+lWBT5/celrh+W4NIX0rNaY5t9ZlN9wqkbtJ5ZH9hL/9W5qe+EE2/VjQdtq27OpflLr70fvOqdCqL9Etayqf7cjOPRkGsNYbz57dsctUQWHvfD19BH8wECQZ+j8UYOHDNT9zPVCTnmW3NFkFllxNhgGdkk8SFz9ab9wDVgS+0qKMcrJ+jJYO/PYs+Zc0nC4nlg7QtJ/Gain4LtUpS7qB0gadY5XW1RLj/bnBV5xKZSbu2lvRkMA5+YIUjBqdAt4KSvPZku+vV5c2VrhFWJrNtTjj4ToKty9emYOSyLYQVeLL2J3nq2NKCPYWPlWFCW38AMx5QcNcMM2J5DQdWKgvXMai64lvHEF3BE6GOy/MxyoEnjQexp4LSeQjQG+7Iin1CasRiPVsyMBwb1nD9NbIdfhYLIwM3V+bFD6cLNUoG8f244lWJB3Y7lCEM4ceTPa2G5votDqzkRY9D3IYnY5BLjnAnVVyfScj10xtMJDE0EeYKi/i8acTuueOfs+yXYP/bDPccoUldcj3XoCeLRGlZMSrr/NonZEoUEHFab0Q1mRqAAT5wGlxkCVuDLiK4soa74c2Kodrx2XCxlR/ijQoRERBqc21XTOFpqKk2BkkAWFBYCTrJBOc91rPdEKpVEK8A49o2heXOPKC0N1wA+aYHWV9joscoxK7L+3++jRAMJjHS30SNsK6RtzXNi6xqNf42CBW+ekyfUs3OOn9VPoVnj6mR
         * accessKeySecret : J6eekALCWzwPSKbmwx9XyKCWHHe3h4XRAK9vMpCZ4YyH
         * accessKeyId : STS.JmkVvEXXTg95wYtdyBChmwGYk
         * expiration : 2017-03-07T07:06:06Z
         */

        private String securityToken;
        private String accessKeySecret;
        private String accessKeyId;
        private String expiration;

        public String getSecurityToken() {
            return securityToken;
        }

        public void setSecurityToken(String securityToken) {
            this.securityToken = securityToken;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "securityToken='" + securityToken + '\'' +
                    ", accessKeySecret='" + accessKeySecret + '\'' +
                    ", accessKeyId='" + accessKeyId + '\'' +
                    ", expiration='" + expiration + '\'' +
                    '}';
        }
    }
}
