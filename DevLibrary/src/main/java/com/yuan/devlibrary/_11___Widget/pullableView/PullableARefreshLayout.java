package com.yuan.devlibrary._11___Widget.pullableView;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yuan.devlibrary.R;
import com.yuan.devlibrary._11___Widget.indicator.IndicatorView;

public class PullableARefreshLayout extends RelativeLayout
{
	/***********************环境变量*********************/
	private Context context;
	/**该控件是否是第一次布局,true代表是,false代表不是***/
	private boolean isFirstLayout = true;
	/************实现Pullable接口的View控件**************/
	private View pullableView;
	/********************下拉头的整体********************/
	private View refreshAllView;
	/********************下拉头的箭头********************/
	private View refreshPullView;
	/*************下拉头"正在刷新"时的刷新图标***********/
	private IndicatorView refreshingView;
	/************下拉刷新过程中的字体提示控件************/
	private TextView refreshStateTextView;
	/*********下拉头刷新后的结果图标，即打钩的图标*******/
	private View refreshStateImageView;
	/***************下拉头Header下拉的距离***************/
	public float pullDownY = 0;
	/****"下拉刷新"达到要求的最短下拉距离,此值为默认值***/
	private float refreshDist = 200;
	/*******下拉刷新成功后Header默认的最慢回滚速度*******/
	public float MOVE_SPEED = 8;
	/******手指头滑动距离与下拉头滑动距离的比值,中间随正切函数变化,这里就是滑动时的最小摩擦力******/
	private float radio = 2;
	/***********************刷新操作完毕之后显示结果所用的时间,单位:毫秒.**************************/
	private int showResultTime = 600;
	/***************downY和lastY是第一次触摸屏幕的Y坐标(即上一次触摸屏幕的点的Y坐标)***************/
	private float downY, lastY;
	/**********过滤多点触控,即发生多点触控时始终只受第一个触控点的控制，其余的触控点均无效*********/
	private int mEvents;
	/**********在刷新过程中是否还产生了其他的滑动操作(包括滑动Header，ContentView两个部分)"********/
	private boolean isTouch = false;
	/*********************************更新界面情况所用的Handler类**********************************/
	private MyTimer timer;
	/********************************准备刷新时下拉箭头旋转的180°动画*****************************/
	private RotateAnimation rotate180Animation;
	/******************************正在刷新时旋转图标旋转的的360°动画*****************************/
	private RotateAnimation rotate360Animation;


	/************************************当前控件处于的使用状态************************************/
	private int state = INIT;
	/**************************************控件处于初始状态****************************************/
	public static final int INIT = 0;
	/*************************************控件处于释放刷新的状态***********************************/
	public static final int RELEASE_TO_REFRESH = 1;
	/************************************控件处于正在刷新的状态************************************/
	public static final int REFRESHING = 2;
	/**********************************控件处于完成一个操作的状态**********************************/
	public static final int DONE = 3;
	/*********************************************刷新成功*****************************************/
	public static final int SUCCEED = 0;
	/*********************************************刷新失败*****************************************/
	public static final int FAIL = 1;
	/*************************************刷新成功后进行回调的接口*********************************/
	private PullableBRefreshListener mListener;


	public PullableARefreshLayout(Context context)
	{
		super(context);
		initView(context);
	}

