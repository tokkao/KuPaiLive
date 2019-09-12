package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.AuthImageBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * author：luck
 * project：LeTuGolf
 * package：com.tongyu.luck.paradisegolf.adapter
 * email：893855882@qq.com
 * data：16/7/27
 */
public class GridImageListAdapter extends
        RecyclerView.Adapter<GridImageListAdapter.ViewHolder> {
    public final int TYPE_CAMERA = 1;
    public final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<AuthImageBean> list = new ArrayList<>();
    private int selectMax = 9;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;
    private boolean isAuth;
    private int status = -1;

    public interface onAddPicClickListener {
        void onAddPicClick(int type, int position);
    }

    public GridImageListAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setDefaultMaxNum(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<AuthImageBean> list, boolean isAuth, int status) {
        this.list = list;
        this.isAuth = isAuth;
        this.status = status;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        LinearLayout ll_del;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.fiv);
            ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(viewHolder.getAdapterPosition(), v);
                }
            });
        }
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setVisibility(status == 0 || status == 1? View.INVISIBLE : View.VISIBLE);
            viewHolder.mImg.setImageResource(R.mipmap.icon_photo_add);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //审核中 不可操作
                    mOnAddPicClickListener.onAddPicClick(0, viewHolder.getAdapterPosition());
                }
            });
            viewHolder.ll_del.setVisibility(View.INVISIBLE);
        } else {
            //审核中 不可操作
            viewHolder.ll_del.setVisibility(status == 0 || status == 1? View.GONE : View.VISIBLE);
            viewHolder.ll_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnAddPicClickListener.onAddPicClick(1, viewHolder.getAdapterPosition());
                }
            });
            Glide.with(mContext)
                    .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.image_placeholder).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .load(isAuth ? list.get(position).getPath() : new File(list.get(position).getPath()))
                    .into(viewHolder.mImg);

        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
