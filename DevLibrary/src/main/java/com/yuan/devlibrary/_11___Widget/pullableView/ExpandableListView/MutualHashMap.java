package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import java.util.HashMap;

public class MutualHashMap<Key, Value>
{
    HashMap<Key, Value> mKeyToValue = new HashMap<Key, Value>();
    HashMap<Value, Key> mValueToKey = new HashMap<Value, Key>();

    MutualHashMap()
    {

    }

    public Key getKey(Value value)
    {
        return mValueToKey.get(value);
    }

    public Value getValue(Key key)
    {
        return mKeyToValue.get(key);
    }

    public void removeByKey(Key key)
    {
        if(getValue(key)!=null)
            mValueToKey.remove(getValue(key));
        mKeyToValue.remove(key);
    }

    public void removeByValue(Value value)
    {
        if(getKey(value)!=null)
            mKeyToValue.remove(getKey(value));
        mValueToKey.remove(value);
    }

    public void put(Key key, Value value)
    {
        removeByKey(key);
        removeByValue(value);
        mKeyToValue.put(key, value);
        mValueToKey.put(value, key);
    }
}