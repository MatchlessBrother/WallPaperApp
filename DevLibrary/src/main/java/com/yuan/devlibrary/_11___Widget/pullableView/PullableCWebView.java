package com.yuan.devlibrary._11___Widget.pullableView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;
import com.yuan.devlibrary.R;

public class PullableCWebView extends LinearLayout implements Pullable
{
	/**此控件还有一个缺点:在某些具有音乐或则视频的页面进行**/
	/**后退的时候,这个时候并没有正确的关闭掉当前音乐或则视**/
	/**频,所以该控件还有待改进,暂时在使用此控件Activity的***/
	/**onDestroy()里面调用WebView.webViewDestroy()方法进行**/
	/****************关闭声音但是切记并不通用!**************/
	/**判定当前控件是否可以下拉刷新数据和上拉加载数据的变量*/
	private Boolean isCanPullDown = false;
	/**这个view是外部提供的刷新按钮,因为有某些网页不支持下拉刷新,所以***/
	/**一旦遇到这种网页就关闭下拉刷新功能,而采用手动刷新的方式进行刷新**/
	private View view = null;
	/***************************显示网页的WebView***********************/
	private WebView webView = null;
	/***********************显示网页加载进度的进度条********************/
	private ProgressBar progressBar = null;
	/****************监听加载网页或则刷新网页是否完毕的监听器***********/
	private OnRefreshListener refreshListener = null; 
	private static final int DISPLAY_PROGRESSBAR = 1;
	
	public PullableCWebView(Context context)
	{
		this(context,null);
	}