	public PullableARefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	public PullableARefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context);
	}
	
	/*************************************初始化控件整体的布局*************************************/
	private void initView(Context context)
	{
		this.context = context;
		timer = new MyTimer(updateHandler);
		rotate180Animation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.pulltorefreshload_reversinganim);
		rotate360Animation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.pulltorefreshload_rotatinganim);
		/********************添加匀速转动动画******************/
		LinearInterpolator lir = new LinearInterpolator();
		rotate180Animation.setInterpolator(lir);
		rotate360Animation.setInterpolator(lir);
	}
	
	/*************************************控件本身的自定义布局*************************************/
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		if (isFirstLayout)
		{
			/******************控件第一次显示需要做的初始化操作***************/
			refreshAllView = getChildAt(0);
			pullableView = getChildAt(1);
			isFirstLayout = false;
			initInternalView();
			refreshDist = ((ViewGroup) refreshAllView).getChildAt(0).getMeasuredHeight();
		}
		/************************Header下拉刷新布局定位************************/
		refreshAllView.layout(0, (int) pullDownY - refreshAllView.getMeasuredHeight(), refreshAllView.getMeasuredWidth(), (int) pullDownY);
		/***********************ContentView中间内容布局定位********************/
		pullableView.layout(0, (int) pullDownY, pullableView.getMeasuredWidth(), (int) pullDownY + pullableView.getMeasuredHeight());
	}
	
	/**************************************控件本身的自定义布局************************************/
	private void initInternalView()
	{
		/************************初始化下拉刷新部分中的***********************/
		refreshPullView = refreshAllView.findViewById(R.id.pull_icon);
		refreshingView = (IndicatorView)refreshAllView.findViewById(R.id.refreshing_icon);
		refreshStateTextView = (TextView) refreshAllView.findViewById(R.id.state_tv);
		refreshStateImageView = refreshAllView.findViewById(R.id.state_iv);
	}

	/*******************刷新操作完毕,显示刷新结果.注意:刷新完成后一定要调用这个方法****************/
	public void refreshFinish(int refreshResult)
	{
		refreshingView.hideViewGone();
		switch (refreshResult)
		{
			case SUCCEED:
			{
				refreshStateTextView.setText(R.string.refresh_succeed);
				refreshStateImageView.setVisibility(View.VISIBLE);
				refreshStateImageView.setBackgroundResource(R.drawable.pulltorefreshsucceed);
				break;
			}

			case FAIL:
			{
				refreshStateTextView.setText(R.string.refresh_fail);
				refreshStateImageView.setVisibility(View.VISIBLE);
				refreshStateImageView.setBackgroundResource(R.drawable.pulltorefreshfailed);
				break;
			}
			default:break;
		}

		/**刷新操作完成后,显示刷新结果**/
		if(pullDownY > 0)
		{
			new Handler()
			{
				public void handleMessage(Message msg)
				{
					changeState(DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0,showResultTime);
		}
		/**刷新操作完成后,不显示刷新结果**/
		else
		{
			changeState(DONE);
			hide();
		}
	}

	/***********************隐藏下拉刷新头的Header或则隐藏上拉加载头的Footer***********************/
	private void hide()
	{
		timer.schedule(6);
	}
	
	/************************************改变控件当前的刷新状态************************************/
	private void changeState(int state)
	{
		this.state = state;
		switch (state)
		{
			case INIT:
			{
				/**************************下拉头初始化**********************/
				refreshStateImageView.setVisibility(View.GONE);
				refreshPullView.clearAnimation();
				refreshPullView.setVisibility(View.VISIBLE);
				refreshStateTextView.setText(R.string.pull_to_refresh);
				break;
			}
			case RELEASE_TO_REFRESH:
			{
				/************************释放刷新状态************************/
				refreshPullView.startAnimation(rotate180Animation);
				refreshStateTextView.setText(R.string.release_to_refresh);
				break;
			}
			case REFRESHING:
			{
				/************************正在刷新状态************************/
				refreshPullView.clearAnimation();
				refreshPullView.setVisibility(View.INVISIBLE);
				refreshingView.showView();
				refreshStateTextView.setText(R.string.refreshing);
				break;
			}
			case DONE:
			{
				/***********************刷新操作完毕**************************/
				break;
			}
		}
	}

	/*****************由父控件决定是否分发事件,防止事件冲突,同时在这里过滤多点触控*****************/
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		switch (event.getActionMasked())
		{
			case MotionEvent.ACTION_DOWN:
			{
				downY = event.getY();
				lastY = downY;
				timer.cancel();
				mEvents = 0;
				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_POINTER_UP:
			{
				/********************************这里过滤多点触碰**********************************/
				mEvents = -1;
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				/**************************保证控件当前只受一个手势的控制**************************/
				if (mEvents == 0)
				{
					/*********************************刷新数据操作*********************************/
					if (((Pullable)pullableView).getCanPullDown())
					{
						/****************这里对实际滑动距离做缩小，造成用力拉的感觉****************/
						pullDownY = pullDownY + (event.getY() - lastY) / radio;
						if (pullDownY < 0)
							pullDownY = 0;
						if (pullDownY > getMeasuredHeight())
							pullDownY = getMeasuredHeight();
						if (state == REFRESHING)
						{
							/****************************正在刷新时触摸移动************************/
							isTouch = true;
						}
					}
				}
				else
					mEvents = 0;

				lastY = event.getY();
				/********************根据下拉距离改变摩擦力值,造成用力拉的感觉*********************/
				radio = (float)(2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * pullDownY));
				/************************************刷新数据的操作********************************/
				if (pullDownY > 0)
				{
					requestLayout();
					if (pullDownY < refreshDist && (state == RELEASE_TO_REFRESH || state == DONE))
					{
						/*如果下拉距离没达到刷新的距离并且当前显示状态的文字是释放刷新,改变状态文字为下拉刷新*/
						changeState(INIT);
					}
					if (pullDownY >= refreshDist && state == INIT)
					{
						/**如果下拉距离达到刷新的距离并且当前显示状态的文字是下拉刷新,改变状态文字为释放刷新**/
						changeState(RELEASE_TO_REFRESH);
					}
				}

				if (pullDownY > 8)
				{
					/*******************防止下拉刷新过程中误触发长按事件和点击事件*****************/
					event.setAction(MotionEvent.ACTION_CANCEL);
				}
				break;
			}

			case MotionEvent.ACTION_UP:
			{
				/************************正在刷新时往下拉,释放后下拉头不隐藏***********************/
				if (pullDownY > refreshDist)
					isTouch = false;

				if (state == RELEASE_TO_REFRESH)
				{
					/**********************************释放刷新操作********************************/
					changeState(REFRESHING);
					if (mListener != null)
						mListener.onStartRefreshing(this);
				}
				hide();
				break;
			}
			default:break;
		}
		/**********事件分发交给父类*********/
		super.dispatchTouchEvent(event);
		return true;
	}

	/*************************************模拟手指滑动的Task***************************************/
	private class AutoRefreshTask extends AsyncTask<Integer, Float, String>
	{
		/**************************后台执行的操作************************/
		protected String doInBackground(Integer... params)
		{
			while (pullDownY <  refreshDist)
			{
				pullDownY += MOVE_SPEED;
				publishProgress(pullDownY);
				try{Thread.sleep(params[0]);} 
				catch (InterruptedException e){e.printStackTrace();}
			}
			return null;
		}

		/****在doInBackground()里通过publishProgress()回调该方法更新UI内容***/
		protected void onProgressUpdate(Float... values)
		{
			if (pullDownY > refreshDist)
				changeState(RELEASE_TO_REFRESH);
			requestLayout();
		}
		
		/************doInBackground()执行完毕后回调该方法更新UI内容***********/
		protected void onPostExecute(String result)
		{
			changeState(REFRESHING);
			if (mListener != null)
				mListener.onStartRefreshing(PullableARefreshLayout.this);
			hide();
		}
	}

	/***********************************更新界面情况所用的Handler类********************************/
	private class MyTimer
	{
		private Handler handler;
		private Timer timer;/**timer(计时器),用于特定的时间启动指定的任务**/
		private MyTask task;/****task(计时器任务),计时器启动的指定任务****/

		public MyTimer(Handler handler)
		{
			this.handler = handler;
			timer = new Timer();
		}

		/*******根据间隔时间执行此次任务********/
		public void schedule(long period)
		{
			if (task != null)
			{
				task.cancel();
				task = null;
			}
			task = new MyTask(handler);
			timer.schedule(task, 0, period);
		}

		/***********取消执行的任务***********/
		public void cancel()
		{
			if (task != null)
			{
				task.cancel();
				task = null;
			}
		}

		/********指定执行的操作任务********/
		class MyTask extends TimerTask
		{
			private Handler handler;
			public MyTask(Handler handler)
			{
				this.handler = handler;
			}

			/**Handler通过obtainMessage()获取的消息已经拥有发送的目的地对象,我们再通过Msg.sendToTarget()把消息本身发送给目的地对象**/
			public void run()
			{
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	/*****************************下拉刷新之后,使控件自动进行回滚的Handler*************************/
	private Handler updateHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			/************************回弹速度随下拉距离pullDownY增大而增大*************************/
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * pullDownY));
			if (!isTouch)
			{
				/****************正在刷新，且没有往上推的话则悬停，显示"正在刷新..."***************/
				if (state == REFRESHING && pullDownY <= refreshDist)
				{
					pullDownY = refreshDist;
					timer.cancel();
				}
			}
			/********************************下拉头下拉距离自减,造成回滚效果***********************/
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			/********************************刷新结束,上拉头已完成回滚效果*************************/
			if (pullDownY <= 0)
			{
				pullDownY = 0;
				refreshPullView.clearAnimation();
				/***********隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新才可改变状态*******/
				if (state != REFRESHING)
					changeState(INIT);
				timer.cancel();
			}
			/***调用requestLayout(),让系统自动调用onLayout()刷新当前布局**/
			requestLayout();
		}
	};

	/*******************************以下是给予外部调用的方法***************************************/
	/****************************************模拟自动刷新操作**************************************/
	public void autoRefresh()
	{
		AutoRefreshTask task = new AutoRefreshTask();
		task.execute(22);
	}

	/********************************设置操作结果显示时间,单位：毫秒*******************************/
	public void setShowResultTime(int showResultTime)
	{
		this.showResultTime = showResultTime;
	}

	/*********************************为获取刷新操作结果设置监听器*********************************/
	public void setOnPullRefreshListener(PullableBRefreshListener listener)
	{
		mListener = listener;
	}
}