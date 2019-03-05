package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SectionIndexer;

import java.util.List;

/**此控件是带有黏沾性ParentView头部的ExpandableListView,适配器建议实现StickyHeadersViewDExpandableListViewAdapter类**/
/**以便你快速,高效的实现适配器.使用此控件必须调用setDefaultHeaderViewClickListener(),否则点击父类标题不会有任何响应**/
/**(包括展开父标题/关闭父标题),当然你也可以通过调用setOnHeaderClickListener()设置自己实现的监听器,但是你实现的监听器*/
/**************必须继承OnHeaderClickListener类并调用其父类初始化函数,否则无法正常使用该控件,并且必报异常*************/
/**当此控件在展开父标题或则关闭父标题的时候,其实是支持实现动画流程的(用属性动画),通过实现IAnimationExecutor动画接口,*/
/**然后再调用setAnimExecutor()函数设置动画流程就可以了!亲,可以试试的哟!等我有时间了解清楚完属性动画后再来弄这个动画.*/
public class StickyHeadersViewCExpandableListView extends StickyHeadersViewBListView
{
    public final static int ANIMATION_EXPAND = 0;
    public final static int ANIMATION_COLLAPSE = 1;
    private IAnimationExecutor mDefaultAnimExecutor = new IAnimationExecutor()
    {
        public void executeAnim(View target, int animType)
        {
            if(animType == ANIMATION_EXPAND)
                target.setVisibility(VISIBLE);
            else if(animType == ANIMATION_COLLAPSE)
                target.setVisibility(GONE);
        }
    };
    private StickyHVCExpandableListViewAdapter mStickyHVCExpandableListViewAdapter;

    public StickyHeadersViewCExpandableListView(Context context) {
        super(context);
    }

    public StickyHeadersViewCExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyHeadersViewCExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public StickyHVCExpandableListViewAdapter getAdapter() {return mStickyHVCExpandableListViewAdapter;}

    private void animateView(final View target, final int type)
    {
        if(ANIMATION_EXPAND == type && target.getVisibility() == View.VISIBLE)
            return;
        if(ANIMATION_COLLAPSE == type && target.getVisibility() != VISIBLE)
            return;
        if(mDefaultAnimExecutor != null)
        {
            mDefaultAnimExecutor.executeAnim(target,type);
        }
    }

    public void setAdapter(StickyHVAdapter adapter)
    {
        if(adapter instanceof SectionIndexer)
            mStickyHVCExpandableListViewAdapter = new StickyHVDSIExpandableListViewAdapter(adapter);
        else
            mStickyHVCExpandableListViewAdapter = new StickyHVCExpandableListViewAdapter(adapter);
        super.setAdapter(mStickyHVCExpandableListViewAdapter);
        if(adapter instanceof StickyHeadersViewDExpandableListViewAdapter)
            ((StickyHeadersViewDExpandableListViewAdapter)adapter).collapseAllGroupView(this);
    }

    public boolean isHeaderCollapsed(long headerId)
    {
        return  mStickyHVCExpandableListViewAdapter.isHeaderCollapsed(headerId);
    }

    public View findViewByItemId(long itemId)
    {
        return mStickyHVCExpandableListViewAdapter.findViewByItemId(itemId);
    }

    public long findItemIdByView(View view)
    {
        return mStickyHVCExpandableListViewAdapter.findItemIdByView(view);
    }

    public void collapse(long headerId)
    {
        if(mStickyHVCExpandableListViewAdapter.isHeaderCollapsed(headerId)) {return;}
        mStickyHVCExpandableListViewAdapter.collapse(headerId);
        List<View> itemViews = mStickyHVCExpandableListViewAdapter.getItemViewsByHeaderId(headerId);
        if(itemViews == null)
            return;
        for (View view : itemViews)
            animateView(view, ANIMATION_COLLAPSE);
    }

    public void expand(long headerId)
    {
        if(!mStickyHVCExpandableListViewAdapter.isHeaderCollapsed(headerId)) {return;}
        mStickyHVCExpandableListViewAdapter.expand(headerId);
        List<View> itemViews = mStickyHVCExpandableListViewAdapter.getItemViewsByHeaderId(headerId);
        if(itemViews == null)
            return;
        for (View view : itemViews)
            animateView(view, ANIMATION_EXPAND);
    }

    public interface IAnimationExecutor
    {
        public void executeAnim(View target, int animType);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setAnimExecutor(IAnimationExecutor animExecutor)
    {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            mDefaultAnimExecutor = animExecutor;
    }

    public void setDefaultHeaderViewClickListener(StickyHeadersViewCExpandableListView listView)
    {
        listView.setOnHeaderClickListener(new OnHeaderClickListener());
    }

    public static class OnHeaderClickListener implements StickyHeadersViewBListView.OnHeaderClickListener
    {
        public void onHeaderClick(StickyHeadersViewBListView listView, View header, int itemPosition, long headerId, boolean currentlySticky)
        {
            if(listView instanceof StickyHeadersViewCExpandableListView)
            {
                StickyHeadersViewCExpandableListView newListView = (StickyHeadersViewCExpandableListView) listView;
                {
                    if(!((StickyHeadersViewAObject)newListView.getAdapter().getItem(itemPosition)).getIsRealData())
                        newListView.collapse(headerId);
                    else
                    {
                        if (newListView.isHeaderCollapsed(headerId))
                            newListView.expand(headerId);
                        else
                            newListView.collapse(headerId);
                        if (currentlySticky)
                            newListView.setSelection(itemPosition);
                    }
                }
            }
        }
    }
}