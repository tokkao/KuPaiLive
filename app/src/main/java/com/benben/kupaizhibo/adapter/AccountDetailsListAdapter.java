package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.WalletDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 账单流水列表adapter
 */
public class AccountDetailsListAdapter extends AFinalRecyclerViewAdapter<WalletDetailBean> {



    public AccountDetailsListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new GuardSeatListViewHolder(m_Inflater.inflate(R.layout.item_account_details_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((GuardSeatListViewHolder) holder).setContent(getItem(position), position);
    }

    public class GuardSeatListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_unit)
        TextView tvUnit;

        View itemView;

        public GuardSeatListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(WalletDetailBean mWalletDetailBean, int position) {
            tvType.setText(mWalletDetailBean.getChange_type());
            tvDate.setText(mWalletDetailBean.getCreate_time());
            if (mWalletDetailBean.getChange_bobi() < 0) {
                tvUnit.setText("- ￥");
                tvMoney.setText(""+Math.abs(mWalletDetailBean.getChange_bobi()));
            } else {
                tvUnit.setText("+ ￥");
                tvMoney.setText("" + mWalletDetailBean.getChange_bobi());
            }

        }
    }
}
