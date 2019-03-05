package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

/**最终包装器,包装了adapter,负责调用返回的Header和item的视图,负责处理headers的索引值.*/
public class StickyHVGridViewBaseAdapter extends BaseAdapter
{
	private int mCount;
	private View mLastViewSeen;
	private int mNumColumns = 1;
	private final Context mContext;
	private View mLastHeaderViewSeen;
	private boolean mCounted = false;
	private StickyHeadersViewGridView mGridView;
	private final StickyHVGridViewListAdapter mDelegate;
	
    private static final int sNumViewTypes = 3;
    protected static final int ID_HEADER = -0x01;
    protected static final int ID_FILLER = -0x02;
    protected static final int ID_HEADER_FILLER = -0x03;
    protected static final int POSITION_FILLER = -0x01;
    protected static final int POSITION_HEADER = -0x02;
    protected static final int POSITION_HEADER_FILLER = -0x03;
    protected static final int VIEW_TYPE_FILLER = 0x00;
    protected static final int VIEW_TYPE_HEADER = 0x01;
    protected static final int VIEW_TYPE_HEADER_FILLER = 0x02;

    protected void updateCount() 
    {
        mCount = 0;
        int numHeaders = mDelegate.getNumHeaders();
        if (numHeaders == 0) 
        {
            mCount = mDelegate.getCount();
            mCounted = true;
            return;
        }

        for (int i = 0; i < numHeaders; i++) 
        {
            mCount += mDelegate.getCountForHeader(i) + mNumColumns;
        }
        mCounted = true;
    }
    
    private DataSetObserver mDataSetObserver = new DataSetObserver() 
    {
        public void onChanged() 
        {
            updateCount();
            notifyDataSetChanged();
        }

        public void onInvalidated() 
        {
            mCounted = false;
            notifyDataSetInvalidated();
        }
    };
    
    public void registerDataSetObserver(DataSetObserver observer) 
    {
        mDelegate.registerDataSetObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) 
    {
        mDelegate.unregisterDataSetObserver(observer);
    }
    
    public StickyHVGridViewBaseAdapter(Context context, StickyHeadersViewGridView gridView, StickyHVGridViewListAdapter delegate)
    {
        mContext = context;
        mDelegate = delegate;
        mGridView = gridView;
        delegate.registerDataSetObserver(mDataSetObserver);
    }

    public int getCount()
    {
        if (mCounted) 
        {
            return mCount;
        }
        mCount = 0;
        int numHeaders = mDelegate.getNumHeaders();
        if (numHeaders == 0) 
        {
            mCount = mDelegate.getCount();
            mCounted = true;
            return mCount;
        }

        for (int i = 0; i < numHeaders; i++) 
        {
            //Pad count with space for header and trailing filler in header group.
            mCount += mDelegate.getCountForHeader(i) + unFilledSpacesInHeaderGroup(i) + mNumColumns;
        }
        mCounted = true;
        return mCount;
    }

