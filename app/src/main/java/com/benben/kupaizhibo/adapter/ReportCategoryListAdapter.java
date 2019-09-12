package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.ReportCategoryBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能:举报类型列表适配器
 */
public class ReportCategoryListAdapter extends AFinalRecyclerViewAdapter<ReportCategoryBean> {


    public ReportCategoryListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new ActiveAnchorListViewHoler(m_Inflater.inflate(R.layout.item_report_category, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((ActiveAnchorListViewHoler) holder).setContent(getItem(position), position);
    }

    public class ActiveAnchorListViewHoler extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_auto_text)
        TextView tv_auto_text;
        @BindView(R.id.llyt_root)
        LinearLayout llyt_root;

        public ActiveAnchorListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(ReportCategoryBean activeAnchorListBean, int position) {
            tv_auto_text.setText(activeAnchorListBean.getClassify_name());

            llyt_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener == null) {
                        return;
                    }
                    mOnItemClickListener.onItemClick(v,position,activeAnchorListBean);
                }
            });

        }
    }
}
