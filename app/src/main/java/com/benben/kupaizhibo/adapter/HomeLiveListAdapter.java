package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.LiveInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-02.
 * Describe: 首页-直播列表adapter
 */
public class HomeLiveListAdapter extends AFinalRecyclerViewAdapter<LiveInfoBean> {

    public HomeLiveListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new HomeLiveListViewHolder(m_Inflater.inflate(R.layout.item_home_live_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((HomeLiveListViewHolder) holder).setContent(getItem(position), position);
    }

    public class HomeLiveListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_live_face_pic)
        ImageView ivLiveFacePic;
        @BindView(R.id.tv_live_title)
        TextView tvLiveTitle;
        @BindView(R.id.civ_user_photo)
        CircleImageView civUserPhoto;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.iv_play_icon)
        ImageView ivPlayIcon;
        @BindView(R.id.tv_bofang_cishu)
        TextView tvBofangCishu;
        View itemView;

        public HomeLiveListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(LiveInfoBean mLiveInfoBean, int position) {
            ImageUtils.getPic(mLiveInfoBean.getThumb(), ivLiveFacePic, m_Context,R.mipmap.banner_default);
            ImageUtils.getPic(mLiveInfoBean.getAvatar(), civUserPhoto, m_Context,R.mipmap.icon_default_avatar);
            tvLiveTitle.setText(mLiveInfoBean.getTitle());
            tvUserName.setText(mLiveInfoBean.getNickname());
            tvBofangCishu.setText(mLiveInfoBean.getNumber());

            itemView.setOnClickListener(view -> {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(view,position,mLiveInfoBean);
                }
            });
        }
    }
}
