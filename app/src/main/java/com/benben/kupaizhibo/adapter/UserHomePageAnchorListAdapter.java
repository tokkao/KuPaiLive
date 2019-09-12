package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.MyFansFollowListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 用户主页主播列表adapter
 */
public class UserHomePageAnchorListAdapter extends AFinalRecyclerViewAdapter<MyFansFollowListBean.DataBean> {


    private static final String TAG ="UserHomePageAnchorListAdapter" ;

    public UserHomePageAnchorListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new UserHomePageAnchorListViewHolder(m_Inflater.inflate(R.layout.item_home_page_follow_anchor, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((UserHomePageAnchorListViewHolder) holder).setContent(getItem(position), position);
    }

    public class UserHomePageAnchorListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.civ_user_avatar)
        CircleImageView civUserAvatar;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        View itemView;

        public UserHomePageAnchorListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(MyFansFollowListBean.DataBean mUserHomePageAnchorListBean, int position) {
            tvUserName.setText(mUserHomePageAnchorListBean.getNickname());
            ImageUtils.getPic(mUserHomePageAnchorListBean.getAvatar(), civUserAvatar, m_Context, R.mipmap.icon_default_avatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(view,position,mUserHomePageAnchorListBean);
                    }
                }
            });

        }
    }
}
