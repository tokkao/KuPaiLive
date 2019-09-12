package com.benben.kupaizhibo.api;

/**
 * 功能:APP接口类
 */
public class NetUrlUtils {
    //该项目接口命名，根据后台接口的真实地址，全参数命名
    public static String BASEURL = "http://bjzb.hncjne.com/api/v1/";

    //首页活动列表-获取活动
    public static String GET_LIVE_ACTIVE_LIST = BASEURL + "5cb83af2dc193";

    public static String POST_CODE = BASEURL + "5b5bdc44796e8";
    //注册
    public static String REGIST_USER = BASEURL + "5cad9f63e4f94";
    //手机号验证码登录
    public static String USER_MOBILE_LOGIN = BASEURL + "5c78dca45ebc1";
    //用户名密码登录
    public static String USER_NAME_LOGIN = BASEURL + "5c78dbfd977cf";
    //获取验证码
    public static String GET_CODE = BASEURL + "5b5bdc44796e8";
    //忘记|找回密码
    public static String FORGET_PASSWORD = BASEURL + "5caeeba9866aa";
    //创建直播
    public static String LIVES_CREATE_ROOM = BASEURL + "5ca9b9a73f6e9";
    //更改直播状态
    public static String LIVES_CHANGE_STATUS = BASEURL + "5caafe7620142";
    //进入直播间
    public static String LIVES_ENTER_ROOM = BASEURL + "5cab010d53c97";
    //关闭直播间
    public static String LIVES_STOP_ROOM = BASEURL + "5cab096a0708e";
    //获取直播关闭统计信息
    public static String LIVES_GET_STOP_MSG = BASEURL + "5cac612833807";
    //获取直播间观众列表
    public static String LIVES_GET_USER_LIST = BASEURL + "5cac626d79027";
    //获取首页话题直播列表
    public static String INDEX_GET_TOPIC_LIVE = BASEURL + "5cb45778a2cfc";
    //直播话题/标签
    public static String LIVES_GET_TOPIC_TAGS = BASEURL + "5cb69a27c02b1";
    //直播活动-参与活动
    public static String LIVESACTIVE_JOIN_LIVE_ACTIVE = BASEURL + "5cb83eb293f95";
    //直播活动-获取活动主播列表
    public static String LIVESACTIVE_ACTIVE_LIVES_LIST = BASEURL + "5cc26e6e2f703";
    //直播--举报列表
    public static String LIVES_REPORT_CLASSIFY = BASEURL + "5cc2aca48e264";
    //直播--举报
    public static String LIVES_REPORT_ADD = BASEURL + "5cc2bfa2e3233";
    //获取礼物列表
    public static String LIVES_GET_GIFT = BASEURL + "5cac6086e2dad";
    //赠送礼物（通用）
    public static String LIVES_SEND_GIFT = BASEURL + "5cac5f2d938b3";
    //获取指定广告位的广告列表
    public static String ADS_GET_ADS = BASEURL + "5c94aa1a043e7";

    //统一单张图片上传接口
    public static String PICTURE_UPLOAD = BASEURL + "5cb7e8e675628";

    //日记--列表
    public static String QUERY_DIARY_LIST = BASEURL + "5cb84a80342df";
    //日记--我的日记
    public static String QUERY_MY_DIARY_LIST = BASEURL + "5cb84ca66daae";
    //日记--发布后续日记
    public static String RELEASE_FOLLOW_UP_DIARY = BASEURL + "5cb847467781a";
    //日记--发布
    public static String CONFIRM_RELEASE_DIARY = BASEURL + "5cb830278f0e4";
    //日记--详情列表
    public static String DIARY_DETAILS_LIST = BASEURL + "5cb92f7f877d6";
    //日记--日记顶部详情
    public static String DIARY_TOP_DETAILS = BASEURL + "5cb852de46dc6";
    //日记--变美详情
    public static String CHANGE_BEAUTY_DETAILS = BASEURL + "5cb945f04e80c";
    //日记--评价
    public static String DIARY_COMMENT = BASEURL + "5cb9603982ed2";
    //日记--评价列表
    public static String DIARY_COMMENT_LIST = BASEURL + "5cb96ad730453";
    //日记--点击放大图片
    public static String DIARY_CLICK_IMG_BIG = BASEURL + "5cb996e20b608";
    //日记--删除变美过程
    public static String DELETE_ADDONS = BASEURL + "5cb9a1c977b15";

