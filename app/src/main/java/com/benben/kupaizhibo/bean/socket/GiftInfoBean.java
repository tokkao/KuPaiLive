package com.benben.kupaizhibo.bean.socket;

/**
 * 单个礼物信息
 * create by zjn on 2019/5/17 0017
 * email:168455992@qq.com
 */
public class GiftInfoBean {

    /**
     * id : 21
     * model : 0
     * name : 小绵羊
     * icon : http://192.168.2.134:105/uploads/images/20190515/1564dfe581e83457b317ed623a128e12.png
     * cid : 1
     * money : 2.00
     * zip_name :
     * zip_addr :
     * gif_addr :
     */

    private String id;
    private String model;
    private String name;
    private String icon;
    private String cid;
    private String money;
    private String zip_name;
    private String zip_addr;
    private String gif_addr;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getZip_name() {
        return zip_name;
    }

    public void setZip_name(String zip_name) {
        this.zip_name = zip_name;
    }

    public String getZip_addr() {
        return zip_addr;
    }

    public void setZip_addr(String zip_addr) {
        this.zip_addr = zip_addr;
    }

    public String getGif_addr() {
        return gif_addr;
    }

    public void setGif_addr(String gif_addr) {
        this.gif_addr = gif_addr;
    }


    @Override
    public String toString() {
        return "GiftInfoBean{" +
                "id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", cid='" + cid + '\'' +
                ", money='" + money + '\'' +
                ", zip_name='" + zip_name + '\'' +
                ", zip_addr='" + zip_addr + '\'' +
                ", gif_addr='" + gif_addr + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
