package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.ShowWithdrawBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.ui.NormalWebViewActivity;
import com.benben.kupaizhibo.widget.CashPwdPopupWindow;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Author:zhn
 * Time:2019/5/15 0015 15:43
 * <p>
 * 我的钱包-提现页面
 */
public class WithdrawActivity extends BaseActivity {

    private static final String TAG = "WithdrawActivity";

    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.cb_ali)
    CheckBox cbAli;
    @BindView(R.id.edt_ali_account)
    EditText edtAliAccount;
    @BindView(R.id.cb_wechat)
    CheckBox cbWechat;
    @BindView(R.id.edt_weixin_account)
    EditText edtWeiXinAccount;
    @BindView(R.id.cb_yhk)
    CheckBox cbYhk;

    @BindView(R.id.tv_wallet_balance)
    TextView tvWalletBalance;
    @BindView(R.id.tv_wallet_tixian_money)
    TextView tvWalletTixianMoney;
    @BindView(R.id.edt_withdraw)
    EditText edtWithdraw;

    private final int WAY_NULL = -1;//未选择
    private final int WAY_ALI = 1;//支付宝
    private final int WAY_WECHAT = 2;//微信
    private final int WAY_YHK = 3;//银行卡
    @BindView(R.id.right_title)
    TextView rightTitle;

    //提现方式
    private int mWithdrawWay = WAY_ALI;

    private ShowWithdrawBean mShowWithdrawBean;

    //密码输入框
    private CashPwdPopupWindow mCashPwdPopupWindow;

    @Override
    protected int getStatusBarColor() {
        return R.color.color_FFFFFF;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected boolean needStatusBarDarkText() {
        return true;
    }

    @Override
    protected void initData() {
        centerTitle.setText("提现");
        rightTitle.setText("提现记录");
        cbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbWechat.setChecked(false);
                    cbYhk.setChecked(false);
                    mWithdrawWay = WAY_ALI;
                } else {
                    mWithdrawWay = WAY_NULL;
                }
            }
        });
        cbWechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbAli.setChecked(false);
                    cbYhk.setChecked(false);
                    mWithdrawWay = WAY_WECHAT;
                } else {
                    mWithdrawWay = WAY_NULL;
                }
            }
        });
        cbYhk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbAli.setChecked(false);
                    cbWechat.setChecked(false);
                    mWithdrawWay = WAY_YHK;
                } else {
                    mWithdrawWay = WAY_NULL;
                }
            }
        });

        mCashPwdPopupWindow = new CashPwdPopupWindow(mContext, new CashPwdPopupWindow.CashInterface() {
            @Override
            public void PwdCallback(String pwd) {
                if (mCashPwdPopupWindow.isShowing()) {
                    mCashPwdPopupWindow.dismiss();
                }
                //提现
                doWithdraw();
            }
        }, getWindow().getDecorView().getRootView());

        //获取提现数据
        getShowWithdraw();
    }


    //获取提现页面数据
    private void getShowWithdraw() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.SHOW_WITHDRAW)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                mShowWithdrawBean = JSONUtils.jsonString2Bean(result, ShowWithdrawBean.class);
                if (mShowWithdrawBean != null) {
                    refreshUI();
                }
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getMessage());
                toast("请求失败！");
            }
        });
    }

    private void refreshUI() {
        tvWalletBalance.setText("当前余额   ￥" + mShowWithdrawBean.getUser_money());
        tvWalletTixianMoney.setText(mShowWithdrawBean.getUser_money() + "");
    }

    @OnClick({R.id.rl_back, R.id.btn_withdraw, R.id.tv_wallet_tixian_shuoming, R.id.right_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.btn_withdraw:
                checkWithDraw();
                break;
            case R.id.tv_wallet_tixian_shuoming:
                //获取直播公约
                getArticleList();
                break;
            case R.id.right_title:
                //提现记录
                startActivity(new Intent(mContext, WithdrawRecordActivity.class));
                break;
        }
    }

    //获取直播公约
    private void getArticleList() {
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.ARTICLE_GET_LIST)
                .get().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {

                String url = JSONUtils.getNoteJson(result, "tixian_shuoming");
                NormalWebViewActivity.startWithData(mContext, url, "提现说明", true, true);

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, msg);
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });

    }


    //提现前验证
    private void checkWithDraw() {

        String money = edtWithdraw.getText().toString().trim();
        if (StringUtils.isEmpty(money)) {
            toast("提现金额不能为空");
            return;
        }
        if (Double.parseDouble(money) <= 0) {
            toast("提现金额不能为0");
            return;
        }
        if (mShowWithdrawBean == null
                || mShowWithdrawBean.getUser_money() < Double.parseDouble(money)) {
            toast("提现金额超限");
            return;
        }
        if (mWithdrawWay == WAY_NULL) {
            toast("请先选择提现方式");
            return;
        }
        String aliAccount = edtAliAccount.getText().toString();
        if (mWithdrawWay == WAY_ALI
                && StringUtils.isEmpty(aliAccount)) {
            toast("请输入支付宝账户");
            return;
        }
        String weixinAccount = edtWeiXinAccount.getText().toString();
        if (mWithdrawWay == WAY_WECHAT
                && StringUtils.isEmpty(weixinAccount)) {
            toast("请输入微信账户");
            return;
        }
        //   mCashPwdPopupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
        doWithdraw();
    }

    //提现
    private void doWithdraw() {
        String aliAccount = edtAliAccount.getText().toString();
        String weixinAccount = edtWeiXinAccount.getText().toString();
        String money = edtWithdraw.getText().toString().trim();

        BaseOkHttpClient.Builder builder = BaseOkHttpClient.newBuilder();
        if (mWithdrawWay == WAY_ALI) {
            builder.addParam("account", aliAccount);
        }
        if (mWithdrawWay == WAY_WECHAT) {
            builder.addParam("account", weixinAccount);
        }
        builder.addParam("money", Double.parseDouble(money))
                .addParam("type", mWithdrawWay)
                //  .addParam("payPassword", password)
                .url(NetUrlUtils.WITH_DRAW)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                ToastUtils.show(mContext, "提现成功！");
                onBackPressed();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getMessage());
                toast("请求失败！");
            }
        });
    }


}
