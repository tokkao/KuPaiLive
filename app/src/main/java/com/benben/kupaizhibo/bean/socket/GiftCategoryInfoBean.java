package com.benben.kupaizhibo.bean.socket;

import java.util.List;

/**
 * 礼物类别信息对象
 * create by zjn on 2019/5/17 0017
 * email:168455992@qq.com
 */
public class GiftCategoryInfoBean {
    private String name;
    private String id;
    private List<GiftInfoBean> gift;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GiftInfoBean> getGift() {
        return gift;
    }

    public void setGift(List<GiftInfoBean> gift) {
        this.gift = gift;
    }
}