    //商城--收货地址--添加收货地址
    public static String ADD_ADDRESS = BASEURL + "5cadb304426d8";
    //商城--收货地址--列表
    public static String ADDRESS_LIST = BASEURL + "5cadcdd909c17";
    //商城--收货地址--修改默认地址
    public static String CHANGE_DEFAULT_ADDRESS = BASEURL + "5cadce9008a62";
    //商城--收货地址--删除地址
    public static String DEL_ADDRESS = BASEURL + "5cadd0d3a0c93";
    //商城--收货地址--获得单条数据
    public static String GET_ONE_ADDRESS = BASEURL + "5cadc769e4f16";
    //商城--订单--支付
    public static String PAY_ORDER = BASEURL + "5cbd28fc6f231";
    //商城--购物车--默认收货地址优惠券信息
    public static String GET_ADDRESS_COUPON = BASEURL + "5cb6903e68025";
    //商城--订单--删除订单
    public static String DEL_ORDER = BASEURL + "5cb5a4db11da5";
    //商城--订单--提醒发货
    public static String NOTIC_SEND_ORDER = BASEURL + "5cb42d69ca20b";
    //商城--订单--再次购买
    public static String AGAIN_BUY = BASEURL + "5cb3f7a5a2aa6";
    //商城--订单--评价
    public static String DO_COMMENT = BASEURL + "5cb3f13570cb6";
    //商城--订单--订单详情
    public static String DETAIL_ORDER = BASEURL + "5caf0f6bc1d65";
    //商城--订单--取消/完成订单
    public static String CANCEL_ORDER = BASEURL + "5caefb77df573";
    //商城--订单--订单列表
    public static String ORDER_LIST = BASEURL + "5caef2e0a028d";
    //商城--购物车--生成订单
    public static String ADD_ORDER = BASEURL + "5caeb2bb97a9b";
    //删除购物
    public static String DEL_CART = BASEURL + "5cad9d47a442b";
    //商城--购物车--修改购物车
    public static String EDIT_CART = BASEURL + "5cad9a6a064f9";
    //购物车列表
    public static String CART_LIST = BASEURL + "5cad973135249";
    //商城--购物车--添加
    public static String ADD_CART = BASEURL + "5cad8295a0df0";
    //商城--商品详情
    public static String GOODS_DETAIL = BASEURL + "5cac5e85a3af6";
    //商城--商品分类列表
    public static String GET_CATEGORY_LIST = BASEURL + "5cac03aac2fdd";
    //商城--商品列表
    public static String GET_GOODS_LIST = BASEURL + "5cac0ce69cb61";

    //保存意见反馈
    public static String SUGGESTIONS = BASEURL + "5cc3f28296cf0";

    //会员--验证用户实名认证
    public static String USER_AUTHENTICATION = BASEURL + "5cb9ac3a7d9c3";
    //获取会员详细信息
    public static String GET_USER_INFO = BASEURL + "5c78c4772da97";
    //会员签到
    public static String USER_SIGN_IN = BASEURL + "5caf00505dd00";
    //个人资料-个人资料修改
    public static String EDIT_USER_INFO = BASEURL + "5cb54af125f1c";
    //会员--案例--首页列表
    public static String CASES_INDEX_LIST = BASEURL + "5cba7df1a4147";

    //用户--优惠券--优惠券列表
    public static String COUPON_LIST = BASEURL + "5cb5ad18a18fb";
    //会员--优惠券--优惠券详情
    public static String COUPON_DETAIL = BASEURL + "5cb5aec30095a";

    //会员--执业资格图片列表
    public static String GET_PROFESSIONAL_IMAGE = BASEURL + "5cb9ae51b12ac";
    //会员--机构执业资格修改
    public static String USER_PROFESSIONAL_EDIT = BASEURL + "5cb9aeb2254ec";
    //会员--机构执业资格图片删除
    public static String USER_PROFESSIONAL_DELETE = BASEURL + "5cbb0c56680f5";

    //会员--医院环境图片列表
    public static String GET_HOSPITAL_IMAGE = BASEURL + "5cb9afd54ab81";
    //会员--医院环境图片修改
    public static String USER_HOSPITAL_EDIT = BASEURL + "5cb9af44196f7";
    //会员--机构医院环境图片删除
    public static String USER_HOSPITAL_DELETE = BASEURL + "5cbb09a524cc9";

    //关注和粉丝列表
    public static String ATTENTION = BASEURL + "5cb6c07f79fb8";
    //关注主播/取消关注主播
    public static String FOLLOW = BASEURL + "5cac603d80bd7";

