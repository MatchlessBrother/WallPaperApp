package com.yuan.devlibrary._11___Widget.pullableView.RecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

public class BGLItemDecoration extends RecyclerView.ItemDecoration
{
    private Context mContext;
    private int mOrientation;
    private Drawable mDividerDrawable;
    public  static final int GLM_VERTICAL   = GridLayoutManager.VERTICAL;
    public  static final int GLM_HORIZONTAL = GridLayoutManager.HORIZONTAL;
    private static final int DIVIDER_ATTRS[] = new int[]{android.R.attr.listDivider};

    public BGLItemDecoration(Context context, int orientation)
    {
        mContext = context;
        setOrientation(orientation);
        TypedArray typedArray = mContext.obtainStyledAttributes(DIVIDER_ATTRS);
        mDividerDrawable  =  typedArray.getDrawable(0);   typedArray.recycle();
    }

    /*********设置当前视图的分割线方向********/
    public void setOrientation(int orientation)
    {
        if (orientation != GLM_VERTICAL && orientation != GLM_HORIZONTAL)
        {
            throw new IllegalArgumentException("Be Careful:Invalid Orientation");
        }
        mOrientation = orientation;
    }

    /**************设置当前视图的分割线Drawable************/
    public void setDividerDrawable(Drawable dividerDrawable)
    {
        if(dividerDrawable != null)
            mDividerDrawable = dividerDrawable;
    }

    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state)
    {
        drawVerticalDecoration(canvas,recyclerView);
        drawHorizontalDecoration(canvas, recyclerView);
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        super.onDrawOver(c, parent, state);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state)
    {
        int childCount = recyclerView.getAdapter().getItemCount();
        int spanCount = ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        int position  = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        if(isLastRaw(recyclerView,position, spanCount, childCount) && isLastColumn(recyclerView, position, spanCount, childCount))
            outRect.set(0, 0, 0, 0);
        else if(isLastRaw(recyclerView,position, spanCount, childCount))
            outRect.set(0, 0, mDividerDrawable.getIntrinsicWidth(), 0);
        else if(isLastColumn(recyclerView, position, spanCount, childCount))
            outRect.set(0, 0, 0, mDividerDrawable.getIntrinsicHeight());
        else
            outRect.set(0, 0, mDividerDrawable.getIntrinsicWidth(), mDividerDrawable.getIntrinsicHeight());
    }

    /**********************关于Item之间垂直方向上的分割线**********************/
    public void drawVerticalDecoration(Canvas canvas, RecyclerView recyclerView)
    {
        final int childCount = recyclerView.getChildCount();
        for(int index = 0;index < childCount;index++)
        {
            final View view = recyclerView.getChildAt(index);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)view.getLayoutParams();
            final int left = view.getRight() + params.rightMargin;
            final int top =  view.getTop() - params.topMargin;
            final int right = left + mDividerDrawable.getIntrinsicWidth();
            final int bottom = view.getBottom() + params.bottomMargin;
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(canvas);
        }
    }

    /**********************关于Item之间水平方向上的分割线**********************/
    public void drawHorizontalDecoration(Canvas canvas, RecyclerView recyclerView)
    {
        final int childCount = recyclerView.getChildCount();
        for(int index = 0;index < childCount;index++)
        {
            final View view = recyclerView.getChildAt(index);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)view.getLayoutParams();
            final int left = view.getLeft() - params.leftMargin;
            final int top =  view.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerDrawable.getIntrinsicHeight();
            final int right = view.getRight() + params.rightMargin + mDividerDrawable.getIntrinsicWidth();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(canvas);
        }
    }

    /******************************判定当前位置的Item是否处于最后一行******************************/
    public boolean isLastRaw(RecyclerView recyclerView,int position,int spanCount,int childCount)
    {
        if(mOrientation == GLM_HORIZONTAL)
        {
            if((position + 1) % spanCount == 0)
                return true;
            return false;
        }
        else
        {
            if(childCount % spanCount == 0)
            {
                childCount = childCount - spanCount;
                if(position >= childCount)
                    return true;
            }
            else
            {
                childCount = childCount - childCount % spanCount;
                if(position >= childCount)
                    return true;
            }
            return false;
        }
    }

    /******************************判定当前位置的Item是否处于最后一列******************************/
    public boolean isLastColumn(RecyclerView recyclerView,int position,int spanCount,int childCount)
    {
        if(mOrientation == GLM_HORIZONTAL)
        {
            if(childCount % spanCount == 0)
            {
                childCount = childCount - spanCount;
                if(position >= childCount)
                    return true;
            }
            else
            {
                childCount = childCount - childCount % spanCount;
                if(position >= childCount)
                    return true;
            }
            return false;
        }
        else
        {
            if((position + 1) % spanCount == 0)
                return true;
            return false;
        }
    }
}