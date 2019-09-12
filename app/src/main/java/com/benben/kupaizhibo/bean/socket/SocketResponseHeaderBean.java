package com.benben.kupaizhibo.bean.socket;

import java.io.Serializable;
import java.util.List;

/**
 * 功能:socket 响应请求头
 * create by zjn on 2019/5/16 0016
 * email:168455992@qq.com
 */
public class SocketResponseHeaderBean implements Serializable {
    private String retcode;
    private String retmsg;
    private List<SocketResponseBodyBean> msg;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public List<SocketResponseBodyBean> getMsg() {
        return msg;
    }

    public void setMsg(List<SocketResponseBodyBean> msg) {
        this.msg = msg;
    }
}
