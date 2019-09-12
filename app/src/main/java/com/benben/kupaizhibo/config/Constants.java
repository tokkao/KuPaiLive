package com.benben.kupaizhibo.config;

/**
 * 功能:配置的APPid
 */
public class Constants {
    public static final String APP_ID = "91940827";
    public static final String APP_SECRET = "PJCpBvxjHSNZckaKdBoPHxVpcXVQvfTz";

    //微信支付商户号
    //1546781451
    public static final String WX_APP_ID = "wxa84ed2dbcafda6cb";
    public static final String WX_SECRET = "42b3ef96fcec35123fe3588dddfdeb60";

    public static final String TEST_AVATAR = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2131765346,1808107930&fm=26&gp=0.jpg";
    public static final String TEST_COVER = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1616790526,3488652206&fm=26&gp=0.jpg";
    public static final String TEST_BANNER = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561985725062&di=dc2655ed56cb5977b2722ca7b955f3ec&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20120121%2FImg332833001.jpg";
    //图片后缀
    public static final String BITMAP_SUFFIX = ".kpzb";

    //支付方式
    public static final String EXTRA_KEY_PAYMENT_WAY = "payment_way";
    //订单编号
    public static final String EXTRA_KEY_ORDER_ID = "orderId";
    //支付金额
    public static final String EXTRA_KEY_PAYMENT_MONEY = "payment_money";
    //webview标题
    public static final String EXTRA_KEY_WEB_VIEW_TITLE = "webview_title";
    //是否是url 还是富文本
    public static final String EXTRA_KEY_IS_URL = "is_url";
    //Url
    public static final String EXTRA_KEY_WEB_VIEW_URL = "webview_url";
    //常用地址
    public static final String SP_KEY_USED_ADDRESS = "used_address";
    //第一次进入
    public static final String SP_KEY_IS_FIRST = "is_first_open_app";
    //是否刷新
    public static final String EXTRA_KEY_IS_REFRESH = "is_refresh";
    //用户id
    public static final String EXTRA_KEY_USER_ID = "user_id";
    //刷新个人中心
    public static final String RXBUS_KEY_REFRESH_MINEFRAGMENT = "refresh_minefragment";

    //request code
    public static final int BIND_MOBILE_REQUEST_CODE = 106;

    public static final int ADD_DIARY_REQUEST_CODE = 100;
    public static final int ADD_COMMENT_REQUEST_CODE = 101;
    public static final int ADDRESS_REQUEST_CODE = 102;
    public static final int COUPON_REQUEST_CODE = 103;
    public static final int CONFIRM_ORDER_REQUEST_CODE = 104;
    public static final int ORDER_DETAILS_REQUEST_CODE = 105;
    //result code
    public static final int RESULT_CODE_OK = 200;
    /*socket 连接常量定义*/
    //广播服务
    public static final String EVENT_BROADCASTINGLISTENER = "broadcastingListen";
    //心跳包
    public static final String EVENT_HEARTBEAT = "heartbeat";
    //握手包
    public static final String EVENT_HANDSHAKE = "conn";
    //自己发送的消息事件
    public static final String EVENT_BROAD_CAST = "broadcast";
    /*END*/
}
