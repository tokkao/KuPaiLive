package com.benben.kupaizhibo.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.KuPaiLiveApplication;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.RechargeNumbersListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.api.PayResultListener;
import com.benben.kupaizhibo.bean.PayBean;
import com.benben.kupaizhibo.bean.RechargeRuleBean;
import com.benben.kupaizhibo.bean.WalletMoneyBean;
import com.benben.kupaizhibo.config.Constants;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.utils.PayListenerUtils;
import com.google.gson.Gson;
import com.hyphenate.helper.StatusBarUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-07-04.
 * Describe:充值钻石
 */
public class RechargeDiamondActivity extends BaseActivity {

    private static final String TAG = "RechargeDiamondActivity";
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.rlv_diamond_numbers)
    RecyclerView rlvDiamondNumbers;
    @BindView(R.id.tv_alipay)
    TextView tvAlipay;
    @BindView(R.id.tv_wxpay)
    TextView tvWxpay;
    @BindView(R.id.btn_pay)
    Button btnPay;

    //支付的回调
    private OnPayCallback onPayCallback;
    private int mPayWay = -1; // 0支付宝 1 微信
    private RechargeNumbersListAdapter mAdapter;
    private int rechargeTypeId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_diamond;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.recharge));
        rightTitle.setText(R.string.account_details);
        ImageUtils.getPic(KuPaiLiveApplication.mPreferenceProvider.getPhoto(),civAvatar,mContext,R.mipmap.icon_default_avatar);
        rlvDiamondNumbers.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAdapter = new RechargeNumbersListAdapter(mContext);
        rlvDiamondNumbers.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<RechargeRuleBean>() {
            @Override
            public void onItemClick(View view, int position, RechargeRuleBean model) {
                rechargeTypeId =
                        model.getId();
            }

            @Override
            public void onItemLongClick(View view, int position, RechargeRuleBean model) {

            }
        });

        tvName.setText(KuPaiLiveApplication.mPreferenceProvider.getUserName());
        getMyMoney();


        onPayCallback = new OnPayCallback() {
            @Override
            public void paySuccess() {
                toast(mContext.getResources().getString(R.string.pay_success));
                finish();
            }

            @Override
            public void payCancel() {
                toast(mContext.getResources().getString(R.string.pay_cancel));
            }

            @Override
            public void payFail() {
                toast(mContext.getResources().getString(R.string.pay_fail));
            }
        };


        //微信支付的回调监听
        PayListenerUtils.getInstance(this).addListener(new PayResultListener() {
            @Override
            public void onPaySuccess() {
                //支付成功
                toast(mContext.getResources().getString(R.string.pay_success));
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onPayError() {
                //支付失败
                toast(mContext.getResources().getString(R.string.pay_fail));
            }

            @Override
            public void onPayCancel() {
                //取消支付
                toast(mContext.getResources().getString(R.string.pay_cancel));
            }
        });
    }

    private void getMyMoney() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_MY_MONEY)
                .json().post()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                WalletMoneyBean result = JSONUtils.jsonString2Bean(json, WalletMoneyBean.class);
                if (result != null) {
                    tvBalance.setText(mContext.getResources().getString(R.string.my_account_balance, StringUtils.getWanStr(result.getUser_bobi())));
                }
                //请求充值规则
                getRechargeNumbersList();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }

    //获取充值数量列表
    private void getRechargeNumbersList() {


        BaseOkHttpClient.newBuilder()
                .addParam("group", 0)// 默认0通用，1 IOS
                .url(NetUrlUtils.RECHARGE_RULE)
                .json().post()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                List<RechargeRuleBean> lstRechargeRule = JSONUtils.jsonString2Beans(json,
                        RechargeRuleBean.class);
                if (lstRechargeRule != null) {
                    mAdapter.refreshList(lstRechargeRule);
                } else {
                    lstRechargeRule.clear();
                }
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }

    @OnClick({R.id.right_title, R.id.tv_alipay, R.id.tv_wxpay, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_title://明细
                Intent intent = new Intent(mContext, AccountDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.tv_alipay://支付宝支付
                mPayWay = 0;
                tvAlipay.setSelected(true);
                tvWxpay.setSelected(false);
                break;
            case R.id.tv_wxpay://微信支付
                mPayWay = 1;
                tvAlipay.setSelected(false);
                tvWxpay.setSelected(true);
                break;
            case R.id.btn_pay://支付
                goPay();
                break;
        }
    }

    private void goPay() {
        if (rechargeTypeId == -1) {
            toast(mContext.getResources().getString(R.string.select_recharge_numer));
            return;
        }
        if (mPayWay == -1) {
            toast(mContext.getResources().getString(R.string.select_pay_way));
            return;
        }

        //生成订单
        createOrder();
    }


    //生成订单
    private void createOrder() {
        BaseOkHttpClient.newBuilder()
                .addParam("order_type", "recharge")
                .addParam("product_id", rechargeTypeId)
                .url(NetUrlUtils.PAY_ADD_ORDER)
                .post().json().build()
                .enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String json, String msg) {
                        String order_sn = JSONUtils.getNoteJson(json, "sn");
                        String pay_price = JSONUtils.getNoteJson(json, "pay_price");

                        if (StringUtils.isEmpty(order_sn)
                                || StringUtils.isEmpty(pay_price)) {
                            return;
                        }
                        //选择支付方式
                        payOnline(mPayWay == 0 ? NetUrlUtils.PAY_GET_ALIPAY : NetUrlUtils.PAY_GET_WXPAY, order_sn, pay_price);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.e(TAG, msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.e(TAG, e.getLocalizedMessage());
                    }
                });
    }


    /**
     * 去付款
     */
    private void payOnline(String url, String order_sn, String price) {
        BaseOkHttpClient.newBuilder()
                .url(url)
                .addParam("sn", order_sn)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                if (mPayWay == 0) {
//                    //支付宝支付
                    String value = JSONUtils.getNoteJson(result, "_string");
                    alipay(value);
                } else if (mPayWay == 1) {
                    //微信支付
                    PayBean payBean = new Gson().fromJson(result, PayBean.class);
                    wxpay(payBean);
                }
            }

            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext, msg);
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }

    IWXAPI api;

    /**
     * 微信支付
     */
    private void wxpay(PayBean bean) {
         LogUtils.e(TAG, bean.toString() );
        api = WXAPIFactory.createWXAPI(mContext, null);
        api.registerApp(Constants.WX_APP_ID);//微信的appkey

        PayReq request = new PayReq();
        request.appId = bean.getAppid();
        request.partnerId = bean.getPartnerid();
        request.prepayId = bean.getPrepayid();
        request.packageValue = bean.getPackageX();
        request.nonceStr = bean.getNoncestr();
        request.timeStamp = bean.getTimestamp();
        request.sign = bean.getSign();
        api.sendReq(request);
    }

    /**
     * 支付宝支付
     */
    private void alipay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝支付的回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Map<String, String> result = (Map<String, String>) msg.obj;

                String resultStatus = result.get("resultStatus");

                if (resultStatus.equals("4000")) {
                    //支付失败
                    if (onPayCallback != null) {
                        onPayCallback.payFail();
                    }
                } else if (resultStatus.equals("9000")) {
                    //支付成功
                    if (onPayCallback != null) {
                        onPayCallback.paySuccess();
                    }
                } else {
                    if (onPayCallback != null) {
                        onPayCallback.payCancel();
                    }
                }
            }
        }
    };

    /**
     * 支付的回调
     */
    public interface OnPayCallback {
        void paySuccess();

        void payCancel();

        void payFail();
    }

}
