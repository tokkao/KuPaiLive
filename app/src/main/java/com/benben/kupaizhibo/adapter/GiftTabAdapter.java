package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.LiveTopicTagBean;


//首页-推荐-tab导航 子item
public class GiftTabAdapter extends AFinalRecyclerViewAdapter<LiveTopicTagBean> {
    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public GiftTabAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(m_Inflater.inflate(R.layout.item_gift_tab,
                parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder baseRecyclerViewHolder, int position) {
        MyViewHolder holder = (MyViewHolder) baseRecyclerViewHolder;
        holder.tvTabTitle.setText(getList().get(position).getTitle());
        if(getList().get(position).isSelect()){
            holder.tvTabTitle.setTextSize(15);
            holder.tvTabTitle.getPaint().setFakeBoldText(true);
        }else{
            holder.tvTabTitle.setTextSize(14);
            holder.tvTabTitle.getPaint().setFakeBoldText(false);
        }
        holder.llytRoot.setTag(position);
        holder.llytRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getList().size(); i++) {
                    getList().get(i).setSelect(false);
                }
                getList().get(position).setSelect(true);
                if(onButtonClickListener == null){
                    return;
                }
                onButtonClickListener.onClickTab(getList().get(position),position);
            }
        });

    }

    public interface OnButtonClickListener {
        void onClickTab(LiveTopicTagBean dataBean, int position);
    }

    class MyViewHolder extends BaseRecyclerViewHolder {

        TextView tvTabTitle;
        RelativeLayout llytRoot;

        public MyViewHolder(View view) {
            super(view);
            tvTabTitle = view.findViewById(R.id.tv_tab_title);
            llytRoot = view.findViewById(R.id.llyt_root);

        }
    }
}
