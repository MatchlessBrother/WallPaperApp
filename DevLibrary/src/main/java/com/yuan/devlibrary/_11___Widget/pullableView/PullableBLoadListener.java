package com.yuan.devlibrary._11___Widget.pullableView;

import android.content.Context;
import android.widget.Toast;

import com.yuan.devlibrary._12_______Utils.NetUtils;

/**设置加载操作结果的回调接口**/
public class PullableBLoadListener
{
    private Context context;

    public PullableBLoadListener(Context context)
    {
        this.context = context;
    }

    /************************设置加载操作结果的回调接口************************/
    /****有网络才能正确的加载数据,所以可以加载数据会返回true,否则返回false*****/
    public Boolean onStartLoading()
    {
        if(NetUtils.WhetherConnectNet(context))
            return true;
        else
        {
            Toast.makeText(context, "亲，网络异常！无法加载新数据哟！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}