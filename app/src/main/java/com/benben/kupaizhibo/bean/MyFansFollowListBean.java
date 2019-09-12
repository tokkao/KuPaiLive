package com.benben.kupaizhibo.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-07-20.
 * Describe: 关注/粉丝列表bean
 */
public class MyFansFollowListBean {


    /**
     * current_page : 1
     * per_page : 15
     * last_page : 1
     * total : 3
     * data : [{"nickname":"用户60215","id":219,"is_follow":0,"avatar":"http://bjzb.hncjne.com/static/admin/images/default_avatar.jpg"},{"nickname":"@小张","id":203,"is_follow":0,"avatar":"http://live.zzqcnz.com/uploads/user_avatar/20190606/140103689143.jpg"},{"nickname":"ZRJM522","id":140,"is_follow":0,"avatar":"http://live.zzqcnz.com/uploads/images/20190528/114556486116.jpg"}]
     */

    private int current_page;
    private int per_page;
    private int last_page;
    private int total;
    private List<DataBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * nickname : 用户60215
         * id : 219
         * is_follow : 0
         * avatar : http://bjzb.hncjne.com/static/admin/images/default_avatar.jpg
         */

        private String nickname;
        private String uid;
        private String avatar;
        private int is_follow;

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
