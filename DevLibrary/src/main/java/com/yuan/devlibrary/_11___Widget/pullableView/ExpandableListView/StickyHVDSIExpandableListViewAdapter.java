package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import android.content.Context;
import android.widget.SectionIndexer;

public class StickyHVDSIExpandableListViewAdapter extends StickyHVCExpandableListViewAdapter implements SectionIndexer
{
    Context mContext;
    SectionIndexer mSectionIndexerDelegate;
    StickyHVDSIExpandableListViewAdapter(StickyHVAdapter sectionIndexerDelegate)
    {
        super(sectionIndexerDelegate);
        mSectionIndexerDelegate = (SectionIndexer) sectionIndexerDelegate;
    }

    StickyHVDSIExpandableListViewAdapter(Context context, StickyHVAdapter sectionIndexerDelegate)
    {
        super(context, sectionIndexerDelegate);
        mContext = context;
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