    //获取腾讯视频上传签名
    public static String GET_VIDEO_SIGN = BASEURL + "5cd62adf38d1e";

    //删除系统消息/站内信
    public static String DEL_SYSTEM_MSG = BASEURL + "5cc56bffbfe7a";
    //获取系统消息/站内信
    public static String GET_SYSTEM_MSG_LIST = BASEURL + "5cc56966e9287";
    //我的钱包-消费明细
    public static String GET_MONEY_DETAIL = BASEURL + "5cc45422e5c87";
    //余额明细 【和上面的消费明细接口重复了】
//    public static String GET_MONEY_DETAIL  = BASEURL+"5cb6c3ee60e5f";
    // 我的钱包-获取糖豆，总收益
    public static String GET_MY_MONEY = BASEURL + "5cc45274d6be9";
    //获取充值规则
    public static String RECHARGE_RULE = BASEURL + "5cd2b4631e656";

    //机构-服务分类接口
    public static String USER_AGENCY_SERVER_CATEGORY = BASEURL + "5cc269a90d0d6";
    //主播-机构服务订单-获取订单列表
    public static String LIVE_ORDER_LIST = BASEURL + "5cc181184947a";
    //机构服务订单-订单详情
    public static String USER_AGENCY_ORDER_INFO = BASEURL + "5cc17608505d7";
    //会员机构服务 订单列表
    public static String USER_ORDER_LIST = BASEURL + "5cc1482ff0ce3";
    //机构服务订单-提交订单
    public static String SUBMIT_ORDER = BASEURL + "5cc11a1146868";
    //机构-- 服务设置修改
    public static String EDIT_USER_AGENCY_SERVER = BASEURL + "5cbef05a6f1fe";
    //机构-- 服务设置查看详情
    public static String USER_AGENCY_SERVER_INFO = BASEURL + "5cbeeec5ae2e1";
    //机构 -- 删除单条服务设置
    public static String DELETE_USER_AGENCY_SERVER = BASEURL + "5cbee275171b8";
    //机构—— 服务设置 新增接口
    public static String ADD_USER_AGENCY_SERVER = BASEURL + "5cbede6d3dfda";
    //机构—服务设置列表
    public static String USER_AGENCY_SERVER = BASEURL + "5cbebca0e740f";
    //获取会员视频列表
    public static String GET_USER_VIDEO = BASEURL + "5cd90e68b2c90";


    //个人资料-标签修改-获取标签列表
    public static String GET_USER_LABEL = BASEURL + "5cb6d0ca3c331";
    //个人资料-标签修改-保存修改
    public static String EDIT_USER_LABEL = BASEURL + "5cb6d21e49e76";

    //会员-上传背景图
    public static String USER_BACKGROUND = BASEURL + "5cc2ab24c16e5";


    //主播/超管设置一个会员为房管
    public static String SET_ADMIN = BASEURL + "5cac62b15ecba";
    //主播/超管取消一个会员的房管
    public static String DEL_ADMIN = BASEURL + "5cac62ed2bf96";
    //禁言/踢人(需房管以上权限userType>=30)
    public static String SHUT_UP = BASEURL + "5cac5ffd43a85";
    //抢红包
    public static String LOTTERY_DRAW = BASEURL + "5cbffc8bacc5d";
    //创建红包
    public static String CREATE_ENVELOPES = BASEURL + "5cbffc042e9ad";
    //获取红包领取日志
    public static String GET_LOG = BASEURL + "5cbffcc98a944";


    //获取环信会员的昵称和头像
    public static String GET_EASEMOB_USER = BASEURL + "5cc2d001e4c6b";


