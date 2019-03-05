package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**************************已经简单实现的一个adapter,重写这个方法可以实现布局的变换.************************/
public class StickyHVGridViewSCALListArrayAdapter extends BaseAdapter implements StickyHVGridViewListAdapter
{
    private HeaderData[] mHeaders;
    private StickyHVGridViewSAListAdapter mDelegate;

    public StickyHVGridViewSCALListArrayAdapter(StickyHVGridViewSAListAdapter adapter)
    {
        mDelegate = adapter;
        mHeaders = generateHeaderList(adapter);
        adapter.registerDataSetObserver(new DataSetObserverExtension());
    }

    public int getCount()
    {
        return mDelegate.getCount();
    }

    public Object getItem(int position)
    {
        return mDelegate.getItem(position);
    }

    public long getItemId(int position)
    {
        return mDelegate.getItemId(position);
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
        return mDelegate.getHeaderView(mHeaders[position].getRefPosition(), convertView, parent);
    }

    public boolean hasStableIds()
    {
        return mDelegate.hasStableIds();
    }

    public int getNumHeaders()
    {
        return mHeaders.length;
    }

    public int getCountForHeader(int position)
    {
        return mHeaders[position].getCount();
    }

    protected HeaderData[] generateHeaderList(StickyHVGridViewSAListAdapter adapter)
    {
        Map<Long, HeaderData> mapping = new HashMap<Long, HeaderData>();
        List<HeaderData> headers = new ArrayList<HeaderData>();

        for (int i = 0; i < adapter.getCount(); i++)
        {
            long headerId = adapter.getHeaderId(i);
            HeaderData headerData = mapping.get(headerId);
            if (headerData == null)
            {
                headerData = new HeaderData(i);
                headers.add(headerData);
            }
            headerData.incrementCount();
            mapping.put(headerId, headerData);
        }
        return headers.toArray(new HeaderData[headers.size()]);
    }

    private class HeaderData
    {
        private int mCount;
        private int mRefPosition;

        public HeaderData(int refPosition)
        {
            mCount = 0;
            mRefPosition = refPosition;
        }

        public int getCount()
        {
            return mCount;
        }

        public void incrementCount()
        {
            mCount++;
        }

        public int  getRefPosition()
        {
            return mRefPosition;
        }
    }

    private final class DataSetObserverExtension extends DataSetObserver
    {
        public void onChanged()
        {
            mHeaders = generateHeaderList(mDelegate);
            notifyDataSetChanged();
        }

        public void onInvalidated()
        {
            mHeaders = generateHeaderList(mDelegate);
            notifyDataSetInvalidated();
        }
    }
}