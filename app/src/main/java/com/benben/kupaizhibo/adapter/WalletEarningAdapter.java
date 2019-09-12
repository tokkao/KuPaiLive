package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.WalletEarningBean;

/**
 * Author:zhn
 * Time:2019/5/15 0015 14:45
 * 钱包详情
 */
public class WalletEarningAdapter extends AFinalRecyclerViewAdapter<WalletEarningBean> {

    public WalletEarningAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new WalletDetailViewHolder(m_Inflater.inflate(R.layout.item_account_details_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((WalletDetailViewHolder) holder).setContent(position, getItem(position));
    }


    class WalletDetailViewHolder extends BaseRecyclerViewHolder {

        TextView tvWalletChangeType;
        TextView tvWalletCreateTime;
        TextView tvWalletChangeMoney;
        TextView tvUnit;

        public WalletDetailViewHolder(View view) {
            super(view);
            tvWalletChangeType = view.findViewById(R.id.tv_type);
            tvWalletCreateTime = view.findViewById(R.id.tv_date);
            tvWalletChangeMoney = view.findViewById(R.id.tv_money);
            tvUnit = view.findViewById(R.id.tv_unit);
        }

        public void setContent(int position, WalletEarningBean data) {
            tvWalletChangeType.setText(data.getChange_type_name());
            tvWalletCreateTime.setText(DateUtils.stampToDate(data.getCreate_time()));
            if (data.getChange_money() < 0) {
                tvUnit.setText("- ￥");
                tvWalletChangeMoney.setText("" + Math.abs(data.getChange_money()));
            } else {
                tvUnit.setText("+ ￥");
                tvWalletChangeMoney.setText("" + data.getChange_money());
            }
        }
    }


}
