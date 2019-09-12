package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.MyShareListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:zhn
 * Time:2019/5/13 0013 16:36
 * <p>
 * 我的分享列表Adapter
 */
public class MySharingListAdapter extends AFinalRecyclerViewAdapter<MyShareListBean> {


    public MySharingListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MySharingListViewHolder(m_Inflater.inflate(R.layout.item_my_sharing,
                parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MySharingListViewHolder) holder).setContent(position, getItem(position));
    }

    class MySharingListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public MySharingListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public void setContent(int position, MyShareListBean data) {
            tvTitle.setText(data.getType_name());
            tvTime.setText(data.getTime());
        }
    }

}
