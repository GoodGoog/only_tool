package com.example.more.retrofit;

public class QqInfoResponse {


    /**
     * code : 200
     * msg : 获取成功
     * data : {"qq":"1987428088","name":"虫虫威武","email":"1987428088@qq.com","avatar":"https://q1.qlogo.cn/g?b=qq&nk=1987428088&s=100"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
         * qq : 1987428088
         * name : 虫虫威武
         * email : 1987428088@qq.com
         * avatar : https://q1.qlogo.cn/g?b=qq&nk=1987428088&s=100
         */

        private String qq;
        private String name;
        private String email;
        private String avatar;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
