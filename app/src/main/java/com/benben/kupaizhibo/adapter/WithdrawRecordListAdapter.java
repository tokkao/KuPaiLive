package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.WithdrawRecordListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-02.
 * Describe: 提现记录列表adapter
 */
public class WithdrawRecordListAdapter extends AFinalRecyclerViewAdapter<WithdrawRecordListBean> {
    public WithdrawRecordListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new WithdrawRecordListViewHolder(m_Inflater.inflate(R.layout.item_withdraw_record, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((WithdrawRecordListViewHolder) holder).setContent(getItem(position), position);
    }

    public class WithdrawRecordListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_order_num)
        TextView tvOrderNum;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_account)
        TextView tvAccount;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        View itemView;

        public WithdrawRecordListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(WithdrawRecordListBean mWithdrawRecordListBean, int position) {
            tvOrderNum.setText("流水号:"+mWithdrawRecordListBean.getOrder_no());
            tvTime.setText(mWithdrawRecordListBean.getCreate_time());
            tvAccount.setText(mWithdrawRecordListBean.getAccount_id());
            String type = mWithdrawRecordListBean.getAccount_type() == 1 ? "微信" : "支付宝";
            tvType.setText(type +"   "+mWithdrawRecordListBean.getCash_fee());
            switch (mWithdrawRecordListBean.getCheck_status()){
                case 0:
                    tvStatus.setText("未审核");
                    break;
                case 1:
                    tvStatus.setText("已审核");
                    break;
                case 2:
                    tvStatus.setText("已拒绝");
                    break;
            }
        }
    }

}
