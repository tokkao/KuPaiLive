package com.benben.kupaizhibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

/**
 * AFinalAdapter的实现类 ,只需要实现getView方法即可
 *
 * @param <T>
 */
public abstract class AFinalRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected Context m_Context;
    protected LayoutInflater m_Inflater;
    public OnItemClickListener<T> mOnItemClickListener;
    protected Activity m_Activity;

    public AFinalRecyclerViewAdapter(Context ctx) {
        this.m_Context = ctx.getApplicationContext();
        m_Inflater = LayoutInflater.from(m_Context);
    }

    public AFinalRecyclerViewAdapter(Activity ctx) {
        this.m_Activity = ctx;
        m_Inflater = LayoutInflater.from(m_Activity);
    }

    private List<T> mList = new LinkedList<T>();

    /**
     * 获取当前List
     *
     * @return
     */
    public List<T> getList() {
        return mList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mList.size() > 0) {
            count = mList.size();
        }
        return count;
    }

    /**
     * position
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        if (position > mList.size() - 1) {
            return null;
        }
        return mList.get(position);
    }


    public void refreshList(List<T> list) {
        if (mList == null) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 添加一个List
     *
     * @param list
     */
    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加一个List到第一个
     *
     * @param list
     */
    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
        notifyDataSetChanged();
    }

    /**
     * 清除List
     */
    public void clear() {
        if (mList != null) {
            mList.clear();
        }

        notifyDataSetChanged();
    }

    public abstract static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateCustomerViewHolder(parent, viewType);
    }

    //重写创建viewholder 代码
    protected abstract BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        onBindCustomerViewHolder(holder, position);
    }

    //重写个viewholder 赋值方法
    protected abstract void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position);

    // 点击事件接口
    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T model);

        void onItemLongClick(View view, int position, T model);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }
}