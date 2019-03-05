package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**************普通的adapter的包装器,只是为了连接上这个设计模式,让该控件也能使用普通的adapter*********/
public class StickyHVGridViewListBaseAdapter extends BaseAdapter implements StickyHVGridViewListAdapter
{
    private ListAdapter mDelegate;

    private DataSetObserver mDataSetObserver = new DataSetObserver()
    {
        public void onChanged()
        {
            notifyDataSetChanged();
        }

        public void onInvalidated()
        {
            notifyDataSetInvalidated();
        }
    };

    public StickyHVGridViewListBaseAdapter(ListAdapter adapter)
    {
        mDelegate = adapter;
        if (adapter != null)
            adapter.registerDataSetObserver(mDataSetObserver);
    }

    public int getCount()
    {
        if (mDelegate == null)
            return 0;
        return mDelegate.getCount();
    }

    public long getItemId(int position)
    {
        return mDelegate.getItemId(position);
    }

    public Object getItem(int position)
    {
        if (mDelegate == null)
            return null;
        return mDelegate.getItem(position);
    }

    public int getViewTypeCount()
    {
        return mDelegate.getViewTypeCount();
    }

    public int getItemViewType(int position)
    {
        return mDelegate.getItemViewType(position);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        return mDelegate.getView(position, convertView, parent);
    }

    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }

    public int getNumHeaders()
    {
        return 0;
    }

    public boolean hasStableIds()
    {
        return mDelegate.hasStableIds();
    }

    public int getCountForHeader(int header)
    {
        return 0;
    }
}