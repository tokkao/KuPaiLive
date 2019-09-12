package com.benben.kupaizhibo.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 观众列表bean
 */
public class ViewerListBean {


    /**
     * nums : 1
     * lists : [{"sign":"Z20190720160047375722","id":33,"userType":0,"avatar":"http://bjzb.hncjne.com/uploads/images/20190720/152938465690.jpg","user_nickname":"我裂开了","guard_type":0}]
     */

    private int nums;
    private List<ListsBean> lists;

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * sign : Z20190720160047375722
         * id : 33
         * userType : 0
         * avatar : http://bjzb.hncjne.com/uploads/images/20190720/152938465690.jpg
         * user_nickname : 我裂开了
         * guard_type : 0
         */

        private String sign;
        private int id;
        private int userType;
        private String avatar;
        private String user_nickname;
        private int guard_type;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public int getGuard_type() {
            return guard_type;
        }

        public void setGuard_type(int guard_type) {
            this.guard_type = guard_type;
        }
    }
}
