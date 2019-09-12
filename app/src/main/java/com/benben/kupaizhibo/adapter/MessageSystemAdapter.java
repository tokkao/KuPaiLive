package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.MessageListBean;

/**
 * Author:zhn
 * Time:2019/5/13 0013 16:36
 * <p>
 * 系统消息列表Adapter
 */
public class MessageSystemAdapter extends AFinalRecyclerViewAdapter<MessageListBean> {

    public MessageSystemAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MessageListViewHolder(m_Inflater.inflate(R.layout.item_system_message,
                parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MessageListViewHolder) holder).setContent(position, getItem(position));
    }

    class MessageListViewHolder extends BaseRecyclerViewHolder {

        CircleImageView civMessageAvatar;
        TextView tvMessageTitle;
        TextView tvMessageDetail;
        TextView tvMessageTime;


        public MessageListViewHolder(View view) {
            super(view);
            civMessageAvatar = view.findViewById(R.id.civ_message_avatar);
            tvMessageTitle = view.findViewById(R.id.tv_message_title);
            tvMessageDetail = view.findViewById(R.id.tv_message_detail);
            tvMessageTime = view.findViewById(R.id.tv_message_time);
        }

        public void setContent(int position, MessageListBean data) {
            tvMessageTitle.setText(data.getEvent());
            tvMessageDetail.setText(data.getContent());
            tvMessageTime.setText(DateUtils.stampToDate(data.getCreate_time()));
        }
    }

}
