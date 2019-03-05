package com.yuan.devlibrary._11___Widget.pullableView;

import android.content.Context;
import android.util.AttributeSet;
import com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView.StickyHeadersViewCExpandableListView;

public class PullableExpandableListView extends StickyHeadersViewCExpandableListView implements Pullable
{
	private Boolean isCanPullDown = false;
	private Boolean isCanPullUp = false;
	
	public PullableExpandableListView(Context context) {super(context);}

	public PullableExpandableListView(Context context, AttributeSet attrs) {super(context, attrs);}

	public PullableExpandableListView(Context context, AttributeSet attrs, int defStyle) {super(context, attrs, defStyle);}

	/****************判断控件是否可以下拉，可以下拉返回true，否则返回false*****************/
	public Boolean getCanPullDown()
	{
		if(isCanPullDown)
		{
			if (getCount() == 0)
				return true;
			else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= getFirstHeaderViewHeight())
				return true;
			else
				return false;
		}
		else
			return false;
	}

	/**********判断控件是否可以上拉，可以上拉返回true，否则返回false，此处禁止上拉*********/
	public Boolean getCanPullUp()
	{
		if(isCanPullUp)
			return false;
		else
			return false;
	}
	
	/*************设置控件是否可以下拉，true表示可以下拉，false表示不可以下拉**************/
	public void setCanPullDown(Boolean isCanPullDown)
	{
		this.isCanPullDown = isCanPullDown;
	}
	
	/*******设置控件是否可以上拉，true表示可以上拉，false表示不可以上拉，此处禁止上拉******/
	public void setCanPullUp(Boolean isCanPullUp)
	{
		this.isCanPullUp = false;
	}
}