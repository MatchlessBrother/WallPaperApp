package company.petrifaction.boss.network;

import java.util.Map;
import retrofit2.http.GET;
import okhttp3.RequestBody;
import io.reactivex.Observable;
import retrofit2.http.QueryMap;
import company.petrifaction.boss.bean.PhotoData;
import company.petrifaction.boss.bean.StyleData;
import company.petrifaction.boss.bean.BaseReturnListData;

public interface NetUrl
{
    @GET("/wallpaper/getMenu")
    Observable<BaseReturnListData<StyleData>> getStyleDatas(@QueryMap Map<String,RequestBody> params);

    @GET("/wallpaper/getWallPaperList")
    Observable<BaseReturnListData<PhotoData>> getPhotoDatas(@QueryMap Map<String,RequestBody> params);
}