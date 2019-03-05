package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public interface StickyHVAdapter extends ListAdapter
{
	long getHeaderId(int position);

	View getHeaderView(int position, View convertView, ViewGroup parent);
}