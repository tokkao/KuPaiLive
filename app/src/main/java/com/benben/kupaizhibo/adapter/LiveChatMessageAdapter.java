package com.benben.kupaizhibo.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.bean.socket.CTMessageBean;
import com.benben.kupaizhibo.bean.socket.SocketResponseHeaderBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.benben.kupaizhibo.widget.UserInfoDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * create by zjn on 2019/5/16 0016
 * email:168455992@qq.com
 */
public class LiveChatMessageAdapter extends AFinalRecyclerViewAdapter<SocketResponseHeaderBean> {

    private static final String TAG = "LiveChatMessageAdapter";
    private  String mStream;
    public LiveChatMessageAdapter(Activity ctx, String stream) {
        super(ctx);
        this.mStream = stream;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(m_Inflater.inflate(R.layout.item_live_chat_message, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MyViewHolder) holder).setContent(getItem(position), position);
    }

    public class MyViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;
        @BindView(R.id.iv_user_level)
        ImageView ivUserLevel;
        @BindView(R.id.rlyt_message)
        RelativeLayout rlytMessage;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(SocketResponseHeaderBean responseHeaderBean, int position) {
            if ("SendMsg".equals(responseHeaderBean.getMsg().get(0).get_method_())) {
                //聊天消息
                if ("2".equals(responseHeaderBean.getMsg().get(0).getMsgtype())) {
                    //直接取ct
                    //添加用户等级标识
                    if(responseHeaderBean.getMsg().get(0).getUser_level()!=null){
                        ivUserLevel.setVisibility(View.VISIBLE);
                        switch (responseHeaderBean.getMsg().get(0).getUser_level()) {
                            case "0":
                                ivUserLevel.setImageResource(R.mipmap.icon_level_0);
                                break;
                            case "1":
                                ivUserLevel.setImageResource(R.mipmap.icon_level_1);
                                break;
                            case "2":
                                ivUserLevel.setImageResource(R.mipmap.icon_level_2);
                                break;
                            case "3":
                                ivUserLevel.setImageResource(R.mipmap.icon_level_3);
                                break;
                            case "4":
                                ivUserLevel.setImageResource(R.mipmap.icon_level_4);
                                break;
                            case "5":
                                ivUserLevel.setImageResource(R.mipmap.icon_level_5);
                                break;
                            default:
                                ivUserLevel.setImageResource(R.mipmap.icon_level_5);
                                break;
                        }
                    }
                    String chatMessage = "<font color='#FF4545'>" + responseHeaderBean.getMsg().get(0).getUname() + ": " + "</font><font color='#FFFFFF'>" + responseHeaderBean.getMsg().get(0).getCt() + "</font>";
                    tvMessageContent.setText(Html.fromHtml(chatMessage));
                } else if ("0".equals(responseHeaderBean.getMsg().get(0).getMsgtype())) {
                    CTMessageBean ctMessageBean = JSONUtils.jsonString2Bean(responseHeaderBean.getMsg().get(0).getCt(), CTMessageBean.class);
                    String enterMessage = "<font color='#FF4545'>" + ctMessageBean.getUser_nickname() + "</font><font color='#FFFFFF'>进入了房间</font>";
                    tvMessageContent.setText(Html.fromHtml(enterMessage));
                }

            } else if ("SystemNot".equals(responseHeaderBean.getMsg().get(0).get_method_())) {
                ivUserLevel.setVisibility(View.GONE);

                //系统消息
                tvMessageContent.setText("系统消息：" + responseHeaderBean.getMsg().get(0).getCt());
                tvMessageContent.setTextColor(Color.parseColor("#FFFFFF"));
            } else if ("SendGift".equals(responseHeaderBean.getMsg().get(0).get_method_())) {
                ivUserLevel.setVisibility(View.GONE);
                //添加用户等级标识
                if(responseHeaderBean.getMsg().get(0).getUser_level()!=null){
                    ivUserLevel.setVisibility(View.VISIBLE);
                    switch (responseHeaderBean.getMsg().get(0).getUser_level()) {
                        case "0":
                            ivUserLevel.setImageResource(R.mipmap.icon_level_0);
                            break;
                        case "1":
                            ivUserLevel.setImageResource(R.mipmap.icon_level_1);
                            break;
                        case "2":
                            ivUserLevel.setImageResource(R.mipmap.icon_level_2);
                            break;
                        case "3":
                            ivUserLevel.setImageResource(R.mipmap.icon_level_3);
                            break;
                        case "4":
                            ivUserLevel.setImageResource(R.mipmap.icon_level_4);
                            break;
                        case "5":
                            ivUserLevel.setImageResource(R.mipmap.icon_level_5);
                            break;
                        default:
                            ivUserLevel.setImageResource(R.mipmap.icon_level_5);
                            break;
                    }
                }
                CTMessageBean ctMessageBean = JSONUtils.jsonString2Bean(responseHeaderBean.getMsg().get(0).getCt(), CTMessageBean.class);
                String giftMessage = "<font color='#FF4545'>" + responseHeaderBean.getMsg().get(0).getUname() + ": " + "</font><font color='#FFFFFF'>送了一个" + ctMessageBean.getGift_name() + "</font>";
                tvMessageContent.setText(Html.fromHtml(giftMessage));
                tvMessageContent.setTextColor(Color.parseColor("#FFFFFF"));
            } else if ("SendRedEnvelopes".equals(responseHeaderBean.getMsg().get(0).get_method_())) {
                ivUserLevel.setVisibility(View.GONE);

                if ("81".equals(responseHeaderBean.getMsg().get(0).getAction())
                        && "2".equals(responseHeaderBean.getMsg().get(0).getMsgtype())
                ) {
                    //用户接受红包结果，监听
                    tvMessageContent.setText(responseHeaderBean.getMsg().get(0).getUname() + ": " + responseHeaderBean.getMsg().get(0).getCt());
                    tvMessageContent.setTextColor(Color.parseColor("#D59CFF"));
                } else if ("80".equals(responseHeaderBean.getMsg().get(0).getAction())
                        && "1".equals(responseHeaderBean.getMsg().get(0).getMsgtype())
                ) {
                    //红包发送信息监听
//                    CTMessageBean ctMessageBean = JSONUtils.jsonString2Bean(responseHeaderBean.getMsg().get(0).getCt(),CTMessageBean.class);
                    tvMessageContent.setText("主播发送了一个红包");
                    tvMessageContent.setTextColor(Color.parseColor("#D59CFF"));
                }
            } else if ("ShutUpUser".equals(responseHeaderBean.getMsg().get(0).get_method_())) {
                ivUserLevel.setVisibility(View.GONE);

                //禁言提示
                if ("1".equals(responseHeaderBean.getMsg().get(0).getAction())
                        && "4".equals(responseHeaderBean.getMsg().get(0).getMsgtype())
                ) {
                    //剔出房间系统消息信息监听
                    tvMessageContent.setText("系统提示:" + responseHeaderBean.getMsg().get(0).getCt() + "");
                    tvMessageContent.setTextColor(Color.parseColor("#FFFFFF"));
                }
            } else if ("KickUser".equals(responseHeaderBean.getMsg().get(0).get_method_())) {
                ivUserLevel.setVisibility(View.GONE);

                //踢出用户监听
                if ("2".equals(responseHeaderBean.getMsg().get(0).getAction())
                        && "4".equals(responseHeaderBean.getMsg().get(0).getMsgtype())
                ) {
                    //剔出房间系统消息信息监听
                    tvMessageContent.setText(responseHeaderBean.getMsg().get(0).getUname() + ":" + responseHeaderBean.getMsg().get(0).getCt() + "");
                    tvMessageContent.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }

            rlytMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("SendMsg".equals(responseHeaderBean.getMsg().get(0).get_method_())) {
                        //聊天消息
                        //if ("2".equals(responseHeaderBean.getMsg().get(0).getMsgtype())) {
                        openUserInfoOptionPop(responseHeaderBean, position);
                        //}
                    }
                }
            });
        }
    }

    private void openUserInfoOptionPop(SocketResponseHeaderBean responseHeaderBean, int position) {

        BaseOkHttpClient.newBuilder()
                .addParam("uid", responseHeaderBean.getMsg().get(0).getUid())
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json()
                .build().enqueue(m_Activity, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                UserInfoBean mUserInfoBean = JSONUtils.jsonString2Bean(result, UserInfoBean.class);
                UserInfoDialog zhuBoInfoDialog = UserInfoDialog.getInstance();
                zhuBoInfoDialog.showUserInfoDialog(m_Activity, mUserInfoBean,mStream);
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


    private SpannableStringBuilder setDifTextColor(String var1, String var2, String color2) {

    /*    Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
        Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
        Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
      */
        Log.e(TAG, "setDifTextColor: var1=" + var1);
        if (StringUtils.isEmpty(var1)) {
            return null;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        //设置颜色
        ForegroundColorSpan blue = new ForegroundColorSpan(Color.parseColor(color2));
        //改变money字体颜色
        builder.setSpan(blue, 0, var1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }
}
