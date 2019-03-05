package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;

import android.content.Context;
import android.widget.Checkable;

public class ItemCheckableView extends ItemAView implements Checkable
{
	public ItemCheckableView(final Context context)
	{
		super(context);
	}

	public void toggle()
	{
		setChecked(!isChecked());
	}

	public boolean isChecked()
	{
		return ((Checkable) mItem).isChecked();
	}

	public void setChecked(final boolean checked)
	{
		((Checkable) mItem).setChecked(checked);
	}
}