package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;

public class StickyHVAListViewAdapter extends BaseAdapter implements StickyHVAdapter
{
	private Context  mContext;
	private Drawable mDivider;
	private Integer  mDividerHeight;
	protected StickyHVAdapter mStickyHeadersAdapter;
	private OnStickyHeaderViewClickListener mStickyHeaderClickListener;
	private final List<View> mStickyHeaderCache = new LinkedList<>();
	private final DataSetObserver mDataSetObserver = new DataSetObserver()
	{
		public void onChanged()
		{
			StickyHVAListViewAdapter.super.notifyDataSetChanged();
		}

		public void onInvalidated()
		{
			mStickyHeaderCache.clear();
			StickyHVAListViewAdapter.super.notifyDataSetInvalidated();
		}
	};

	StickyHVAListViewAdapter(Context context, StickyHVAdapter stickyHeadersAdapter)
	{
		mContext = context;
		mStickyHeadersAdapter = stickyHeadersAdapter;
		stickyHeadersAdapter.registerDataSetObserver(mDataSetObserver);
	}

	public int getItemViewType(int position)
	{
		return mStickyHeadersAdapter.getItemViewType(position);
	}

	public int getCount()
	{
		return mStickyHeadersAdapter.getCount();
	}

	public boolean hasStableIds()
	{
		return mStickyHeadersAdapter.hasStableIds();
	}

	public int getViewTypeCount()
	{
		return mStickyHeadersAdapter.getViewTypeCount();
	}

	public Object getItem(int position)
	{
		return mStickyHeadersAdapter.getItem(position);
	}

	public long getItemId(int position)
	{
		return mStickyHeadersAdapter.getItemId(position);
	}

	public boolean areAllItemsEnabled()
	{
		return mStickyHeadersAdapter.areAllItemsEnabled();
	}

	public boolean isEnabled(int position)
	{
		return mStickyHeadersAdapter.isEnabled(position);
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return ((BaseAdapter) mStickyHeadersAdapter).getDropDownView(position, convertView, parent);
	}

	private View popHeader()
	{
		if(mStickyHeaderCache.size() > 0)
		{
			return mStickyHeaderCache.remove(0);
		}
		return null;
	}

	private boolean previousPositionHasSameHeader(int position)
	{
		return position != 0 && mStickyHeadersAdapter.getHeaderId(position) == mStickyHeadersAdapter.getHeaderId(position - 1);
	}

	private void recycleStickyHeadersViewIfExist(ItemAView StickyHeadersView)
	{
		View headers = StickyHeadersView.mHeader;
		if (headers != null)
		{
			mStickyHeaderCache.add(headers);
			headers.setVisibility(View.VISIBLE);
		}
	}

	private View configureHeader(ItemAView itemAView, final int position)
	{
		View header = itemAView.mHeader == null ? popHeader() : itemAView.mHeader;
		header = mStickyHeadersAdapter.getHeaderView(position, header, itemAView);
		if (header == null)
		{
			throw new NullPointerException("Header view must not be null.");
		}

		header.setClickable(true);
		header.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				if(mStickyHeaderClickListener != null)
				{
					long headerId = mStickyHeadersAdapter.getHeaderId(position);
					mStickyHeaderClickListener.onStickyHeaderViewClick(view, position, headerId);
				}
			}
		});
		return header;
	}

	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		return mStickyHeadersAdapter.getHeaderView(position, convertView, parent);
	}

	public long getHeaderId(int position)
	{
		return mStickyHeadersAdapter.getHeaderId(position);
	}

	public ItemAView getView(int position, View convertView, ViewGroup parent)
	{
		ItemAView itemAView = (convertView == null) ? new ItemAView(mContext) : (ItemAView) convertView;
		View item = mStickyHeadersAdapter.getView(position, itemAView.mItem, parent);
		View header = null;
		if (previousPositionHasSameHeader(position))
			recycleStickyHeadersViewIfExist(itemAView);
		else
			header = configureHeader(itemAView, position);

		if((item instanceof Checkable) && !(itemAView instanceof ItemCheckableView))
			itemAView = new ItemCheckableView(mContext);
		else if(!(item instanceof Checkable) && (itemAView instanceof ItemCheckableView))
			itemAView = new ItemAView(mContext);
		itemAView.updateView(item, header, mDivider, mDividerHeight);
		return itemAView;
	}



	public void notifyDataSetChanged()
	{
		((BaseAdapter) mStickyHeadersAdapter).notifyDataSetChanged();
	}

	public void notifyDataSetInvalidated()
	{
		((BaseAdapter) mStickyHeadersAdapter).notifyDataSetInvalidated();
	}

	public int hashCode()
	{
		return mStickyHeadersAdapter.hashCode();
	}

	public boolean isEmpty()
	{
		return mStickyHeadersAdapter.isEmpty();
	}

	public String toString()
	{
		return mStickyHeadersAdapter.toString();
	}

	public boolean equals(Object o)
	{
		return mStickyHeadersAdapter.equals(o);
	}

	public interface OnStickyHeaderViewClickListener
	{
		void onStickyHeaderViewClick(View header, int itemPosition, long headerId);
	}

	public void setDivider(Drawable divider, int dividerHeight)
	{
		mDivider = divider;
		mDividerHeight = dividerHeight;
		notifyDataSetChanged();
	}

	public void setOnHeaderClickListener(OnStickyHeaderViewClickListener onHeaderClickListener)
	{
		this.mStickyHeaderClickListener = onHeaderClickListener;
	}
}