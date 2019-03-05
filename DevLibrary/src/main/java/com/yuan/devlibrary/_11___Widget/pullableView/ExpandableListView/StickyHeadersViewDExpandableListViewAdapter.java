package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

/**实现StickyHeadersViewCExpandableListView控件的适配器时,建议继承StickyHeadersViewDExpandableListViewAdapter适配器类,*/
/***********************************************这样有助于快速,高效的实现适配器.***************************************/
public abstract class StickyHeadersViewDExpandableListViewAdapter<K extends StickyHeadersViewAObject,V extends StickyHeadersViewAObject> extends BaseAdapter implements StickyHVAdapter,SectionIndexer
{
    private Context mContext = null;
    private V mChildFalseData = null;
    private Integer mChildCountIndex = 0;
    private String[] mParentPartArray = null;
    private ArrayList<V> mChildSetDatas = null;
    private ArrayList<K> mParentSetDatas = null;
    private LinkedHashMap<K,ArrayList<V>> mMapDatas = null;

    public StickyHeadersViewDExpandableListViewAdapter(Context context, LinkedHashMap<K, ArrayList<V>> hashMap)
    {
        mContext = context;
        mMapDatas = hashMap;
        initFalseData();
        initDatasValue();
    }

    public void notifyAllDataSetChanged()
    {
        initFalseData();
        initDatasValue();
        notifyDataSetChanged();
    }

    private void initFalseData()
    {
        for(Map.Entry<K,ArrayList<V>> entry : mMapDatas.entrySet())
        {
            for(V childData : entry.getValue())
            {
                mChildFalseData = childData;return;
            }
        }
    }

    private void initDatasValue()
    {
        mChildCountIndex = 0;
        mParentSetDatas = new ArrayList<K>();
        mChildSetDatas  = new ArrayList<V>();
        boolean isFirstSetOfDataForMapDatas = true;

        for(Map.Entry<K,ArrayList<V>> entry : mMapDatas.entrySet())
        {
            K parentObj = entry.getKey();
            ArrayList<V> childObjs = entry.getValue();
            if(childObjs.size() == 0)
            {
                V childData = (V)mChildFalseData.cloneObject();
                childData.setIsRealData(false);
                childObjs.add(childData);
            }
            /**父类元素必须记录其第一个子类元素的下标*/
            if(isFirstSetOfDataForMapDatas)
            {
                parentObj.setTagObj(0);
                isFirstSetOfDataForMapDatas = false;
                mChildCountIndex = childObjs.size() - 1;
            }
            else
            {
                parentObj.setTagObj(mChildCountIndex+1);
                mChildCountIndex += childObjs.size();
            }
            /**子类元素必须记录其所属父类元素的下标*/
            for(V childObj : childObjs)
                childObj.setTagObj(mParentSetDatas.size());
            mParentSetDatas.add(parentObj);
            mChildSetDatas .addAll(childObjs);
        }

        mParentPartArray = new String[mParentSetDatas.size()];
        for(int index = 0;index < mParentSetDatas.size();index++)
            mParentPartArray[index] = "Part：" + (index+1) ;
    }

    /******************************************优化滚动速度部分******************************************/
    public int getCount()
    {
        return mChildCountIndex + 1;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public Object getItem(int position)
    {
        return mChildSetDatas.get(position);
    }

    public long getHeaderId(int position)
    {
        return (int)mChildSetDatas.get(position).getTagObj();
    }

    /*****此函数用于关闭所有父类标签,其实就是为了正常显示无子类数据的父类标签,在setAdapter()中被调用*****/
    public void collapseAllGroupView(StickyHeadersViewCExpandableListView expandableListView) {
        for(Map.Entry<K,ArrayList<V>> entry : mMapDatas.entrySet())
        {
            expandableListView.collapse((int)entry.getValue().get(0).getTagObj());
        }
    }

    public ArrayList<V> getChildSetDatas()
    {
        return mChildSetDatas;
    }

    public ArrayList<K> getParentSetDatas()
    {
        return mParentSetDatas;
    }

    /******************************************快速搜索内容部分******************************************/
    public Object[] getSections()
    {
        return mParentPartArray;
    }

    public int getSectionForPosition(int position) {return (int)mChildSetDatas.get(position).getTagObj();}

    public int getPositionForSection(int sectionIndex) {return (int)mParentSetDatas.get(sectionIndex).getTagObj();}

    /******************************************生成界面内容部分******************************************/
    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        return getParentView(mParentSetDatas.get((int) mChildSetDatas.get(position).getTagObj()), position,convertView,parent);
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getChildView(mChildSetDatas.get(position),position,convertView,parent);
    }
    public abstract View getParentView(K key,int position,View convertView,ViewGroup parent);
    public abstract View getChildView(V value,int position,View convertView,ViewGroup parent);
}