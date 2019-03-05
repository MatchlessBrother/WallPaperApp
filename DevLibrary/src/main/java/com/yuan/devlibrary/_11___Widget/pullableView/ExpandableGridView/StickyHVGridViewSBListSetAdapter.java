package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**简单的使用了包装器,包装实现了StickyGridHeadersSimpleAdapter适配器,然后把方法和主要装饰者定义的方法做链接.*/
public class StickyHVGridViewSBListSetAdapter<T> extends BaseAdapter implements StickyHVGridViewSAListAdapter
{
    protected static final String TAG = StickyHVGridViewSBListSetAdapter.class.getSimpleName();
    private List<T> mItems;
    private int mItemResId;
    private int mHeaderResId;
    private LayoutInflater mInflater;

    public StickyHVGridViewSBListSetAdapter(Context context, T[] items, int headerResId, int itemResId)
    {
        init(context, Arrays.asList(items), headerResId, itemResId);
    }
    
    public StickyHVGridViewSBListSetAdapter(Context context, List<T> items, int headerResId, int itemResId)
    {
        init(context, items, headerResId, itemResId);
    }

    private void init(Context context, List<T> items, int headerResId, int itemResId) 
    {
        this.mItems = items;
        this.mItemResId = itemResId;
        this.mHeaderResId = headerResId;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() 
    {
        return mItems.size();
    }

    public T getItem(int position) 
    {
        return mItems.get(position);
    }

    public long getItemId(int position) 
    {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        ViewHolder holder;
        if (convertView == null) 
        {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        else 
            holder = (ViewHolder)convertView.getTag();

        T item = getItem(position);
        if (item instanceof CharSequence) 
            holder.textView.setText((CharSequence)item);
    	else 
            holder.textView.setText(item.toString());
        return convertView;
    }
  
    public long getHeaderId(int position) 
    {
        T item = getItem(position);
        CharSequence value;
        if (item instanceof CharSequence) 
            value = (CharSequence)item;
        else 
            value = item.toString();
        return value.subSequence(0, 1).charAt(0);
    }

    public View getHeaderView(int position, View convertView, ViewGroup parent) 
    {
        HeaderViewHolder holder;
        if (convertView == null) 
        {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } 
        else 
            holder = (HeaderViewHolder)convertView.getTag();

        T item = getItem(position);
        CharSequence string;
        if (item instanceof CharSequence) 
            string = (CharSequence)item;
        else
            string = item.toString();
        holder.textView.setText(string.subSequence(0, 1));
        return convertView;
    }
    
    public boolean areAllItemsEnabled() 
    {
        return false;
    }
    
    protected class ViewHolder 
    {
        public TextView textView;
    }
    
    protected class HeaderViewHolder 
    {
        public TextView textView;
    }
}