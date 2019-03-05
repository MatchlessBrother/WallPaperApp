package com.yuan.devlibrary._11___Widget.pullableView.RecyclerView;

import android.view.View;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

public class ALLItemDecoration extends RecyclerView.ItemDecoration
{
    private Context mContext;
    private int mOrientation;
    private Drawable mDividerDrawable;
    public  static final int LLM_VERTICAL   = LinearLayoutManager.VERTICAL;
    public  static final int LLM_HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    private static final int DIVIDER_ATTRS[] = new int[]{android.R.attr.listDivider};

    public ALLItemDecoration(Context context, int orientation)
    {
        mContext = context;
        setOrientation(orientation);
        TypedArray typedArray = mContext.obtainStyledAttributes(DIVIDER_ATTRS);
        mDividerDrawable  =  typedArray.getDrawable(0);   typedArray.recycle();
    }

    /*********设置当前视图的分割线方向********/
    public void setOrientation(int orientation)
    {
        if (orientation != LLM_VERTICAL && orientation != LLM_HORIZONTAL)
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
        if(mOrientation == LLM_HORIZONTAL)
            drawHorizontalDecoration(canvas, recyclerView);
        else
            drawVerticalDecoration(canvas,recyclerView);
    }

    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state)
    {
        super.onDrawOver(canvas, parent, state);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state)
    {
        if(((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition() != recyclerView.getAdapter().getItemCount() - 1)
        {
            if(mOrientation == LLM_HORIZONTAL)
                outRect.set(0, 0, mDividerDrawable.getIntrinsicWidth(), 0);
            else
                outRect.set(0, 0, 0, mDividerDrawable.getIntrinsicHeight());
        }
        else
            outRect.set(0,0,0,0);
    }

    /**********************关于Item之间垂直方向上的分割线**********************/
    public void drawVerticalDecoration(Canvas canvas, RecyclerView recyclerView)
    {
        final int left = recyclerView.getPaddingLeft();
        final int right = recyclerView.getWidth()- recyclerView.getPaddingRight();
        final int childCount = recyclerView.getChildCount();
        for(int index = 0;index< childCount;index++)
        {
            final View view = recyclerView.getChildAt(index);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)view.getLayoutParams();
            final int top =  view.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerDrawable.getIntrinsicHeight();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(canvas);
        }
    }

    /**********************关于Item之间水平方向上的分割线**********************/
    public void drawHorizontalDecoration(Canvas canvas, RecyclerView recyclerView)
    {
        final int top = recyclerView.getPaddingTop();
        final int bottom = recyclerView.getHeight()- recyclerView.getPaddingBottom();
        final int childCount = recyclerView.getChildCount();
        for(int index = 0;index < childCount;index++)
        {
            final View view = recyclerView.getChildAt(index);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)view.getLayoutParams();
            final int left =  view.getRight() + params.rightMargin;
            final int right = left + mDividerDrawable.getIntrinsicWidth();
            mDividerDrawable.setBounds(left,top,right,bottom);
            mDividerDrawable.draw(canvas);
        }
    }
}