	public PullableCWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		View allView = LayoutInflater.from(context).inflate(R.layout.inflater_pullablewebview,this,true);
		webView = (WebView)allView.findViewById(R.id.pullablewebview_webview);
		progressBar = (ProgressBar)allView.findViewById(R.id.pullablewebview_progressbar);
		/*****geturl()获取本网页地址,getoriginalurl()获取根网页地址****/
		webView.getSettings().setAllowFileAccess(true);//允许网页访问本地数据.
		webView.getSettings().setJavaScriptEnabled(true);//允许访问包含javascript脚本的网页.
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//允许网页自动弹出javascript脚本窗口.
		webView.getSettings().setDomStorageEnabled(true);//允许用户与当前网页进行交互,比如点击播放音乐等.
		webView.getSettings().setSupportZoom(true);//支持缩放，默认true.
		webView.getSettings().setBuiltInZoomControls(true);//使用webview内部的缩放方式.默认false.
		webView.getSettings().setUseWideViewPort(true);// 允许网页中包含视图.
		webView.getSettings().setLoadWithOverviewMode(true);// 让网页内容的宽度自动适配手机屏幕的宽度.
		webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染的优先级
		webView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));//设置默认的背景颜色
		webView.setFocusable(true);//确保点击输入框能够弹出软键盘
		webView.setFocusableInTouchMode(true);//确保点击输入框能够弹出软键盘
		if(android.os.Build.VERSION.SDK_INT > 10)//隐藏缩放控件,但是支持手势缩放
			webView.getSettings().setDisplayZoomControls(false);
		else
			hideZoomController(webView);

		/* 此方法只限于activity中用来适配网页到屏幕大小的作用. DisplayMetrics metrics = new
		 * DisplayMetrics();
		 * getWindowManager().getDefaultDisplay().getMetrics(metrics); int
		 * mDensity = metrics.densityDpi;
		 *
		 *
		 * if (mDensity == 120) { settings.setDefaultZoom(ZoomDensity.CLOSE); }
		 * else if (mDensity == 160) {
		 * settings.setDefaultZoom(ZoomDensity.MEDIUM); } else if (mDensity
		 * ==240) { settings.setDefaultZoom(ZoomDensity.FAR); }*/

		/********主要帮助WebView处理各种通知,请求事件********/
		webView.setWebViewClient(new WebViewClient()
		{
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return false;
			}
		});

		/*******主要帮助WebView处理JS的脚本以及加载进度等等********/
		webView.setWebChromeClient(new WebChromeClient()
		{
			public void onProgressChanged(WebView view, int newProgress)
			{
				super.onProgressChanged(view, newProgress);
				if (progressBar != null)
				{
					progressBar.setProgress(newProgress);
					if (newProgress < 100)
						progressBar.setVisibility(View.VISIBLE);
					else
					{
						Message msg = new Message();
						msg.what = 1;
						new ProgressHandler().sendMessageDelayed(msg, 120);
					}
				}
			}
		});

		/*******第一次加载网页时确定其是否支持下拉刷新属性***********/
		new Handler()
		{
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				if(getVerticalScrollRange() == getVerticalScrollExtent())
					view.setVisibility(View.VISIBLE);
				else
					view.setVisibility(View.INVISIBLE);
			}
		}.sendEmptyMessageDelayed(0, 1200);
	}
	
	/*********隐藏webView自带的缩放控件 ,针对Android3.0之前的版本********/
	private void hideZoomController(View view)
	{
	   Class classType;
	   Field field;
	   try
	   {
	       classType = WebView.class;
	       field = classType.getDeclaredField("mZoomButtonsController");
	       field.setAccessible(true);
	       ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
	       mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
	       try
	       {
	          field.set(view, mZoomButtonsController);
	       }
	       catch (IllegalArgumentException e) 
	       {
	    	   e.printStackTrace();
	       }
	       catch (IllegalAccessException e)
	       {
	           e.printStackTrace();
	       }
	    }
	   	catch (SecurityException e)
	   	{
	       e.printStackTrace();
	    } 
	   	catch (NoSuchFieldException e)
	   	{
	       e.printStackTrace();
	    }
    }
	
	/**开始加载页面的时候显示进度条 ,加载页面结束后隐藏进度条的Handler类*/
	private class ProgressHandler extends Handler
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == DISPLAY_PROGRESSBAR)
			{
				refreshListener.completeRefresh();
				progressBar.setVisibility(View.GONE);
				new Handler()
				{
					public void handleMessage(Message msg) 
					{
						super.handleMessage(msg);
						if(getVerticalScrollRange() == getVerticalScrollExtent())
							view.setVisibility(View.VISIBLE);
						else
							view.setVisibility(View.INVISIBLE);
					}
				}.sendEmptyMessageDelayed(0, 1200);
			}
		}
	}	
	
	/*********判断控件是否可以下拉,可以下拉返回true,否则返回false********/
	public Boolean getCanPullDown()
	{
		if(isCanPullDown)
		{
			/****增加getVerticalScrollRange()!=getVerticalScrollExtent()是为了判定网页是否支持下拉刷新属性*****/
			if (getVerticalScrollRange() != getVerticalScrollExtent() && webView.getScrollY() == 0)
				return true;
			else
				return false;
		}
		else 
			return false;
	}

	/*********判断控件是否可以上拉,可以上拉返回true,否则返回false********/
	public Boolean getCanPullUp()
	{
		return false;
	}
	
	/******************获取当前网页垂直滚动的全部范围值******************/
	private int getVerticalScrollRange()
	{
		 int resultNum = 0;
		 Class clazz = null;
		 Method method = null;
		 try 
		 {
			clazz = WebView.class;
			method = clazz.getDeclaredMethod("computeVerticalScrollRange", new Class[]{});
			method.setAccessible(true);
			try 
			{
				resultNum = (Integer) method.invoke(webView, new Object[]{});
			}
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e) 
			{
				e.printStackTrace();
			}
		 } 
		catch (NoSuchMethodException e) 
		{
			e.printStackTrace();
		}
		return resultNum;
	}
	
	/******************获取当前网页垂直滚动的额外范围值******************/
	private int getVerticalScrollExtent()
	{
		 int resultNum = 0;
		 Class clazz = null;
		 Method method = null;
		
		 try 
		 {
			clazz = WebView.class;
			method = clazz.getDeclaredMethod("computeVerticalScrollExtent", new Class[]{});
			method.setAccessible(true);
			try 
			{
				resultNum = (Integer) method.invoke(webView, new Object[]{});
			}
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e) 
			{
				e.printStackTrace();
			}
		 } 
		catch (NoSuchMethodException e) 
		{
			e.printStackTrace();
		}
		return resultNum;
	}


	/********************************************************************/
	/*********************以下是使用此控件-必须调用的函数****************/
	/******确保在使用webView的时候能够正确弹出软键盘并且不遮挡输入框*****/
	public void popUpSoftwareDisk(Activity activity)
	{
		activity.getWindow().setSoftInputMode(
		WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|
		WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); 
	}

	/*********************获取外部提供的手动刷新按钮*********************/
	public void setRefreshView(View view)
	{
		this.view = view;
	}

	/**确保webView在加载页面完毕时或则刷新页面完毕时,内部会通过传递进来的OnRefreshListener***/
	/**********对象利用回调机制告诉外部加载完毕或则刷新完毕，以便及时进行UI的交互************/
	public void setOnRefreshListener(OnRefreshListener refreshListener)
	{
		this.refreshListener = refreshListener;
	}
	/*********************以上是使用此控件-必须调用的函数****************/
	/********************************************************************/


	/*******设置控件是否可以下拉,true表示可以下拉,false表示不可以下拉****/
	public void setCanPullDown(Boolean isCanPullDown)
	{
		this.isCanPullDown = isCanPullDown;
	}
	
	/*******设置控件是否可以上拉,true表示可以上拉,false表示不可以上拉****/
	public void setCanPullUp(Boolean isCanPullUp)
	{
		
	}
	
	/*******************设置webView加载指定  的Url网页*******************/
	public void  webViewLoadUrl(String urlStr)
	{
		webView.loadUrl(urlStr);
	}
	
	/*********************设置webView重新加载当前的网页******************/
	public void  webViewReload()
	{
		webView.reload();
	}
	
	/*************************清空浏览器缓存资料*************************/
	public void webViewClearCache()
	{
		webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
	}


	/**在利用PullAbleWebView的页面中点击后退按钮,这个时候必须调用此方法判定*/
	/**PullAbleWebView是否有可以回退的页面,有返回true,没有返回false,********/
	/**同时相关的Activity页面根据返回值判定是否finish当前Activity页面*******/
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack())
		{
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
				webView.onPause();
			webView.goBack();
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return false;
		}
		else if (keyCode == KeyEvent.KEYCODE_FORWARD && webView.canGoForward())
		{
			webView.goForward();
			return true;
		}

		else if (keyCode == KeyEvent.KEYCODE_FORWARD)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/*************此接口用于监听该webView刷新是否完毕，如果完毕*************/
	/**************则会自动调用completeRefresh函数以便进行交互**************/
	public interface OnRefreshListener
	{
		public void completeRefresh();
	}
}