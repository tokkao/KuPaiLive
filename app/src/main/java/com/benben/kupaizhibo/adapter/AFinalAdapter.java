package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * AFinalAdapter的实现类 ,只需要实现getView方法即可
 *
 * @param <T>
 */
public abstract class AFinalAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected LayoutInflater m_Inflater;

	public AFinalAdapter(Context ctx) {
		this.mContext = ctx;
		m_Inflater = LayoutInflater.from(mContext);
	}

	private List<T> mList = new LinkedList<T>();


	/**
	 * 给当前的List复制
	 */
	public void againList(List<T> list) {
		this.mList=list;
		notifyDataSetChanged();
	}

	/**
	 * 获取当前List
	 *
	 * @return
	 */
	public List<T> getList() {
		return mList;
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
	 * 功能：添加一个对象
	 *
	 * @param t
	 */
	public void append(T t) {
		if (t == null) {
			return;
		}
		mList.add(t);
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
	 * 刷新list
	 */
	public void refreshList(List<T> list){
		if (mList==null){
			return;
		}
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}


	/**
	 * 清除List
	 */
	public void clear() {
		mList.clear();

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position > mList.size() - 1) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (position == getCount() - 1) {
			onReachBottom();
		}
		return getExView(position, convertView, parent);
	}

	protected abstract View getExView(int position, View convertView,
                                      ViewGroup parent);

	/**
	 * 滑动到底部了
	 */
	protected void onReachBottom() {

	}

}
