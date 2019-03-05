package company.petrifaction.boss.ui.base;

import io.reactivex.Observer;
import retrofit2.HttpException;
import android.content.Context;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import io.reactivex.disposables.Disposable;
import company.petrifaction.boss.network.NetFlags;
import com.yuan.devlibrary._12_______Utils.NetUtils;
import company.petrifaction.boss.bean.BaseReturnData;

/******************************查询网络数据回调的处理类****************************/
public class BaseMvp_NetObjCallBack<T extends BaseReturnData> implements Observer<T>
{
    private Context mContext;
    private Disposable mDisposable;
    private BaseMvp_LocalObjCallBack mBaseMvpLocalCallBack;

    public BaseMvp_NetObjCallBack(Context context, BaseMvp_LocalObjCallBack<T> baseMvpLocalCallBack)
    {
        mContext = context;
        mDisposable = null;
        mBaseMvpLocalCallBack = baseMvpLocalCallBack;
    }

    public void onSubscribe(Disposable disposable)
    {
        mDisposable = disposable;

    }

    public void onNext(T baseReturnData)
    {
        if (NetFlags.RequestFail.equals(baseReturnData.getCode()))
        {
            mBaseMvpLocalCallBack.onFailure(baseReturnData.getMsg());
        }
        else if(NetFlags.RequestWillUpdate.equals(baseReturnData.getCode()))
        {
           //SignInAct.quitCrrentAccount(mContext,"请升级软件后再使用");
        }
        else if(NetFlags.RequestSuccess.equals(baseReturnData.getCode()))
        {
            mBaseMvpLocalCallBack.onSuccess(baseReturnData);
        }
    }

    public void onError(Throwable e)
    {
        if (!NetUtils.WhetherConnectNet(mContext))
        {
            mBaseMvpLocalCallBack.onError("网络不可用,请稍后再试...");
        }
        else if (e instanceof SocketTimeoutException)
        {
            mBaseMvpLocalCallBack.onError("网络连接超时,请稍后再试...");
        }
        else if (e instanceof ConnectException)
        {
            mBaseMvpLocalCallBack.onError("网络连接超时,请稍后再试...");
        }
        else if (e instanceof HttpException)
        {
            mBaseMvpLocalCallBack.onError("网络异常,请稍后再试...");
        }
        else if (e instanceof UnknownHostException)
        {
            mBaseMvpLocalCallBack.onError("网络异常,请稍后再试...");
        }
        else
        {
            mBaseMvpLocalCallBack.onError("网络发生错误,请稍后再试...");
        }
        mBaseMvpLocalCallBack.onFinish();
    }

    public void onComplete()
    {
        mBaseMvpLocalCallBack.onFinish();

    }
}