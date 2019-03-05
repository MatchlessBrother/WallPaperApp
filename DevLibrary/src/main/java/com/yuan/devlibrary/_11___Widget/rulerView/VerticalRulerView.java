package com.yuan.devlibrary._11___Widget.rulerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yuan.devlibrary.R;

public class VerticalRulerView extends RelativeLayout
{
	private Context mContext;
	private ScrollView mScrollView;
	private LinearLayout mContentView;
	private Integer mDirection;/**度量尺放置的方位:left,right*/
	private Integer mMinValue;/**度量尺最小值*/
	private Integer mMaxValue;/**度量尺最大值*/
	private Integer mCurrentValue;/**度量尺当前值*/
	private Boolean mAlreadyInitView;/**度量尺是否已经初始化,防止外部调用者的onWindowFocusChanged()函数重复调用引起的该控件重复初始化*/
	
	private int mOldTouchValue = 0;/**旧的滚轮值,判定该控件是否仍在滚动*/
	private final int bigGridSize = 200;/**默认一大格的距离值*/
	private final int smallGridSize = bigGridSize / 10;/**默认一小格的距离值*/
	private onScrollListener mListener;/**对外暴露给用户,获取滚动值的接口变量*/
	
	/**不管用何种方式滑动控件，都能准确的获取到控件的最后结果值*/
	private Handler mLeaveTouchHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			/**表示ScrollView停止滚动*/
			if(mOldTouchValue == mScrollView.getScrollY())
			{
				mCurrentValue = mMinValue+ (int) Math.ceil(mScrollView.getScrollY() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
			}
			
			else/**表示ScrollView仍在滚动*/
			{
				mOldTouchValue = mScrollView.getScrollY();
				mCurrentValue = mMinValue+ (int) Math.ceil(mScrollView.getScrollY() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
				mLeaveTouchHandler.sendEmptyMessageDelayed(0, 100);
			}
		}
	};

	/*************直接沿用默认的属性值*************/
	public VerticalRulerView(Context context) 
	{
		this(context,null);
	}

