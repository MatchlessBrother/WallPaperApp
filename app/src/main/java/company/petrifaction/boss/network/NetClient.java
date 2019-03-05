package company.petrifaction.boss.network;

import java.util.List;
import retrofit2.Retrofit;
import java.util.ArrayList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import android.content.Context;
import company.petrifaction.boss.BuildConfig;
import com.yuan.devlibrary._9__Network.retrofit.Http1BaseConfig.RetrofitFactory;
import com.yuan.devlibrary._9__Network.okhttp.Http3Interceptions.RetrysInterceptor;
import com.yuan.devlibrary._9__Network.okhttp.Http1BasicConfig.OkhttpClientFactory;
import com.yuan.devlibrary._9__Network.okhttp.Http3Interceptions.TokenInterceptor_PersistentStore;

public class NetClient
{
    private NetUrl mNetUrl;
    private static NetClient mNetClient;
    private volatile Retrofit mRetrofit;
    private volatile OkHttpClient mOkHttpClient;

    public synchronized static NetClient getInstance(Context context)
    {
        if(null == mNetClient)
            mNetClient = new NetClient(context);
        return mNetClient;
    }

    private NetClient(Context context)
    {
        List<Interceptor> appInterceptorList = new ArrayList<Interceptor>();
        appInterceptorList.add(new RetrysInterceptor(2,1000,2000));
        TokenInterceptor_PersistentStore tokenInterceptorPersistentStore = TokenInterceptor_PersistentStore.getInstance(context);
        tokenInterceptorPersistentStore.setMaxNumOfTryOn(0);
        appInterceptorList.add(tokenInterceptorPersistentStore);
        mOkHttpClient = OkhttpClientFactory.create(context,30,30,30,"",0,appInterceptorList,null);
        mRetrofit = RetrofitFactory.create(mOkHttpClient, BuildConfig.SERVICE_URL);
    }

    public NetUrl getNetUrl()
    {
        if(null == mNetUrl)
            mNetUrl = mRetrofit.create(NetUrl.class);
        return mNetUrl;
    }

    public Retrofit getRetrofit()
    {
        return mRetrofit;

    }

    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;

    }

    public void setRetrofit(Retrofit retrofit)
    {
        mRetrofit = retrofit;

    }

    public void setOkHttpClient(OkHttpClient okHttpClient)
    {
        mOkHttpClient = okHttpClient;

    }
}