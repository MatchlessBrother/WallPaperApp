package com.yuan.devlibrary._11___Widget.pullableView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.yuan.devlibrary.R;
import com.yuan.devlibrary._12_______Utils.NetUtils;
import com.yuan.devlibrary._11___Widget.indicator.IndicatorView;

/************此控件只支持嵌套LinearLayout布局,不支持RelativeLayout布局,因为***************/
/************LinearLayout布局的长宽都是根据其子View的多少和大小决定的,且排布方式简单******/
/************而RelativeLayout布局的长宽多由其本身决定，如果其采用由子类View的多少和大小***/
/************来决定其本身的长宽，那么动态添加其子类View就会有很多困扰,因此可以揣测********/
/************RelativeLayout布局和ScrollView布局本身就不适合单独的搭配使用*****************/
/**********************所以此控件只支持嵌套LinearLayout布局*******************************/
public class PullableCScrollView extends ScrollView implements Pullable
{
	private Context context = null;
	/***********控件底部加载栏**********/
	private View footView = null;
	/********控件底部加载栏的高度*******/
	public int footViewHeight = 0;
	/****控件底部加载栏中的旋转图片*****/
	private IndicatorView footViewImg = null;
	/****控件底部加载栏中的状态文字*****/
	private TextView footViewTv = null;
	/**表示此控件是否支持下拉刷新功能***/
	private Boolean isCanPullDown = false;
	/**表示此控件是否支持上拉加载功能***/
	private Boolean isCanPullUp = false;
	/********用户手指是否离开屏幕*******/
	private Boolean isTouching = false;
	/*********控件处于正在刷新中********/
	private Boolean isRefresing = false;
	/********控件是否处于滚动状态*******/
	private Boolean isScrolling = false;
	/*******控件之前滚动的ScrollY值*****/
	private int lastScrollY = 0;
	/****加载更多数据时进行回调的接口***/
	private PullableBLoadListener mListener = null;
	/****加载更多数据时进行旋转的动画***/
	private RotateAnimation footViewImgAnim = null;
	/*******当控件由动态变为静态时指定使用的回调Handler********/
	private StopScrollCallBackHandler StopScrollHandler = null;
	/*******************表示控件当前的加载状态*****************/
	private Integer loadDataState = LOADSTATE_ALREADYLOAD;
	public static final int LOADSTATE_ALREADYLOAD = 0x0001;
	public static final int LOADSTATE_LOADING = 0x0002;
	public static final int LOADSTATE_NODATALOAD = 0x0003;
	/************表示控件是否成功加载新数据的静态变量**********/
	public static final int LOADDATA_NONET 	 = 0x0000;
	public static final int LOADDATA_SUCCESS = 0x0001;
	public static final int LOADDATA_FAIL    = 0x0002;

	public PullableCScrollView(Context context)
	{
		this(context, null);
	}

