package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.ViewerListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-02.
 * Describe: 观众列表adapter
 */
public class ViewerListAdapter extends AFinalRecyclerViewAdapter<ViewerListBean.ListsBean> {



    public ViewerListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new ViewerListViewHolder(m_Inflater.inflate(R.layout.item_viewer_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((ViewerListViewHolder) holder).setContent(getItem(position), position);
    }

    public class ViewerListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_guard_name)
        TextView tvGuardName;
        @BindView(R.id.civ_user_avatar)
        CircleImageView civUserAvatar;

        View itemView;

        public ViewerListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(ViewerListBean.ListsBean mViewerListBean, int position) {
            tvUserName.setText(mViewerListBean.getUser_nickname());
            if(mViewerListBean.getGuard_type() == 0){
                tvGuardName.setText(m_Context.getResources().getString(R.string.gold));

            }else if(mViewerListBean.getGuard_type() == 1){
                tvGuardName.setText(m_Context.getResources().getString(R.string.silver));

            }else {
                tvGuardName.setVisibility(View.GONE);
            }
            ImageUtils.getPic(mViewerListBean.getAvatar(),civUserAvatar,m_Context,R.mipmap.icon_default_header);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, position, mViewerListBean);
                    }
                }
            });
        }
    }

}
