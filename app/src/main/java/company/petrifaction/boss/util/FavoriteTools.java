package company.petrifaction.boss.util;

import java.util.List;
import com.google.gson.Gson;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import android.content.SharedPreferences;
import company.petrifaction.boss.bean.PhotoData;
import com.yuan.devlibrary._12_______Utils.GsonUtils;

/***存储Favorite的工具类***/
public class FavoriteTools
{
    /************************在本地Sp添加指定Favorite图片数据*********************/
    public static void addPhotoData(Context context,PhotoData photoData)
    {
        List<PhotoData> oldPhotoDatas = getPhotoDatas(context);
        if(oldPhotoDatas.size() > 0)
        {
            for(int index = 0;index < oldPhotoDatas.size();index++)
            {
                PhotoData oldPhotoData = oldPhotoDatas.get(index);
                if(null != oldPhotoData.getDownloadUrl() && null != photoData && null != photoData.
                getDownloadUrl() && oldPhotoData.getDownloadUrl().trim().equals(photoData.getDownloadUrl().trim()))
                {
                    return;
                }
                if(index == oldPhotoDatas.size() - 1)
                {
                    oldPhotoDatas.add(photoData);
                    break;
                }
            }
        }
        else
        {
            oldPhotoDatas.add(photoData);
        }
        addPhotoDatas(context,oldPhotoDatas);
    }

    /************************在本地Sp删除指定Favorite图片数据*********************/
    public static void removePhotoData(Context context,PhotoData photoData)
    {
        List<PhotoData> oldPhotoDatas = getPhotoDatas(context);
        if(oldPhotoDatas.size() > 0)
        {
            for(int index = 0;index < oldPhotoDatas.size();index++)
            {
                PhotoData oldPhotoData = oldPhotoDatas.get(index);
                if(null != oldPhotoData.getDownloadUrl() && null != photoData && null != photoData.
                getDownloadUrl() && oldPhotoData.getDownloadUrl().trim().equals(photoData.getDownloadUrl().trim()))
                {
                    oldPhotoDatas.remove(index);
                    addPhotoDatas(context,oldPhotoDatas);
                    return;
                }
            }
        }
        return;
    }

    /**************************在本地Sp存储Favorite图片数据**********************/
    public static void addPhotoDatas(Context context, List<PhotoData> photoDatas)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putString("photodatas",GsonUtils.objectToGsonString(photoDatas)).apply();
    }

    /**************************从本地Sp获取Favorite图片数据**********************/
    public static List<PhotoData> getPhotoDatas(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_MULTI_PROCESS);
        return new Gson().fromJson(sharedPreferences.getString("photodatas","[]"),new TypeToken<List<PhotoData>>(){}.getType());
    }
}