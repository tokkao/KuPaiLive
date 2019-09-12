package com.benben.kupaizhibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.DensityUtil;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.EasemobUserBean;

import cn.qssq666.giftmodule.bean.GiftDemoModel;
import cn.qssq666.giftmodule.interfacei.GiftModelI;
import cn.qssq666.giftmodule.interfacei.UserInfoI;
import cn.qssq666.giftmodule.periscope.GiftAnimLayout;
import cn.qssq666.giftmodule.ui.StrokeTextView;


/**
 * Author:zhn
 * Time:2019/5/13 0013 16:36
 * <p>
 * 消息列表Adapter
 */
public class MessageListAdapter extends AFinalRecyclerViewAdapter<EasemobUserBean> {

    public MessageListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MessageListViewHolder(m_Inflater.inflate(R.layout.item_message_list, parent,
                false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MessageListViewHolder) holder).setContent(position, getItem(position));
    }

    class MessageListViewHolder extends BaseRecyclerViewHolder {

        View rootView;
        CircleImageView civMessageAvatar;
        TextView tvMessageTitle;
        TextView tvMessageInfo;
        TextView tvMessageTime;
        TextView tvMessageCount;


        public MessageListViewHolder(View view) {
            super(view);
            rootView = view;
            civMessageAvatar = view.findViewById(R.id.civ_message_avatar);
            tvMessageTitle = view.findViewById(R.id.tv_message_title);
            tvMessageInfo = view.findViewById(R.id.tv_message_info);
            tvMessageTime = view.findViewById(R.id.tv_message_time);
            tvMessageCount = view.findViewById(R.id.tv_message_count);
        }

        public void setContent(int position, EasemobUserBean data) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(rootView, position, data);
                    }
                }
            });
            if (data.isSystemMsg()) {
                civMessageAvatar.setImageResource(R.mipmap.icon_system_message);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvMessageTitle.getLayoutParams();
                layoutParams.setMargins(0, DensityUtil.dip2px(m_Context,27),0,0);
            } else {
                ImageUtils.getPic(data.geteAvatar(), civMessageAvatar, m_Context, R.mipmap.icon_default_header);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvMessageTitle.getLayoutParams();
                layoutParams.setMargins(0, DensityUtil.dip2px(m_Context,15),0,0);

            }
            tvMessageTitle.setText(data.geteNickName());
            tvMessageTime.setText(data.geteLastMsgTime());
//            tvMessageTime.setText(DateUtils.stampToDate(data.geteLastMsgTime()));
            tvMessageInfo.setText(data.geteLastMsg());
            if (data.geteUnreadMsgCount() > 0) {
                tvMessageCount.setVisibility(View.VISIBLE);
                if (data.geteUnreadMsgCount() > 9) {
                    tvMessageCount.setText("9+");
                } else {
                    tvMessageCount.setText(String.valueOf(data.geteUnreadMsgCount()));
                }
            } else {
                tvMessageCount.setVisibility(View.GONE);
            }
        }
    }

    //清空并添加一条数据
    public void refreshData(EasemobUserBean data){
        getList().clear();
        getList().add(data);
        notifyDataSetChanged();
    }

    /**
     * Create by wanghk on 2019-05-20.
     * Describe:
     */
    public static class LookLiveGiftBarAdapter implements GiftAnimLayout.GiftCallBack{


        private Context mContext;
        private static final String TAG = "GiftBarAdapter";

        public LookLiveGiftBarAdapter(Activity mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onGiftAnimOver(GiftModelI giftModel) {
            ((GiftDemoModel) giftModel).setShowcount(3);//如果是0则会直接设置为1的
            Log.e(TAG, "onGiftAnimOver:" + giftModel);
        }

        @Override
        public void onFindExistGiftAnim(GiftModelI giftModel) {
            //                ((GiftModel) giftModel).setShowcount(1);
            Log.e(TAG, "onFindExistGiftAnim:" + giftModel);

        }

        @Override
        public void onAddNewGift(GiftModelI giftModel) {
            Log.e(TAG, "onAddNewGift:" + giftModel);

        }

        @Override
        public void onAddWaitUnique(GiftModelI giftModel) {
            Log.e(TAG, "onAddWaitUnique:" + giftModel);

        }

        @Override
        public int onRequestShowGiftCount(GiftModelI modelI, StrokeTextView tvValue) {
            int showcount = ((GiftDemoModel) modelI).getShowcount();//如果一直返回0还是由内部支持
            //                showcount = showcount == 0 ? tvValue.getValue() + 1 : showcount;
            Log.e(TAG, "onRequestShowGiftCount :showCount:" + showcount);
            //                showcount = showcount == 0 ? tvValue.getValue() + 1 : showcount;
            return showcount;
        }

        @Override
        public ViewGroup getGiftLayout(GiftAnimLayout giftAnimLayout) {
            //返回null表示内部本来就有的那个
            return (ViewGroup) LayoutInflater.from(giftAnimLayout.getContext()).inflate(R.layout.layout_live_gift_bar_prescro, giftAnimLayout, false);
        }

        /**
         * 返回false表明内部进行绑定图片 那么这里就没必要再进行处理了。但是这里要维护的话头像也给维护了哈!
         *
         * @param userInfo
         * @param giftModel
         * @param giftHolder
         * @return
         */
        @Override
        public boolean onBindPic(UserInfoI userInfo, GiftModelI giftModel, GiftAnimLayout.GiftHolder giftHolder) {
            ImageUtils.getPic(userInfo.getFace(),giftHolder.ivFace,mContext,R.mipmap.icon_default_avatar);
            ImageUtils.getPic(giftModel.getGiftImage(),giftHolder.ivGift,mContext,R.mipmap.ic_default_pic);
            return true;
        }
    }
}