    /*-----------------------------------陪护模块-----------------------------------------------*/
    //陪护--糖豆支付陪护订单
    public static String BOBI_PAY_ORDER = BASEURL + "5ce4a556de7ed";
    //主播修改陪聊设置-获取性格标签列表 项目列表
    public static String CHARACTER_LABEL_LIST = BASEURL + "5cbea3e5f2f9d";
    //陪聊订单糖豆支付- 支付提交
    public static String PAY_ESCORT_ORDER = BASEURL + "5cbd6cbcb0d51";
    //主播订单 - 主播陪护列表获取
    public static String LIVE_ESCORT_ORDER_LIST = BASEURL + "5cbd2ca649545";
    //主播陪聊订单-获取陪聊订单详情
    public static String LIVE_ESCORT_ORDER_INFO = BASEURL + "5cbd36a5a7761";
    //主播陪护订单详情-结束订单
    public static String FINISH_ESCORT_ORDER = BASEURL + "5cbd0e76b2d0c";
    //订单评价-提交评价
    public static String APPRAISE_ESCORT_ORDER = BASEURL + "5cbaf616bb481";
    //会员陪护订单列表-获取会员陪护订单列表
    public static String USER_ESCORT_ORDER_LIST = BASEURL + "5cbadd1d13326";
    //会员陪护订单-获取订单详情
    public static String USER_ESCORT_ORDER_INFO = BASEURL + "5cbae3a0afe7b";
    //陪聊订单-续费陪聊订单
    public static String RENEWAL_ESCORT_ORDER = BASEURL + "5cbace8ecba87";
    //陪聊-提交订单
    public static String SUBMIT_ESCORT_ORDER = BASEURL + "5cbac5e607ce9";
    //陪聊选择-获取选择后的主播列表(陪聊搜索结果列表)
    public static String CHOOSE_ESCORT_USER_LIST = BASEURL + "5cbac4264430d";
    //陪聊-获取陪聊主播列表
    public static String GET_ESCORT_USER_LIST = BASEURL + "5cbab85e5ac30";
    //陪聊主播 - 获取主播详细信息
    public static String GET_ESCORT_USER_INFO = BASEURL + "5cbac04d0c84a";
    //陪聊设置-保存修改设置
    public static String EDIT_ESCORT_SET = BASEURL + "5cbab4cfd79f7";
    //主播陪护设置-获取设置信息
    public static String GET_ESCORT_SET = BASEURL + "5cbab2c8a6ca2";
    //机构订单 评价列表
    public static String SERVICE_APPRAISE_LIST = BASEURL + "5d0dcec3eb85c";
    //陪护订单评价列表
    public static String ESCORT_APPRAISE_LIST = BASEURL + "5d0dd6cc50014";




    /*-----------------------------------系统-----------------------------------------------*/
    //验证用户TOKEN是否有效
    public static String VERIFY_TOKEN = BASEURL + "5cb82995d1f1d";

    //获取首页关注和同城主播
    public static String GET_CITY_FOLLOW_LIVE = BASEURL + "5cd3fc22a9120";

    //首页搜索排行
    public static String GET_SEARCH_RANKING = BASEURL + "5cdb74a4e6867";

    //首页搜索-获取搜索信息
    public static String GET_SEARCH_INFO = BASEURL + "5ce224d36b215";

    //统一下单接口
    public static String PAY_ADD_ORDER = BASEURL + "5ce8a7d7d03aa";
    // 支付宝支付
    public static String PAY_GET_ALIPAY = BASEURL + "5ce8a4fdd693a";
    //微信支付
    public static String PAY_GET_WXPAY = BASEURL + "5ce8a45b71468";

    //返回用户说明/协议
    public static String ARTICLE_GET_LIST = BASEURL + "5cf5230d156ff";

    //获取配置信息
    public static String INDEX_GET_CONFIG = BASEURL + "5cbd22d73dff9";


    /*-----------------------------------会员-----------------------------------------------*/
    //提现展示页面
    public static String SHOW_WITHDRAW = BASEURL + "5ce39a2a80e80";
    //提现
    public static String WITH_DRAW = BASEURL + "5ce25d5e1ffb8";
    //会员--案例--购买图片
    public static String PAY_PICTURE = BASEURL + "5cbd1e9f7d1dd";
    //会员--案例--支付
    public static String CASES_PAY_ORDER = BASEURL + "5cbd2489c0e49";
    //会员-用户认证状态
    public static String GET_USER_STATUS = BASEURL + "5cdcb347f14f0";
    //收益记录
    public static String USER_EARING_RECORD = BASEURL + "5cf8e4068ffb0";

    //主播（会员）订单 结束订单
    public static String FINISH_ORDER = BASEURL + "5cc17c41a80b6";

    // 主播机构订单-获取订单详情
    public static String LIVE_ORDER_INFO = BASEURL + "5cc195bb34918";

    //机构订单-余额支付-提交
    public static String USER_AGENCY_ORDER_PAY_ORDER = BASEURL + "5cc12690caa35";

    //获取日记蜕变 项目列表
    public static String GET_PROJECT_LIST = BASEURL + "5d106b1d7687c";

