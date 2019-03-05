package com.yuan.devlibrary._11___Widget.pullableView;

/**自定义实现下拉刷新数据/上拉加载数据控件必须实现*/
/**这个接口的内容，实现并复制下面注释的代码即可***/
public interface Pullable
{
	/****判断控件是否可以下拉，可以下拉返回true，否则返回false****/
	public Boolean getCanPullDown();
	
	/****判断控件是否可以上拉，可以上拉返回true，否则返回false****/
	public Boolean getCanPullUp();
	
	/**设置控件是否可以下拉，true表示可以下拉，false表示不可以下拉*/
	public void setCanPullDown(Boolean isCanPullDown);
	
	/**设置控件是否可以上拉，true表示可以上拉，false表示不可以上拉*/
	public void setCanPullUp(Boolean isCanPullUp);
	
/*	private Boolean isCanPullDown = false;
	private Boolean isCanPullUp = false;
	//判断控件是否可以下拉，可以下拉返回true，否则返回false
	public Boolean getCanPullDown()
	{
		if(isCanPullDown)
			return true;
		else 
			return false;
	}

	//判断控件是否可以上拉，可以上拉返回true，否则返回false
	public Boolean getCanPullUp()
	{
		if(isCanPullUp)
			return true;
		else 
			return false;
	}
	
	//设置控件是否可以下拉，true表示可以下拉，false表示不可以下拉
	public void setCanPullDown(Boolean isCanPullDown)
	{
		this.isCanPullDown = isCanPullDown;
	}
	
	//设置控件是否可以上拉，true表示可以上拉，false表示不可以上拉
	public void setCanPullUp(Boolean isCanPullUp)
	{
		this.isCanPullUp = isCanPullUp;
	}*/
}