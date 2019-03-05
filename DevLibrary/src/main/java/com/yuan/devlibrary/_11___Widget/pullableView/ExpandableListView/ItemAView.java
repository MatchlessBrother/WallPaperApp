package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ItemAView extends ViewGroup
{
	protected View mItem;
	protected int mItemTop;
	protected View mHeader;
	private Drawable mDivider;
	private int mDividerHeight;

	ItemAView(Context context)
	{
		super(context);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth,MeasureSpec.EXACTLY);
		int measuredHeight = 0;


		if (mHeader != null)
		{
			LayoutParams params = mHeader.getLayoutParams();
			if (params != null && params.height > 0)
				mHeader.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY));
			else
				mHeader.measure(childWidthMeasureSpec,MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			measuredHeight += mHeader.getMeasuredHeight();
		}
		else if (mDivider != null && mItem.getVisibility() != View.GONE)
		{
			measuredHeight += mDividerHeight;
		}


		LayoutParams params = mItem.getLayoutParams();
		if(mItem.getVisibility() == View.GONE)
            mItem.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
		else if (params != null && params.height >= 0)
		{
			mItem.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY));
            measuredHeight += mItem.getMeasuredHeight();
		}
		else
		{
			mItem.measure(childWidthMeasureSpec,MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            measuredHeight += mItem.getMeasuredHeight();
		}
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		left = 0;
		top = 0;
		right = getWidth();
		bottom = getHeight();

		if (mHeader != null)
		{
			int headerHeight = mHeader.getMeasuredHeight();
			mHeader.layout(left, top, right, headerHeight);
			mItemTop = headerHeight;
			mItem.layout(left, headerHeight, right, bottom);
		}
		else if (mDivider != null)
		{
			mDivider.setBounds(left, top, right, mDividerHeight);
			mItemTop = mDividerHeight;
			mItem.layout(left, mDividerHeight, right, bottom);
		}
		else
		{
			mItemTop = top;
			mItem.layout(left, top, right, bottom);
		}
	}

	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		if (mHeader == null && mDivider != null && mItem.getVisibility() != View.GONE)
		{
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
				canvas.clipRect(0, 0, getWidth(), mDividerHeight);
			mDivider.draw(canvas);
		}
	}

	void updateView(View item, View header, Drawable divider, int dividerHeight)
	{
		if (item == null)
		{
			throw new NullPointerException("List view item must not be null.");
		}

		if (mItem != item)
		{
			removeView(mItem);
			mItem = item;
			final ViewParent parent = item.getParent();
			if(parent != null && parent != this)
			{
				if(parent instanceof ViewGroup)
				{
					((ViewGroup) parent).removeView(item);
				}
			}
			addView(item);
		}
		if(mHeader != header)
		{
			if(mHeader != null)
			{
				removeView(mHeader);
			}
			mHeader = header;
			if (header != null)
			{
				addView(header);
			}
		}
		if (mDivider != divider)
		{
			mDivider = divider;
			mDividerHeight = dividerHeight;
			invalidate();
		}
	}

	public View getItem()
	{
		return mItem;
	}

	public View getHeader()
	{
		return mHeader;
	}

	public boolean hasHeader()
	{
		return mHeader != null;
	}
}