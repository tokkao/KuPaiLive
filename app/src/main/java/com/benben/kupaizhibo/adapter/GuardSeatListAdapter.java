package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.GuardSeatListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 守护坐席列表adapter
 */
public class GuardSeatListAdapter extends AFinalRecyclerViewAdapter<GuardSeatListBean> {


    public GuardSeatListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new GuardSeatListViewHolder(m_Inflater.inflate(R.layout.item_guard_seat_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((GuardSeatListViewHolder) holder).setContent(getItem(position), position);
    }

    public class GuardSeatListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.iv_guard_type)
        ImageView ivGuardType;
        @BindView(R.id.tv_name)
        TextView tvName;


        View itemView;

        public GuardSeatListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(GuardSeatListBean mGuardSeatListBean, int position) {
            tvName.setText(mGuardSeatListBean.getUser_nickname());
            if("-1".equals(mGuardSeatListBean.getGuard_type())){
                ivGuardType.setVisibility(View.INVISIBLE);
            }else{
                ivGuardType.setVisibility(View.VISIBLE);
            }
            ivGuardType.setImageResource("0".equals(mGuardSeatListBean.getGuard_type()) ? R.mipmap.icon_gold_guard : R.mipmap.icon_silver_guard);
            ImageUtils.getPic(mGuardSeatListBean.getAvatar(), civAvatar, m_Context, R.drawable.image_placeholder);
        }
    }
}
