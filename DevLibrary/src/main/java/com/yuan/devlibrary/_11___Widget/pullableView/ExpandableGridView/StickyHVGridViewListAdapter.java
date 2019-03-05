package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/*******************这个控件中最主要的装饰者接口***************/
public interface StickyHVGridViewListAdapter extends ListAdapter
{
    public int getNumHeaders();   
    public int getCountForHeader(int header);
    View getHeaderView(int position, View convertView, ViewGroup parent);
}