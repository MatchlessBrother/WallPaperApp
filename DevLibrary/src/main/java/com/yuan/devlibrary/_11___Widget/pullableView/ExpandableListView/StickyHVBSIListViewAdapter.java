package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import android.content.Context;
import android.widget.SectionIndexer;

public class StickyHVBSIListViewAdapter extends StickyHVAListViewAdapter implements SectionIndexer
{
	SectionIndexer mSectionIndexerDelegate;
	StickyHVBSIListViewAdapter(Context context, StickyHVAdapter sectionIndexerDelegate)
	{
		super(context, sectionIndexerDelegate);
		mSectionIndexerDelegate = (SectionIndexer) sectionIndexerDelegate;
	}

	public int getPositionForSection(int section)
	{
		return mSectionIndexerDelegate.getPositionForSection(section);
	}

	public int getSectionForPosition(int position)
	{
		return mSectionIndexerDelegate.getSectionForPosition(position);
	}

	public Object[] getSections()
	{
		return mSectionIndexerDelegate.getSections();
	}
}