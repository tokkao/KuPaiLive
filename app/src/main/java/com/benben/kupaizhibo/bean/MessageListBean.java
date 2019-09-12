package com.benben.kupaizhibo.bean;

/**
 * Author:zhn
 * Time:2019/5/13 0013 16:38
 *
 * 系统消息实体类
 *
 */
public class MessageListBean {
    private int id;//系统消息ID
    private int msgtype;//消息类型
    private int is_read;//是否已读
    private String event;//事件
    private String content;//消息内容
    private String create_time;//创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
