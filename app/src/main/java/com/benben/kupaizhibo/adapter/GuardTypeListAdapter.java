package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.GuardTypeListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 守护类型列表adapter
 */
public class GuardTypeListAdapter extends AFinalRecyclerViewAdapter<GuardTypeListBean> {


    public GuardTypeListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new GuardTypeListViewHolder(m_Inflater.inflate(R.layout.item_guard_type_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((GuardTypeListViewHolder) holder).setContent(getItem(position), position);
    }

    public class GuardTypeListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_guard_name)
        TextView tvGuardName;
        @BindView(R.id.tv_gold_price)
        TextView tvGoldPrice;
        View itemView;

        public GuardTypeListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(GuardTypeListBean mGuardTypeListBean, int position) {
            tvGuardName.setText(mGuardTypeListBean.getGuard_name());
            tvGoldPrice.setText(m_Context.getResources().getString(R.string.diamond_balance, mGuardTypeListBean.getGuard_price()));

            tvGoldPrice.setSelected(mGuardTypeListBean.isSelect());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectSingle(position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, position, mGuardTypeListBean);
                    }
                }
            });
        }
    }

    private void selectSingle(int posotion) {

        if (getList() == null) return;
        for (int i = 0; i < getList().size(); i++) {
            if (i == posotion) {
                getList().get(i).setSelect(true);
            } else {
                getList().get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();
    }


}