    public long getItemId(int position) 
    {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_HEADER) 
            return ID_HEADER;
        if (adapterPosition.mPosition == POSITION_FILLER) 
            return ID_FILLER;
        if (adapterPosition.mPosition == POSITION_HEADER_FILLER) 
            return ID_HEADER_FILLER;
        return mDelegate.getItemId(adapterPosition.mPosition);
    }
    
    public Object getItem(int position) throws ArrayIndexOutOfBoundsException 
    {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_FILLER || adapterPosition.mPosition == POSITION_HEADER) 
            return null;
        return mDelegate.getItem(adapterPosition.mPosition);
    }
    
    public int getViewTypeCount() 
    {
        return mDelegate.getViewTypeCount() + sNumViewTypes;
    }

    public int getItemViewType(int position) 
    {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_HEADER) 
            return VIEW_TYPE_HEADER;
        if (adapterPosition.mPosition == POSITION_FILLER) 
            return VIEW_TYPE_FILLER;
        if (adapterPosition.mPosition == POSITION_HEADER_FILLER) 
            return VIEW_TYPE_HEADER_FILLER;
        int itemViewType = mDelegate.getItemViewType(adapterPosition.mPosition);
        if (itemViewType == IGNORE_ITEM_VIEW_TYPE) 
            return itemViewType;
        return itemViewType + sNumViewTypes;
    }

    public View getView(int position, View convertView, ViewGroup parent) 
    {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_HEADER) 
        {
            HeaderFillerView v = getHeaderFillerView(adapterPosition.mHeader, convertView, parent);
            View view = mDelegate.getHeaderView(adapterPosition.mHeader, (View)v.getTag(), parent);
            mGridView.detachHeader((View) v.getTag());
            v.setTag(view);
            mGridView.attachHeader(view);
            convertView = v;
            mLastHeaderViewSeen = v;
            v.forceLayout();
        } 
        else if (adapterPosition.mPosition == POSITION_HEADER_FILLER) 
        {
            convertView = getFillerView(convertView, parent, mLastHeaderViewSeen);
            convertView.forceLayout();
        }
        else if (adapterPosition.mPosition == POSITION_FILLER) 
        {
            convertView = getFillerView(convertView, parent, mLastViewSeen);
        } 
        else 
        {
            convertView = mDelegate.getView(adapterPosition.mPosition, convertView, parent);
            mLastViewSeen = convertView;
        }
        return convertView;
    }

    private FillerView getFillerView(View convertView, ViewGroup parent, View lastViewSeen) 
    {
        FillerView fillerView = (FillerView)convertView;
        if (fillerView == null) 
            fillerView = new FillerView(mContext);
        fillerView.setMeasureTarget(lastViewSeen);
        return fillerView;
    }

    private HeaderFillerView getHeaderFillerView(int headerPosition, View convertView,ViewGroup parent) 
    {
        HeaderFillerView headerFillerView = (HeaderFillerView)convertView;
        if (headerFillerView == null) 
            headerFillerView = new HeaderFillerView(mContext);
        return headerFillerView;
    }
  
    protected long getHeaderId(int position) 
    {
        return translatePosition(position).mHeader;
    }

    protected View getHeaderView(int position, View convertView, ViewGroup parent) 
    {
        if (mDelegate.getNumHeaders() == 0) 
            return null;
        return mDelegate.getHeaderView(translatePosition(position).mHeader, convertView, parent);
    }
    
    protected Position translatePosition(int position) 
    {
        int numHeaders = mDelegate.getNumHeaders();
        if (numHeaders == 0)
        {
            if (position >= mDelegate.getCount()) 
                return new Position(POSITION_FILLER, 0);
            return new Position(position, 0);
        }

        //Translate GridView position to Adapter position.
        int adapterPosition = position;
        int place = position;
        int i;

        for (i = 0; i < numHeaders; i++) 
        {
            int sectionCount = mDelegate.getCountForHeader(i);

            //Skip past fake items making space for header in front of sections.
            if (place == 0) 
            {
                //Position is first column where header will be.
                return new Position(POSITION_HEADER, i);
            }
            
            place -= mNumColumns;
            if (place < 0) 
            {
                // Position is a fake so return null.
                return new Position(POSITION_HEADER_FILLER, i);
            }
            
            adapterPosition -= mNumColumns;
            if (place < sectionCount) 
            {
                return new Position(adapterPosition, i);
            }
            
            // Skip past section end of section row filler;
            int filler = unFilledSpacesInHeaderGroup(i);
            adapterPosition -= filler;
            place -= sectionCount + filler;
            if (place < 0) 
            {
                // Position is a fake so return null.
                return new Position(POSITION_FILLER, i);
            }
        }
        // Position is a fake.
        return new Position(POSITION_FILLER, i);
    }

    private int unFilledSpacesInHeaderGroup(int headerId) 
    {
        int remainder = mDelegate.getCountForHeader(headerId) % mNumColumns;
        return remainder == 0 ? 0 : mNumColumns - remainder;
    }
    
    public boolean isEmpty() 
    {
        return mDelegate.isEmpty();
    }
    
    public boolean hasStableIds() 
    {
        return mDelegate.hasStableIds();
    }
    
    public boolean areAllItemsEnabled() 
    {
        return false;
    }

    public boolean isEnabled(int position) 
    {
        Position adapterPosition = translatePosition(position);
        if (adapterPosition.mPosition == POSITION_FILLER || adapterPosition.mPosition == POSITION_HEADER) 
            return false;
        return mDelegate.isEnabled(adapterPosition.mPosition);
    }
   
    public void setNumColumns(int numColumns) 
    {
    	mCounted = false;
        mNumColumns = numColumns;
        // notifyDataSetChanged();
    }
    
    public StickyHVGridViewListAdapter getWrappedAdapter()
    {
        return mDelegate;
    }
    
    protected class Position 
    {
        protected int mHeader;
        protected int mPosition;

        protected Position(int position, int header)
        {
        	mHeader = header;
            mPosition = position;       
        }
    }

    protected class HeaderHolder 
    {
        protected View mHeaderView;
    }
    
    /*Simple view to fill space in grid view*/
    protected class FillerView extends View 
    {
        private View mMeasureTarget;
        public FillerView(Context context) 
        {
            super(context);
        }

        public FillerView(Context context, AttributeSet attrs) 
        {
            super(context, attrs);
        }

        public FillerView(Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
        }

        public void setMeasureTarget(View lastViewSeen) 
        {
            mMeasureTarget = lastViewSeen;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
        {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMeasureTarget.getMeasuredHeight(),MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /*A view to hold the section header and measure the header row height correctly*/
    protected class HeaderFillerView extends FrameLayout 
    {
        private int mHeaderId;

        public HeaderFillerView(Context context) 
        {
            super(context);
        }

        public HeaderFillerView(Context context, AttributeSet attrs) 
        {
            super(context, attrs);
        }

        public HeaderFillerView(Context context, AttributeSet attrs, int defStyle) 
        {
            super(context, attrs, defStyle);
        }

        public void setHeaderId(int headerId) 
        {
            mHeaderId = headerId;
        }
        
        public int getHeaderId() 
        {
            return mHeaderId;
        }

        protected LayoutParams generateDefaultLayoutParams()
        {
            return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
        {
            View v = (View)getTag();
            ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params == null) 
            {
                params = generateDefaultLayoutParams();
                v.setLayoutParams(params);
            }
            if (v.getVisibility() != View.GONE) 
            {
                int heightSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 0, params.height);
                int widthSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(mGridView.getWidth(), MeasureSpec.EXACTLY), 0,params.width);
                v.measure(widthSpec, heightSpec);
            }
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), v.getMeasuredHeight());
        }
    }
}