	public PullableCScrollView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public PullableCScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		footView = LayoutInflater.from(context).inflate(R.layout.inflater_pulltorefreshloadfooter,null);
		ViewTreeObserver viewTreeObserver = footView.getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
		{
			public void onGlobalLayout()
			{
				footView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				footViewHeight = footView.getHeight();
			}
		});
		footViewImg = (IndicatorView)footView.findViewById(R.id.loadstate_img);
		footViewTv = (TextView)footView.findViewById(R.id.loadstate_tv);
		footViewImgAnim = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.pulltorefreshload_rotatinganim);
		footViewImgAnim.setInterpolator(new LinearInterpolator());
		StopScrollHandler = new StopScrollCallBackHandler();

		/**为控件底部添加FootView并自动加载新内容直到填满一页**/
		new Handler()
		{
			public void handleMessage(Message msg)
			{
				if(getChildAt(0) instanceof LinearLayout)
				{
					addFootView((LinearLayout) getChildAt(0));
					if(getHeight() >=  computeVerticalScrollRange() - footViewHeight)
						loadMoreData();
				}
				else
					Toast.makeText(PullableCScrollView.this.context,"对不起,此控件只支持LinearLayout根布局,请重新布局！",Toast.LENGTH_SHORT).show();
			}
		}.sendEmptyMessageDelayed(0, 200);
	}

	/***************设置控件是否可以上拉，true表示可以上拉，false表示不可以上拉*************/
	public void setCanPullUp(Boolean isCanPullUp)
	{
		this.isCanPullUp = isCanPullUp;
	}
	/***************设置控件是否可以下拉，true表示可以下拉，false表示不可以下拉*************/
	public void setCanPullDown(Boolean isCanPullDown)
	{
		this.isCanPullDown = isCanPullDown;
	}
	/*******************************没有新数据可以加载了************************************/
	public void noDataToLoad()
	{
		changeLoadDataState(LOADSTATE_NODATALOAD);
	}
	/***********************************为控件设置加载监听器********************************/
	public void setOnLoadListener(PullableBLoadListener listener)
	{
		this.mListener = listener;
	}
	/*****************************设置控件是否正处于刷新数据的状态**************************/
	public void setRefresing(Boolean isRefresing)
	{
		this.isRefresing = isRefresing;
		if(!isRefresing)
			loadMoreData();
	}
	/*********************为PullableScrollView唯一子类添加其相关的内部View******************/
	public void addInsideChildView(View childView)
	{
		if(getChildAt(0) instanceof LinearLayout)
		{
			LinearLayout linearLayout = (LinearLayout)getChildAt(0);
			deleteFootView(linearLayout);
			linearLayout.addView(childView);
			addFootView(linearLayout);
		}
		else
			Toast.makeText(context,"对不起,此控件只支持LinearLayout根布局,请重新布局！",Toast.LENGTH_SHORT).show();
	}
	/****结束控件加载新数据的流程,Params等于0表示无可用网络,等于1表示成功,等于2表示失败*****/
	public void loadingFinish(Integer whetherToComplete)
	{
		if(whetherToComplete == LOADDATA_NONET)
		{
			changeLoadDataState(LOADSTATE_ALREADYLOAD);
		}
		else if(whetherToComplete == LOADDATA_SUCCESS)
		{
			changeLoadDataState(LOADSTATE_ALREADYLOAD);
		}
		else if(whetherToComplete == LOADDATA_FAIL)
		{
			changeLoadDataState(LOADSTATE_ALREADYLOAD);
			Toast.makeText(context,"亲，加载新数据失败了哟！",Toast.LENGTH_SHORT).show();
		}

		/********是否拥有可用网络！有网络且当前内容不满一页，控件则自动加载新内容填满一页*******/
		if(NetUtils.WhetherConnectNet(context))
		{
			/***当内容不满一页时,控件自动加载新内容直到填满一页！排除没有新数据可以加载的情况***/
			if (getHeight() >= computeVerticalScrollRange() - footViewHeight)
			{
				new Handler()
				{
					public void handleMessage(Message msg)
					{
						isTouching = false;
						loadMoreData();
					}
				}.sendEmptyMessageDelayed(0, 200);
			}
		}
	}


	/*****************判断控件是否可以上拉，可以上拉返回true，否则返回false*****************/
	public Boolean getCanPullUp()
	{
		if(isCanPullUp)
		{
			if (getScrollY() + getHeight() >=  computeVerticalScrollRange() - footViewHeight)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	/*************判断控件是否可以下拉，可以下拉返回true，否则返回false*********************/
	public Boolean getCanPullDown()
	{
		if(isCanPullDown)
		{
			if (getScrollY() == 0 && loadDataState != LOADSTATE_LOADING)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	/*********************************在该控件底部添加加载栏********************************/
	private void addFootView(LinearLayout linearLayout)
	{
		if (isCanPullUp)
		{
			linearLayout.addView(footView, linearLayout.getChildCount());
		}
	}
	/*********************************删除该控件底部的加载栏********************************/
	private void deleteFootView(LinearLayout linearLayout)
	{
		if (isCanPullUp)
		{
			linearLayout.removeView(footView);
		}
	}
	/****************************************手势监听***************************************/
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getActionMasked())
		{
			/*****这里保证用户必须松开手指才能正常加载数据******/
			case MotionEvent.ACTION_DOWN:isTouching = true;break;
			case MotionEvent.ACTION_UP:
			{
				isTouching = false;
				/*****当该控件没有填满一页并且需要加载新内容的时候，必须通过*****/
				/*****************"点击加载更多"按钮去加载新数据*****************/
				if(getHeight() >=  computeVerticalScrollRange() - footViewHeight)
				{
					if (event.getY() >= footView.getTop())
						loadMoreData();
				}
				else
					loadMoreData();
				break;
			}
		}
		return super.onTouchEvent(event);
	}
	/****************************************滚动监听***************************************/
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		if(!isScrolling)
		{
			isScrolling = true;
			StopScrollHandler.sendEmptyMessage(0);
		}
	}
	/**************************************加载更多数据*************************************/
	private synchronized void loadMoreData()
	{
		/**加载新数据时必须满足:1.当前不处于正在刷新状态。2.当前不处于正在加载状态并且必须有数据可以加载。3.手指必须离开屏幕4.当前控件*/
		/*******必须处于没有滚动的状态。5.允许采用上拉加载方式去加载数据并且当前处于控件底部。6.必须为该控件设置上拉加载的监听器*******/
		if (!isRefresing && loadDataState == LOADSTATE_ALREADYLOAD && !isTouching && !isScrolling && getCanPullUp()  && mListener != null)
		{
			changeLoadDataState(LOADSTATE_LOADING);
			mListener.onStartLoading();
		}
	}
	/**********************************改变控件当前加载状态*********************************/
	private void changeLoadDataState(int loadDataState)
	{
		this.loadDataState = loadDataState;
		switch (loadDataState)
		{
			case LOADSTATE_ALREADYLOAD:
			{
				footViewImg.hideViewGone();
				footViewTv.setText("点击加载更多");
				break;
			}

			case LOADSTATE_LOADING:
			{
				footViewImg.showView();
				footViewTv.setText("拼命加载数据中...");
				break;
			}

			case LOADSTATE_NODATALOAD:
			{
				footViewImg.hideViewGone();
				footViewTv.setText("亲，没有数据可以挖了！");
				break;
			}
		}
	}
	/*********************当控件由动态变为静态时,控件内部指定使用的回调Handler**************/
	private class StopScrollCallBackHandler extends Handler
	{
		public void handleMessage(android.os.Message msg)
		{
			int scrollNewY = PullableCScrollView.this.getScrollY();
			if(lastScrollY != scrollNewY)
			{
				lastScrollY = scrollNewY;
				this.sendEmptyMessageDelayed(0,66);
			}
			else
			{
				isScrolling = false;
				loadMoreData();
			}
		}
	}
}