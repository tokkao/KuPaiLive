package com.benben.kupaizhibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.bean.FilterableBean;


/**
 * Author:zhn
 * Time:2019/5/9 10:08
 *
 * AutoCompleteTextView 实现下拉选效果
 *
 */
public class AutoSpinnerAdapter<T extends FilterableBean> extends AFinalAdapter<T> implements Filterable {

    public AutoSpinnerAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public Filter getFilter() {
        return new FullStringFilter();
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = m_Inflater.inflate(R.layout.item_auto_layout, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.tv_auto_text);
            convertView.setTag(viewHolder);//将ViewHolder存储在View中
        } else {
            viewHolder = (ViewHolder) convertView.getTag();//重获取viewHolder
        }

        T data= (T) getItem(position);
        viewHolder.textView.setText(data.getFilterString());
        return convertView;
    }


    private class ViewHolder {
        TextView textView;
    }

    //不过滤,默认返回所有的选项
    private class FullStringFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        }
    }

}
