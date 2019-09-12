package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.MyRewardListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:zhn
 * Time:2019/5/13 0013 16:36
 * <p>
 * 我的打赏列表Adapter
 */
public class MyRewardListAdapter extends AFinalRecyclerViewAdapter<MyRewardListBean> {


    public MyRewardListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MyRewardListViewHolder(m_Inflater.inflate(R.layout.item_my_reward,
                parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MyRewardListViewHolder) holder).setContent(position, getItem(position));
    }

    class MyRewardListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public MyRewardListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public void setContent(int position, MyRewardListBean data) {
            ImageUtils.getPic(data.getAvatar(), civAvatar, m_Context, R.mipmap.icon_default_avatar);
            tvTitle.setText(data.getNickname());
            tvContent.setText(data.getGift_name() + " x " + data.getNumber());
            tvTime.setText(data.getCreate_time());
        }
    }

}
