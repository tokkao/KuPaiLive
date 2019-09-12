package com.benben.kupaizhibo.bean.socket;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.kupaizhibo.KuPaiLiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能:发送消息数据对象的封装
 * create by zjn on 2019/5/16 0016
 * email:168455992@qq.com
 */
public class SendMessageUtils {

    /**
     * 功能:封装发送聊天信息包
     * @param chatContent
     * @return
     */
    public static String getSendChatMsgBean(String chatContent) {
        String json = "";
        SocketResponseHeaderBean headerBean = new SocketResponseHeaderBean();
        headerBean.setRetmsg("ok");
        headerBean.setRetcode("000000");
        List<SocketResponseBodyBean> list = new ArrayList<>();
        SocketResponseBodyBean bodyBean = new SocketResponseBodyBean();
        bodyBean.set_method_("SendMsg");
        bodyBean.setAction("0");
        bodyBean.setCt(chatContent);
        bodyBean.setMsgtype("2");
        bodyBean.setUid(KuPaiLiveApplication.mPreferenceProvider.getUId());
        bodyBean.setUname(KuPaiLiveApplication.mPreferenceProvider.getUserName());
        bodyBean.setLevel("10");
        bodyBean.setUser_level(KuPaiLiveApplication.mPreferenceProvider.getUserLevel());
        list.add(bodyBean);
        headerBean.setMsg(list);
        json = JSONUtils.toJsonString(headerBean);
        return json;
    }


    /**
     * 功能:封装发送礼物信息包
     * @return
     */
    public static String getSendGiftBean(String token) {
        String json = "";
        SocketResponseHeaderBean headerBean = new SocketResponseHeaderBean();
        headerBean.setRetmsg("ok");
        headerBean.setRetcode("000000");
        List<SocketResponseBodyBean> list = new ArrayList<>();
        SocketResponseBodyBean bodyBean = new SocketResponseBodyBean();
        bodyBean.set_method_("SendGift");
        bodyBean.setAction("0");
        bodyBean.setUser_level(KuPaiLiveApplication.mPreferenceProvider.getUserLevel());
       /* CTMessageBean ctMessageBean = new CTMessageBean();
        ctMessageBean.setGift_id(giftId);
        ctMessageBean.setGift_name(giftName);
        ctMessageBean.setGift_thumb(giftThumb);
        ctMessageBean.setNumber(number);*/
        bodyBean.setCt(token);
        bodyBean.setMsgtype("1");
        bodyBean.setUid(KuPaiLiveApplication.mPreferenceProvider.getUId());
        bodyBean.setUname(KuPaiLiveApplication.mPreferenceProvider.getUserName());
        bodyBean.setUhead(KuPaiLiveApplication.mPreferenceProvider.getPhoto());
        bodyBean.setLevel("10");
        list.add(bodyBean);
        headerBean.setMsg(list);
        json = JSONUtils.toJsonString(headerBean);
        return json;
    }

    /**
     * 功能:发送红包信息封包
     * @param token 红包token
     * @return
     */
    public static String getSendRedEnvelopesBean(String token) {
        String json = "";
        SocketResponseHeaderBean headerBean = new SocketResponseHeaderBean();
        headerBean.setRetmsg("ok");
        headerBean.setRetcode("000000");
        List<SocketResponseBodyBean> list = new ArrayList<>();
        SocketResponseBodyBean bodyBean = new SocketResponseBodyBean();
        bodyBean.set_method_("SendRedEnvelopes");
        bodyBean.setAction("80");
 /*       CTMessageBean ctMessageBean = new CTMessageBean();
        ctMessageBean.setRed_envelopes_id(redId);
        ctMessageBean.setMoney(money);
        ctMessageBean.setNumber(number);*/
        bodyBean.setCt(token);
        bodyBean.setMsgtype("1");
        bodyBean.setUid(KuPaiLiveApplication.mPreferenceProvider.getUId());
        bodyBean.setUname(KuPaiLiveApplication.mPreferenceProvider.getUserName());
        bodyBean.setUhead(KuPaiLiveApplication.mPreferenceProvider.getPhoto());
        bodyBean.setLevel("10");
        list.add(bodyBean);
        headerBean.setMsg(list);
        json = JSONUtils.toJsonString(headerBean);
        return json;
    }

    /**
     * 功能:抢红包信息结果封包
     * @param message 消息内容 抢了您的红包1星币
     * @return
     */
    public static String getSendRedEnvelopesResultBean(String message) {
        String json = "";
        SocketResponseHeaderBean headerBean = new SocketResponseHeaderBean();
        headerBean.setRetmsg("ok");
        headerBean.setRetcode("000000");
        List<SocketResponseBodyBean> list = new ArrayList<>();
        SocketResponseBodyBean bodyBean = new SocketResponseBodyBean();
        bodyBean.set_method_("SendRedEnvelopes");
        bodyBean.setAction("81");
        bodyBean.setCt(message);
        bodyBean.setMsgtype("2");
        bodyBean.setUid(KuPaiLiveApplication.mPreferenceProvider.getUId());
        bodyBean.setUname(KuPaiLiveApplication.mPreferenceProvider.getUserName());
        bodyBean.setUhead(KuPaiLiveApplication.mPreferenceProvider.getPhoto());
        bodyBean.setLevel("10");
        bodyBean.setTimestamp(String.valueOf(System.currentTimeMillis()));
        list.add(bodyBean);
        headerBean.setMsg(list);
        json = JSONUtils.toJsonString(headerBean);
        return json;
    }



    /**
     * 功能:下播
     * @return
     */
    public static String getCloseLiveMsgBean() {
        String json = "";
        SocketResponseHeaderBean headerBean = new SocketResponseHeaderBean();
        headerBean.setRetmsg("ok");
        headerBean.setRetcode("000000");
        List<SocketResponseBodyBean> list = new ArrayList<>();
        SocketResponseBodyBean bodyBean = new SocketResponseBodyBean();
        bodyBean.set_method_("CloseVideo");
        bodyBean.setAction("18");
        bodyBean.setMsgtype("1");
        bodyBean.setCt("直播关闭");
        bodyBean.setUid(KuPaiLiveApplication.mPreferenceProvider.getUId());
        bodyBean.setUname(KuPaiLiveApplication.mPreferenceProvider.getUserName());
        bodyBean.setLevel("10");
        list.add(bodyBean);
        headerBean.setMsg(list);
        json = JSONUtils.toJsonString(headerBean);
        return json;
    }

    /**
     * 发送红心，点亮
     * @return
     */
    public static String getSendHeartBean() {
        String json = "";
        SocketResponseHeaderBean headerBean = new SocketResponseHeaderBean();
        headerBean.setRetmsg("ok");
        headerBean.setRetcode("000000");
        List<SocketResponseBodyBean> list = new ArrayList<>();
        SocketResponseBodyBean bodyBean = new SocketResponseBodyBean();
        bodyBean.set_method_("light");
        bodyBean.setAction("2");
        bodyBean.setMsgtype("0");
        list.add(bodyBean);
        headerBean.setMsg(list);
        json = JSONUtils.toJsonString(headerBean);
        return json;
    }

}