    //会员相册---我的相册列表
    public static String MY_ALBUM_LIST = BASEURL + "5d04b1b7693d3";
    // 会员相册---删除相册图片
    public static String DELETE_USER_ALBUM = BASEURL + "5d04ac0f3e2bf";
    // 会员相册---添加相册图片
    public static String ADD_USER_ALBUM = BASEURL + "5d04a55e23025";

    // 银行账户删除
    public static String BANKS_ACCOUNT_DEL = BASEURL + "5d1424274ec28";
    // 银行账户列表
    public static String BANKS_ACCOUNT_LIST = BASEURL + "5d14209131768";
    // 银行账户修改
    public static String BANKS_ACCOUNT_EDIT = BASEURL + "5d141f513d6d0";
    // 银行账户新增
    public static String BANKS_ACCOUNT_SUBMIT = BASEURL + "5d141e8059718";
    //银行卡列表
    public static String BANK_LIST = BASEURL + "5d141bcc7a760";

    //分享 邀请码
    public static String SHOW_INVITE_CODE = BASEURL + "5d108a9f8898c";

    //绑定手机号
//    public static String BINDING_MOBILE  = BASEURL + "5d0dfc5d28f14";
    public static String BINDING_MOBILE  = BASEURL + "5d3bc7a3b532b";
    //修改手机号
    public static String MODIFY_MOBILE  = BASEURL + "5d3270138c6db";

    //第三方登录
    public static String THIRD_LOGIN  = BASEURL + "5d398564cb5ef";
    //主播认证
    public static String ANCHOR_AUTHENTICATION  = BASEURL + "5d2c46b4ef9c5";
    //获取守护类型列表
    public static String GET_GUARD_TYPE_LIST  = BASEURL + "5d27fba76b86d";
    //家长控制模式
    public static String SWITCH_PARENTAL_CONTROL_MODE  = BASEURL + "5d0c4b92b1622";
    //实名认证
    public static String REAL_NAME_AUTHENTICATION  = BASEURL + "5d2c38fccd0a3";
    //获取用户贡献排行榜
    public static String GET_CONTRIBUTION_RANKING  = BASEURL + "5cac61964564d";
    //设置公告
    public static String SET_NOTICE_CONTENT  = BASEURL + "5d2c35e419bfc";
    //用户等级
    public static String GET_USER_LEVEL  = BASEURL + "5d0b32839d5a7";
    //主播等级
    public static String GET_ANCHOR_LEVEL  = BASEURL + "5d3fa8f66e1fc";
    //打赏记录
    public static String GET_REWARD_LIST  = BASEURL + "5d0af85b4eacd";
    //分享记录
    public static String GET_SHARING_LIST  = BASEURL + "5d2c1b67bba2a";
    //分享
    public static String GO_SHARE  = BASEURL + "5d2c1c6a094c5";
    //用户邀请二维码
    public static String USER_QR_CODE  = BASEURL + "5d2d1eb55c079";
    //获取默认控制状态
    public static String GET_DEFAULT_CONTROL_STATUS  = BASEURL + "5d3273bb2756a";
    //他人的粉丝和关注列表
    public static String GET_OTHER_FANS_FOLLOW  = BASEURL + "5d32789510ed7";
    //未读消息
    public static String GET_UNREAD_MESSAGE_NUM  = BASEURL + "5d328679b06e0";
    //是否正在直播
    public static String GET_IS_LIVE  = BASEURL + "5d32d52fb9f76";
    //守护坐席列表
    public static String GET_GUARD_LIST  = BASEURL + "5d350da8cd314";
    //开通守护
    public static String RECHARGE_GUARD  = BASEURL + "5d2696e57bfa4";
    //用户认证类型状态
    public static String AUTHENTICATION_STATUS  = BASEURL + "5d37d06795ba1";
    //支付宝支付
    public static String ALIPAY  = BASEURL + "5d2c29833bd7d";
    //微信支付
    public static String WXPAY  = BASEURL + "5d3815939b72e";
    //三方登录
    public static String NEW_THIRD_LOGIN  = BASEURL + "5d398564cb5ef";
    //获取客服列表
    public static String GET_CUSTOM_SERVICE  = BASEURL + "5d3e5f49ae7de";
    //首页推荐主播列表
    public static String GET_BANNER_LIST  = BASEURL + "5d417f58bd252";
    //守护说明
    public static String GET_GUARD_DESCRIPTION  = BASEURL + "5d424a2b1a61c";
    //提现记录
    public static String GET_WITHDRAW_RECORD  = BASEURL + "5d4b785d8dadd";

}
