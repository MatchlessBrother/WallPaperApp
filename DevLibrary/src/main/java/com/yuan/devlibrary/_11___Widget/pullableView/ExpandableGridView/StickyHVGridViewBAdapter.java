package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

/***************实现StickyHeadersViewGridView控件的适配器时,建议继承StickyHVGridViewBAdapter适配器类***************/
/**************这样我们就只用实现其父类元素和子类元素的视图就可以正常使用此适配器类了.从而加快开发速度*************/
public abstract class StickyHVGridViewBAdapter<K,V extends StickyHVGridViewAObject> extends BaseAdapter implements StickyHVGridViewSAListAdapter,SectionIndexer
{
    private Context mContext = null;
    private int mChildCountIndex = 0;
    private String[] mParentPartArray = null;
    private ArrayList<K> mParentDatasList = new ArrayList<K>();
    private ArrayList<V> mChildDatasList  = new ArrayList<V>();
    private LinkedHashMap<K,ArrayList<V>>  mAllDatasMap = null;

    public StickyHVGridViewBAdapter(Context context, LinkedHashMap<K, ArrayList<V>> allDataMap)
    {
        mContext = context;
        mAllDatasMap=allDataMap;
        initDatasValue();
    }

    public void notifyAllDataSetChanged()
    {
        initDatasValue();
        notifyDataSetChanged();
    }

    private void initDatasValue()
    {
        mChildCountIndex = 0;
        mChildDatasList.clear();
        mParentDatasList.clear();

        for(Map.Entry<K,ArrayList<V>> entry : mAllDatasMap.entrySet())
        {
            K parentObj = entry.getKey();
            ArrayList<V> childObjs = entry.getValue();
            for(V childObj : childObjs)
            {
                mChildDatasList.add(childObj);
                childObj.setTagObj(mParentDatasList.size());
            }mParentDatasList.add(parentObj);
        }mChildCountIndex = mChildDatasList.size()-1;

        mParentPartArray = new String[mParentDatasList.size()];
        for(int index = 0;index < mParentDatasList.size();index++)
            mParentPartArray[index] = "Part：" + (index+1) ;
    }

    public Object[] getSections()
    {
        return mParentPartArray;
    }

    public int getSectionForPosition(int position) {
        return  (int)mChildDatasList.get(position).getTagObj();
    }

    public int getPositionForSection(int sectionIndex) {
        int position = 0;
        for(Map.Entry<K,ArrayList<V>> entry : mAllDatasMap.entrySet())
        {
            K parentObj = entry.getKey();
            ArrayList<V> childObjs = entry.getValue();
            if(!parentObj.equals(mParentDatasList.get(sectionIndex)))
                position += childObjs.size();
            else
                break;
        }
        return position;
    }

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
        return mChildDatasList.get(position);
    }

    public ArrayList<V> getChildDatasList()
    {
        return mChildDatasList;
    }

    public ArrayList<K> getParentDatasList()
    {
        return mParentDatasList;
    }

    public LinkedHashMap<K,ArrayList<V>> getAllDatasMap()
    {
        return mAllDatasMap;
    }

    public long getHeaderId(int position)
    {
        return (int)mChildDatasList.get(position).getTagObj();
    }

    public abstract View getChildView(V value,int position,View convertView,ViewGroup parent);
    public View getView(int position, View convertView, ViewGroup parent) {
        return getChildView(mChildDatasList.get(position),position,convertView,parent);
    }

    public abstract View getParentView(K key,int position,View convertView,ViewGroup parent);
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        return getParentView(mParentDatasList.get((int)mChildDatasList.get(position).getTagObj()),position,convertView,parent);
    }
}