	/*******获取xml布局时指定的属性值,如果没有指定确定的属性值,则沿用默认的属性值********/
	public VerticalRulerView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalRulerview);
		mDirection = typedArray.getInteger(R.styleable.VerticalRulerview_vDirections, 1);
		mMinValue = typedArray.getInteger(R.styleable.VerticalRulerview_vMinValue, 0);
		mMaxValue = typedArray.getInteger(R.styleable.VerticalRulerview_vMaxValue, 200);
		mCurrentValue = typedArray.getInteger(R.styleable.VerticalRulerview_vCurrentValue, 60);
		mAlreadyInitView = false;
		typedArray.recycle();
	}

	/****************初始化该控件的内部控件,并监听其所有的手势操作****************/
	/**在使用此控件的Act中,在其onWindowFocusChanged()方法中调用该函数便可初始化度量尺的界面*/
	public void initView(int parentHeight)
	{
		if(!mAlreadyInitView)
		{
			View  view = null;
			switch (mDirection) 
			{
				case 1:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleft,this,true);break;
				case 2:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewright,this,true);break;
			}
			mScrollView = (ScrollView)view.findViewById(R.id.ruler_sv);
			mContentView = (LinearLayout)view.findViewById(R.id.ruler_ll); 
			initRuler(parentHeight);
			
			mScrollView.setOnTouchListener(new OnTouchListener()
			{
				public boolean onTouch(View v, MotionEvent event)
				{
					int action = event.getAction();
					switch (action) 
					{
						case MotionEvent.ACTION_DOWN:
						case MotionEvent.ACTION_MOVE:
						case MotionEvent.ACTION_UP:
						mLeaveTouchHandler.sendEmptyMessageDelayed(0, 100);break;
					}return false;
				}
			});
			mAlreadyInitView = true;
		}
	}
	
	/**************初始化度量尺的界面***********/
	private void initRuler(int parentHeight)
	{
		/**强制确保使用此控件时Xml属性值的正确性和可用性*/
		/**取最近的最小的整数值*/
		mMinValue = mMinValue/10*10;
		/**取最近的最大的整数值*/
		mMaxValue = mMaxValue%10 > 0 ? mMaxValue/10*10+10 : mMaxValue/10*10;
		/**确保最大值和最小值至少保持10个大小的距离*/
		if(mMinValue >= mMaxValue)
			mMaxValue = mMinValue+10;
		/**确保当前值在最大值和最小值之间*/
		if(mCurrentValue < mMinValue)
			mCurrentValue = 0;
		else if(mCurrentValue > mMaxValue)
			mCurrentValue = mMaxValue - mMinValue;
		else
			mCurrentValue = mCurrentValue - mMinValue;
		
		int InitValue = mMinValue;
		if(mDirection == 1)/**左边布局*/
		{
		   /************************************************上部分********************************************************/
		   View topViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   ImageView nullImg1 =  (ImageView)topViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewl_null);
		   nullImg1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,parentHeight/2-bigGridSize/2));
		   mContentView.addView(topViewNullBegin);
		   View topViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   TextView beginTv = (TextView)topViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(InitValue+"");
		   ImageView beginImg =  (ImageView)topViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewl_begin);
		   beginImg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,bigGridSize));
		   mContentView.addView(topViewBegin);			   
		   
		   /************************************************中部分********************************************************/
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10) 
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(InitValue+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewl_content);
			   contentImg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,bigGridSize));
			   mContentView.addView(viewContent);
		   }
				
		   /************************************************下部分********************************************************/
		   View bottomViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   TextView endTv = (TextView)bottomViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(InitValue+"");
		   ImageView endImg =  (ImageView)bottomViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewl_end);		  
		   endImg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,bigGridSize));
		   mContentView.addView(bottomViewEnd);
		   View bottomViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   ImageView nullImg2 =  (ImageView)bottomViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewl_null);
		   nullImg2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,parentHeight/2-bigGridSize/2));
		   mContentView.addView(bottomViewNullEnd);
		}
		
		
		else/**右边布局*/
		{
		   /************************************************上部分********************************************************/
		   View topViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   ImageView nullImg1 =  (ImageView)topViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewr_null);
		   LinearLayout.LayoutParams  nullParams1 = (LinearLayout.LayoutParams)nullImg1.getLayoutParams();
		   nullParams1.height = parentHeight/2-bigGridSize/2;
		   nullImg1.setLayoutParams(nullParams1);
		   mContentView.addView(topViewNullBegin);
		   View topViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   TextView beginTv = (TextView)topViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(InitValue+"");
		   ImageView beginImg =  (ImageView)topViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewr_begin);
		   LinearLayout.LayoutParams  BeginParams = (LinearLayout.LayoutParams)beginImg.getLayoutParams();
		   BeginParams.height = bigGridSize;
		   beginImg.setLayoutParams(BeginParams);
		   mContentView.addView(topViewBegin);			   
		   
		   /************************************************中部分********************************************************/
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10) 
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(InitValue+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewr_content);
			   LinearLayout.LayoutParams  contentParams = (LinearLayout.LayoutParams)contentImg.getLayoutParams();
			   contentParams.height = bigGridSize;
			   contentImg.setLayoutParams(contentParams);
			   mContentView.addView(viewContent);
		   }
				
		   /************************************************下部分********************************************************/
		   View bottomViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   TextView endTv = (TextView)bottomViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(InitValue+"");
		   ImageView endImg =  (ImageView)bottomViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewr_end);		  
		   LinearLayout.LayoutParams  endParams = (LinearLayout.LayoutParams)endImg.getLayoutParams();
		   endParams.height = bigGridSize;
		   endImg.setLayoutParams(endParams);
		   mContentView.addView(bottomViewEnd);
		   View bottomViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   ImageView nullImg2 =  (ImageView)bottomViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewr_null);
		   LinearLayout.LayoutParams  nullParams2 = (LinearLayout.LayoutParams)nullImg2.getLayoutParams();
		   nullParams2.height = parentHeight/2-bigGridSize/2;
		   nullImg2.setLayoutParams(nullParams2);
		   mContentView.addView(bottomViewNullEnd);
		}	
		
		/**初始化指针指向当前的刻度值*/
		new Handler()
		{
			public void handleMessage(Message msg) 
			{
				mScrollView.smoothScrollTo(0, mCurrentValue * smallGridSize);
				if(mListener != null)
					mListener.onScroll(mMinValue+ (int) Math.ceil(mScrollView.getScrollY() / smallGridSize));
			}
		}.sendEmptyMessageDelayed(0, 200);
	}
	
	/******对外暴露的接口:外部获取该控件滚动时的值******/
	public interface onScrollListener
	{
		public void onScroll(int currentValue);
	}
	
	/******对外暴露的接口:外部获取该控件滚动时的值******/
	public void setOnScrollListener(onScrollListener listener)
	{
		mListener = listener;
	}
}