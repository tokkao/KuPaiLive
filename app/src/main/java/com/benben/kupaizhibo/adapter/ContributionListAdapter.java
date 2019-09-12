package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.RankBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-02.
 * Describe: 贡献列表adapter
 */
public class ContributionListAdapter extends AFinalRecyclerViewAdapter<RankBean> {


    public ContributionListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new ContributionListViewHolder(m_Inflater.inflate(R.layout.item_contribution_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((ContributionListViewHolder) holder).setContent(getItem(position), position);
    }

    public class ContributionListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_ranking)
        TextView tvRanking;
        @BindView(R.id.iv_ranking)
        ImageView ivRanking;
        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_contribution_value)
        TextView tvContributionValue;
        View itemView;

        public ContributionListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(RankBean mContributionListBean, int position) {

            if (position == 0 && getItemCount() >= 1) {
                tvRanking.setVisibility(View.GONE);
                ivRanking.setVisibility(View.VISIBLE);
                ivRanking.setImageResource(R.mipmap.icon_ranking_first);
            } else if (position == 1 && getItemCount() >= 2) {
                tvRanking.setVisibility(View.GONE);
                ivRanking.setVisibility(View.VISIBLE);
                ivRanking.setImageResource(R.mipmap.icon_ranking_second);
            } else if (position == 2 && getItemCount() >= 3) {
                tvRanking.setVisibility(View.GONE);
                ivRanking.setVisibility(View.VISIBLE);
                ivRanking.setImageResource(R.mipmap.icon_ranking_third);
            } else {
                tvRanking.setVisibility(View.VISIBLE);
                ivRanking.setVisibility(View.GONE);
                tvRanking.setText(String.valueOf(position + 1));

            }

            tvName.setText(mContributionListBean.getNickname());
            tvContributionValue.setText(m_Context.getResources().getString(R.string.contribution_value, String.valueOf(mContributionListBean.getScore())));
            ImageUtils.getPic(mContributionListBean.getAvatar(), civAvatar, m_Context, R.mipmap.icon_default_header);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, position, mContributionListBean);
                    }
                }
            });
        }
    }

}
