package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.RechargeRuleBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 充值钻石数量列表adapter
 */
public class RechargeNumbersListAdapter extends AFinalRecyclerViewAdapter<RechargeRuleBean> {

    //当前选中的下标
    private int mIndex = 0;

    public RechargeNumbersListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new RechargeNumbersListViewHolder(m_Inflater.inflate(R.layout.item_recharge_number, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((RechargeNumbersListViewHolder) holder).setContent(getItem(position), position);
    }


    public class RechargeNumbersListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_diamond_num)
        TextView tvDiamondNum;
        @BindView(R.id.rlyt_item_view)
        RelativeLayout rlytItemView;
        View itemView;

        public RechargeNumbersListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(RechargeRuleBean mRechargeNumbersListBean, int position) {

            rlytItemView.setSelected(mRechargeNumbersListBean.isSelect());
            tvDiamondNum.setText(String.valueOf(mRechargeNumbersListBean.getName()));
            tvMoney.setText(String.valueOf(mRechargeNumbersListBean.getMoney()));
            rlytItemView.setBackgroundResource(mRechargeNumbersListBean.isSelect() ? R.drawable.shape_orange_radius4_fff9ec : R.drawable.shape_white_radius4_ffffff);
            tvDiamondNum.setTextColor(mRechargeNumbersListBean.isSelect() ? m_Context.getResources().getColor(R.color.color_333333) : m_Context.getResources().getColor(R.color.color_BDBDBD));
            tvMoney.setTextColor(mRechargeNumbersListBean.isSelect() ? m_Context.getResources().getColor(R.color.color_DEA61F) : m_Context.getResources().getColor(R.color.color_BDBDBD));
            tvUnit.setTextColor(mRechargeNumbersListBean.isSelect() ? m_Context.getResources().getColor(R.color.color_DEA61F) : m_Context.getResources().getColor(R.color.color_BDBDBD));

            //item点击事件
            rlytItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIndex = position;
                    singleSelect();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, position, mRechargeNumbersListBean);
                    }
                }
            });
        }
    }


    private void singleSelect() {
        for (int i = 0; i < getList().size(); i++) {
            getList().get(i).setSelect(mIndex == i);
        }
        notifyDataSetChanged();
    }
}
