package com.yuan.devlibrary._11___Widget.pullableView;

import android.content.Context;
import android.widget.Toast;

import com.yuan.devlibrary._12_______Utils.NetUtils;

/**设置刷新操作结果的回调接口**/
public class PullableBRefreshListener
{
	private Context context;

	public PullableBRefreshListener(Context context)
	{
		this.context = context;
	}

	/************************设置刷新操作结果的回调接口************************/
	/****有网络才能正确的刷新数据,所以可以刷新数据会返回true,否则返回false*****/
	public Boolean onStartRefreshing(PullableARefreshLayout pullToRefreshLayout)
	{
		if(NetUtils.WhetherConnectNet(context))
			return true;
		else
		{
			pullToRefreshLayout.refreshFinish(pullToRefreshLayout.FAIL);
			Toast.makeText(context,"亲，网络异常！刷新数据失败了哟！",Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}