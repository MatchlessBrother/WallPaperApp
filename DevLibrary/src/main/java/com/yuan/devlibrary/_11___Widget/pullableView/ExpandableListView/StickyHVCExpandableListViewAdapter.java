package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class StickyHVCExpandableListViewAdapter extends BaseAdapter implements StickyHVAdapter
{
    private Context mContext;
    private final StickyHVAdapter mStickyHVAdapter;
    private List<Long> mCollapseHeaderIds = new ArrayList<Long>();
    private MutualHashMap<View,Long> mViewToItemIdMap = new MutualHashMap<View, Long>();
    private OneToManyHashMap<Integer,View> mHeaderIdToViewMap = new OneToManyHashMap<Integer, View>();

    StickyHVCExpandableListViewAdapter(StickyHVAdapter stickyHVAdapter)
    {
        mStickyHVAdapter = stickyHVAdapter;
    }

    StickyHVCExpandableListViewAdapter(Context context, StickyHVAdapter stickyHVAdapter)
    {
        mContext = context;
        mStickyHVAdapter = stickyHVAdapter;
    }

    public int getCount()
    {
        return mStickyHVAdapter.getCount();
    }

    public Object getItem(int i)
    {
        return mStickyHVAdapter.getItem(i);
    }

    public long getItemId(int i)
    {
        return mStickyHVAdapter.getItemId(i);
    }

    public boolean hasStableIds()
    {
        return mStickyHVAdapter.hasStableIds();
    }

    public int getViewTypeCount()
    {
        return mStickyHVAdapter.getViewTypeCount();
    }

    public int getItemViewType(int i)
    {
        return mStickyHVAdapter.getItemViewType(i);
    }

    public boolean isEnabled(int i)
    {
        return mStickyHVAdapter.isEnabled(i);
    }

    public boolean areAllItemsEnabled()
    {
        return mStickyHVAdapter.areAllItemsEnabled();
    }

    public long getHeaderId(int position)
    {
        return mStickyHVAdapter.getHeaderId(position);
    }

    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        return mStickyHVAdapter.getHeaderView(position, convertView, parent);
    }

    public boolean isHeaderCollapsed(long headerId)
    {
        return mCollapseHeaderIds.contains(headerId);
    }

    public List<View> getItemViewsByHeaderId(long headerId)
    {
        return mHeaderIdToViewMap.get((int) headerId);
    }

    public void expand(long headerId)
    {
        if(isHeaderCollapsed(headerId))
            mCollapseHeaderIds.remove((Object) headerId);
    }

    public void collapse(long headerId)
    {
        if(!isHeaderCollapsed(headerId))
            mCollapseHeaderIds.add(headerId);
    }

    public View getView(int index, View view, ViewGroup viewGroup)
    {
        View convertView = mStickyHVAdapter.getView(index,view,viewGroup);
        mViewToItemIdMap.put(convertView, getItemId(index));
        mHeaderIdToViewMap.add((int) getHeaderId(index), convertView);
        if(mCollapseHeaderIds.contains(getHeaderId(index)))
            convertView.setVisibility(View.GONE);
        else
            convertView.setVisibility(View.VISIBLE);
        return convertView;
    }

    public long findItemIdByView(View view)
    {
        return mViewToItemIdMap.getValue(view);
    }

    public View findViewByItemId(long itemId)
    {
        return mViewToItemIdMap.getKey(itemId);
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver)
    {
        mStickyHVAdapter.registerDataSetObserver(dataSetObserver);
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
    {
        mStickyHVAdapter.unregisterDataSetObserver(dataSetObserver);
    }

    public boolean isEmpty()
    {
        return mStickyHVAdapter.isEmpty();
    }
}