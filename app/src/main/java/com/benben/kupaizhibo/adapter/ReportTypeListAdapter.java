package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.ReportListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-02.
 * Describe: 举报列表adapter
 */
public class ReportTypeListAdapter extends AFinalRecyclerViewAdapter<ReportListBean> {


    public ReportTypeListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new ReportTypeListViewHolder(m_Inflater.inflate(R.layout.item_report_type_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((ReportTypeListViewHolder) holder).setContent(getItem(position), position);
    }

    public class ReportTypeListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_report_type)
        TextView tvReportType;
        View itemView;

        public ReportTypeListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(ReportListBean mReportTypeListBean, int position) {
            tvReportType.setSelected(mReportTypeListBean.isSelect());
            tvReportType.setTextColor(mReportTypeListBean.isSelect() ? m_Context.getResources().getColor(R.color.color_FF6261) : m_Context.getResources().getColor(R.color.color_FFFFFF));
            tvReportType.setText(mReportTypeListBean.getClassify_name());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectSingle(position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, position, mReportTypeListBean);
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
