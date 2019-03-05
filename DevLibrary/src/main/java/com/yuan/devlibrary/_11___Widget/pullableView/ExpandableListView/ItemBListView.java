package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class ItemBListView extends ListView
{
	private int mTopClippingLength;
	private List<View> mFooterViews;
	private Field mSelectorPositionField;
	private Rect mSelectorRect = new Rect();
	private boolean mClippingToPadding = true;
	private boolean mBlockLayoutChildren = false;
	private LifeCycleListener mLifeCycleListener;

	ItemBListView(Context context)
	{
		super(context);
		try
		{
			Field selectorRectField = AbsListView.class.getDeclaredField("mSelectorRect");
			selectorRectField.setAccessible(true);
			mSelectorRect = (Rect) selectorRectField.get(this);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			{
				mSelectorPositionField = AbsListView.class.getDeclaredField("mSelectorPosition");
				mSelectorPositionField.setAccessible(true);
			}
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	protected void dispatchDraw(Canvas canvas)
	{
		positionSelectorRect();
		if (mTopClippingLength != 0)
		{
			canvas.save();
			Rect clipping = canvas.getClipBounds();
			clipping.top = mTopClippingLength;
			canvas.clipRect(clipping);
			super.dispatchDraw(canvas);
			canvas.restore();
		}
		else
		{
			super.dispatchDraw(canvas);
		}
		mLifeCycleListener.onDispatchDrawOccurred(canvas);
	}

	private void positionSelectorRect()
	{
		if (!mSelectorRect.isEmpty())
		{
			int selectorPosition = getSelectorPosition();
			if (selectorPosition >= 0)
			{
				int firstVisibleItem = getFixedFirstVisibleItem();
				View v = getChildAt(selectorPosition - firstVisibleItem);
				if (v instanceof ItemAView)
				{
					ItemAView wrapper = ((ItemAView) v);
					mSelectorRect.top = wrapper.getTop() + wrapper.mItemTop;
				}
			}
		}
	}

	private int getSelectorPosition()
	{
		if (mSelectorPositionField == null)
		{
			for (int i = 0; i < getChildCount(); i++)
			{
				if (getChildAt(i).getBottom() == mSelectorRect.bottom)
				{
					return i + getFixedFirstVisibleItem();
				}
			}
		}
		else
		{
			try
			{
				return mSelectorPositionField.getInt(this);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return -1;
	}

	public boolean performItemClick(View view, int position, long id)
	{
		if (view instanceof ItemAView)
		{
			view = ((ItemAView) view).mItem;
		}
		return super.performItemClick(view, position, id);
	}

	boolean containsFooterView(View v)
	{
		if (mFooterViews == null)
			return false;
		return mFooterViews.contains(v);
	}

	public void addFooterView(View view)
	{
		super.addFooterView(view);
		addInternalFooterView(view);
	}

	public void addFooterView(View view, Object data, boolean isSelectable)
	{
		super.addFooterView(view, data, isSelectable);
		addInternalFooterView(view);
	}

	private void addInternalFooterView(View view)
	{
		if (mFooterViews == null)
			mFooterViews = new ArrayList<View>();
		mFooterViews.add(view);
	}

	public boolean removeFooterView(View view)
	{
		if (super.removeFooterView(view))
		{
			mFooterViews.remove(view);
			return true;
		}
		return false;
	}

	void setTopClippingLength(int topClipping)
	{
		mTopClippingLength = topClipping;
		return;
	}

	public void setClipToPadding(boolean clipToPadding)
	{
		mClippingToPadding = clipToPadding;
		super.setClipToPadding(clipToPadding);
	}

	protected void layoutChildren()
	{
		if (!mBlockLayoutChildren)
		{
			super.layoutChildren();
		}
	}

	public int getFixedFirstVisibleItem()
	{
		int firstVisibleItem = getFirstVisiblePosition();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			return firstVisibleItem;

		for (int i = 0; i < getChildCount(); i++)
		{
			if (getChildAt(i).getBottom() >= 0)
			{
				firstVisibleItem += i;
				break;
			}
		}

		if (!mClippingToPadding && getPaddingTop() > 0 && firstVisibleItem > 0)
		{
			if (getChildAt(0).getTop() > 0)
			{
				firstVisibleItem -= 1;
			}
		}
		return firstVisibleItem;
	}

	public void setBlockLayoutChildren(boolean block)
	{
		mBlockLayoutChildren = block;
		return;
	}

	public void setLifeCycleListener(LifeCycleListener lifeCycleListener)
	{
		mLifeCycleListener = lifeCycleListener;
	}

	interface LifeCycleListener
	{
		void onDispatchDrawOccurred(Canvas canvas);
	}
}