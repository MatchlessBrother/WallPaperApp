package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OneToManyHashMap<Key,ItemValue>
{
    private IDMapper<Key, ItemValue> mIDMapper;
    private LinkedHashMap<Object,Key> mValueToKeyIndexer = new LinkedHashMap<Object, Key>();
    private LinkedHashMap<Object,List<ItemValue>> mKeyToValuesMap = new LinkedHashMap<Object, List<ItemValue>>();

    OneToManyHashMap()
    {
         this(new IDMapper<Key, ItemValue>()
         {
             public Object keyToKeyId(Key key)
             {
                 return key;
             }

             public Key keyIdToKey(Object keyId)
             {
                 return (Key) keyId;
             }

             public Object valueToValueId(ItemValue value)
             {
                 return value;
             }

             public ItemValue valueIdToValue(Object valueId)
             {
                 return (ItemValue) valueId;
             }
         });
    }

    OneToManyHashMap(IDMapper<Key, ItemValue> idMapper)
    {
        mIDMapper = idMapper;
    }

    public void add(Key key,ItemValue value)
    {
        Object keyId = mIDMapper.keyToKeyId(key);
        if(mKeyToValuesMap.get(keyId) == null)
        {
            mKeyToValuesMap.put(keyId,new ArrayList<ItemValue>());
        }

        Key keyForValue = getKey(value);
        if(keyForValue != null)
        {
            mKeyToValuesMap.get(mIDMapper.keyToKeyId(keyForValue)).remove(value);
        }

        mValueToKeyIndexer.put(mIDMapper.valueToValueId(value), key);
        if(!containsValue(mKeyToValuesMap.get(mIDMapper.keyToKeyId(key)),value))
            mKeyToValuesMap.get(mIDMapper.keyToKeyId(key)).add(value);
    }

    protected boolean containsValue(List<ItemValue> list,ItemValue  value)
    {
        for (ItemValue itemValue :list)
        {
            if(mIDMapper.valueToValueId(itemValue).equals(mIDMapper.valueToValueId(value)))
            {
                return true;
            }
        }
        return false;
    }

    public void removeKey(Key key)
    {
        if(mKeyToValuesMap.get(mIDMapper.keyToKeyId(key))!=null)
        {
            for (ItemValue value : mKeyToValuesMap.get(mIDMapper.keyToKeyId(key)))
            {
                mValueToKeyIndexer.remove(mIDMapper.valueToValueId(value));
            }
            mKeyToValuesMap.remove(mIDMapper.keyToKeyId(key));
        }
    }

    public void removeValue(ItemValue value)
    {
        if(getKey(value)!=null)
        {
            List<ItemValue> itemValues = mKeyToValuesMap.get(mIDMapper.keyToKeyId(getKey(value)));
            if(itemValues!=null)
            {
                itemValues.remove(value);
            }
        }
        mValueToKeyIndexer.remove(mIDMapper.valueToValueId(value));
    }

    public void clear()
    {
        mValueToKeyIndexer.clear();
        mKeyToValuesMap.clear();
    }

    public void clearValues()
    {
        for (Map.Entry<Object,List<ItemValue>> entry:entrySet())
        {
            if(entry.getValue()!=null)
            {
                entry.getValue().clear();
            }
        }
        mValueToKeyIndexer.clear();
    }

    public Key getKey(ItemValue value)
    {
        return mValueToKeyIndexer.get(mIDMapper.valueToValueId(value));
    }

    public ItemValue getValueByPosition(int position)
    {
        Object[] vauleIdArray = mValueToKeyIndexer.keySet().toArray();
        if(position>vauleIdArray.length)
        {
            throw new IndexOutOfBoundsException();
        }
        Object valueId = vauleIdArray[position];
        return mIDMapper.valueIdToValue(valueId);
    }

    public List<ItemValue> get(Key key)
    {
        return mKeyToValuesMap.get(mIDMapper.keyToKeyId(key));
    }

    public int size()
    {
        return mKeyToValuesMap.size();
    }

    public int valuesSize()
    {
        return mValueToKeyIndexer.size();
    }

    public Set<Map.Entry<Object,Key>> reverseEntrySet()
    {
        return mValueToKeyIndexer.entrySet();
    }

    public Set<Map.Entry<Object,List<ItemValue>>> entrySet()
    {
        return mKeyToValuesMap.entrySet();
    }

    public interface IDMapper<TKey,TItemValue>
    {
        public Object keyToKeyId(TKey key);
        public TKey keyIdToKey(Object keyId);
        public Object valueToValueId(TItemValue value);
        public TItemValue valueIdToValue(Object valueId);
    }
}