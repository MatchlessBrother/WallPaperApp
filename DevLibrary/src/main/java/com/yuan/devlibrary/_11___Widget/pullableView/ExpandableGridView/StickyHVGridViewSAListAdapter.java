package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/***********定义了两个方法,为主要装饰者接口的部分实现************/
public interface StickyHVGridViewSAListAdapter extends ListAdapter
{
    public long getHeaderId(int position);
    View getHeaderView(int position, View convertView, ViewGroup parent);
}