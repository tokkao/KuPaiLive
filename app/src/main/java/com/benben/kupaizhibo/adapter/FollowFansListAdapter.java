package com.benben.kupaizhibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.MyFansFollowListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by: wanghk 2019-07-10.
 * Describe: 关注/粉丝列表adapter
 */
public class FollowFansListAdapter extends AFinalRecyclerViewAdapter<MyFansFollowListBean.DataBean> {

    public OnFollowClickListener mOnFollowClickListener;
    private static final String TAG = "FollowFansListAdapter";

    private int mType;
    private Activity mActivity;
    public FollowFansListAdapter(Context ctx, int type) {
        super(ctx);
        this.mActivity = (Activity) ctx;
        this.mType = type;
    }

    // 点击事件接口
    public interface OnFollowClickListener {
        void onFollowClick(View view, int position, MyFansFollowListBean.DataBean model);

    }

    public void setOnFollowClickListener(OnFollowClickListener listener) {
        this.mOnFollowClickListener = listener;
    }
    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        if (mType == 0 || mType == 1) { //他人的关注/粉丝
            return new OtherFollowFansListViewHolder(m_Inflater.inflate(R.layout.item_follow_fans_list, parent, false));

        } else {  //我的关注/粉丝
            return new MyFollowFansListViewHolder(m_Inflater.inflate(R.layout.item_my_follow_fans_list, parent, false));
        }
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (mType == 0 || mType == 1) {
            ((OtherFollowFansListViewHolder) holder).setContent(getItem(position), position);
        } else {
            ((MyFollowFansListViewHolder) holder).setContent(getItem(position), position);
        }

    }


    public class OtherFollowFansListViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_id)
        TextView tvId;

        View itemView;

        public OtherFollowFansListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(MyFansFollowListBean.DataBean dataBean, int position) {

            tvName.setText(dataBean.getNickname());
            tvId.setText(m_Context.getResources().getString(R.string.user_id, String.valueOf(dataBean.getUid())));
            ImageUtils.getPic(dataBean.getAvatar(), civAvatar, m_Context, R.mipmap.icon_default_avatar);
        }
    }

    public class MyFollowFansListViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_follow_status)
        TextView tvFollowStatus;
        @BindView(R.id.rlyt_followed)
        RelativeLayout rlytFollowed;
        View itemView;

        public MyFollowFansListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(MyFansFollowListBean.DataBean dataBean, int position) {

            tvName.setText(dataBean.getNickname());
            tvFollowStatus.setText(dataBean.getIs_follow() == 0 ? m_Context.getResources().getString(R.string.add_follow) : m_Context.getResources().getString(R.string.followed));
            tvFollowStatus.setTextColor(dataBean.getIs_follow() == 0 ? m_Context.getResources().getColor(R.color.color_FFFFFF) : m_Context.getResources().getColor(R.color.color_666666));
            rlytFollowed.setBackgroundResource(dataBean.getIs_follow() == 0 ? R.drawable.shape_red_radius13_ff6261 : R.drawable.shape_grey_radius13_f2f2f2);
            ImageUtils.getPic(dataBean.getAvatar(), civAvatar, m_Context, R.mipmap.icon_default_avatar);
            rlytFollowed.setOnClickListener(view -> {
               if(mOnFollowClickListener != null){
                   mOnFollowClickListener.onFollowClick(view,position,dataBean);
               }

            });


        }
    }



}
