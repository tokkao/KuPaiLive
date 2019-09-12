package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.socket.GiftInfoBean;

import java.util.List;

/**
 * 礼物信息适配器
 * create by zjn on 2019/5/17 0017
 * email:168455992@qq.com
 */
public class GiftGridViewAdapter extends BaseAdapter {

    private List<GiftInfoBean> mDatas;
    private LayoutInflater inflater;
    private Context mContext;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 8;

    public GiftGridViewAdapter(Context context, List<GiftInfoBean> mDatas, int curIndex) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.curIndex = curIndex;
        this.mContext = context;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
    }

    @Override
    public GiftInfoBean getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gift_gride_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivGift = convertView.findViewById(R.id.iv_gift);
            viewHolder.tvGiftName = convertView.findViewById(R.id.tv_gift_name);
            viewHolder.rlyt_root = convertView.findViewById(R.id.rlyt_root);
            viewHolder.iv_xz = convertView.findViewById(R.id.iv_xz);
            viewHolder.tvBobi = convertView.findViewById(R.id.tv_bobi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize，
         */
        GiftInfoBean model = getItem(position);

        ImageUtils.getPic(model.getIcon(),viewHolder.ivGift,mContext);
        viewHolder.tvGiftName.setText(model.getName()+"");
        viewHolder.tvBobi.setText("钻石："+model.getMoney());
        if (model.isSelect()){//被选中
            viewHolder.rlyt_root.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selector_gift_bg));
            viewHolder.iv_xz.setVisibility(View.VISIBLE);
        }else {
            viewHolder.rlyt_root.setBackgroundDrawable(null);
            viewHolder.iv_xz.setVisibility(View.GONE);
        }
        return convertView;
    }


    class ViewHolder {
        public ImageView ivGift;
        public ImageView iv_xz;
        public RelativeLayout rlyt_root;
        public TextView tvGiftName;
        public TextView tvBobi;
    }
}
