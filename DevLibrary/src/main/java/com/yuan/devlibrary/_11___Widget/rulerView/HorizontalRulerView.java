package com.yuan.devlibrary._11___Widget.rulerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuan.devlibrary.R;

public class HorizontalRulerView extends RelativeLayout
{
	private Context mContext;
	private LinearLayout mContentView;
	private HorizontalScrollView mHorizontalScrollView;
	private Integer mDirection;/**度量尺放置的方位:top,bottom*/
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
			/**表示HorizontalScrollView停止滚动*/
			if(mOldTouchValue == mHorizontalScrollView.getScrollX())
			{
				mCurrentValue = mMinValue+ (int) Math.ceil(mHorizontalScrollView.getScrollX() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
			}
			
			else/**表示HorizontalScrollView仍在滚动*/
			{
				mOldTouchValue = mHorizontalScrollView.getScrollX();
				mCurrentValue = mMinValue+ (int) Math.ceil(mHorizontalScrollView.getScrollX() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
				mLeaveTouchHandler.sendEmptyMessageDelayed(0, 100);
			}
		}
	};
	
	/***************直接沿用默认的属性值**************/
	public HorizontalRulerView(Context context) 
	{
		this(context,null);
	}

	/*******获取xml布局时指定的属性值,如果没有指定确定的属性值,则沿用默认的属性值********/
	public HorizontalRulerView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalRulerview);
		mDirection = typedArray.getInteger(R.styleable.HorizontalRulerview_hDirections, 1);
		mMinValue = typedArray.getInteger(R.styleable.HorizontalRulerview_hMinValue, 0);
		mMaxValue = typedArray.getInteger(R.styleable.HorizontalRulerview_hMaxValue, 200);
		mCurrentValue = typedArray.getInteger(R.styleable.HorizontalRulerview_hCurrentValue, 60);
		mAlreadyInitView = false;
		typedArray.recycle();
	}
	
	/****************初始化该控件的内部控件,并监听其所有的手势操作****************/
	/**在使用此控件的Act中,在其onWindowFocusChanged()方法中调用该函数便可初始化度量尺的界面*/
	public void initView(int parentWidth)
	{
		if(!mAlreadyInitView)
		{
			View  view = null;
			switch (mDirection) 
			{
				case 1:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtop,this,true);break;
				case 2:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottom,this,true);break;
			}
			mContentView = (LinearLayout)view.findViewById(R.id.ruler_ll); 
			mHorizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.ruler_sv);
			initRuler(parentWidth);
			
			mHorizontalScrollView.setOnTouchListener(new OnTouchListener()
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
	private void initRuler(int parentWidth)
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
		if(mDirection == 1)/**顶部布局*/
		{
		   /************************************************左边部分********************************************************/
		   View leftViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   ImageView nullImg1 =  (ImageView)leftViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewt_null);
		   nullImg1.setLayoutParams(new LinearLayout.LayoutParams(parentWidth/2-bigGridSize/2,LayoutParams.MATCH_PARENT));
		   mContentView.addView(leftViewNullBegin);
		   View leftViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   TextView beginTv = (TextView)leftViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(InitValue+"");
		   ImageView beginImg =  (ImageView)leftViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewt_begin);
		   beginImg.setLayoutParams(new LinearLayout.LayoutParams(bigGridSize,LayoutParams.MATCH_PARENT));
		   mContentView.addView(leftViewBegin);			   
		   
		   /************************************************中间部分********************************************************/
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10) 
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(InitValue+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewt_content);
			   contentImg.setLayoutParams(new LinearLayout.LayoutParams(bigGridSize,LayoutParams.MATCH_PARENT));
			   mContentView.addView(viewContent);
		   }
				
		   /************************************************右边部分********************************************************/
		   View rightViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   TextView endTv = (TextView)rightViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(InitValue+"");
		   ImageView endImg =  (ImageView)rightViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewt_end);		  
		   endImg.setLayoutParams(new LinearLayout.LayoutParams(bigGridSize,LayoutParams.MATCH_PARENT));
		   mContentView.addView(rightViewEnd);
		   View rightViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   ImageView nullImg2 =  (ImageView)rightViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewt_null);
		   nullImg2.setLayoutParams(new LinearLayout.LayoutParams(parentWidth/2-bigGridSize/2,LayoutParams.MATCH_PARENT));
		   mContentView.addView(rightViewNullEnd);
		}
		
		
		else/**底部布局*/
		{
			/************************************************左边部分********************************************************/
		   View leftViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   ImageView nullImg1 =  (ImageView)leftViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewb_null);
		   LinearLayout.LayoutParams  nullParams1 = ( LinearLayout.LayoutParams)nullImg1.getLayoutParams();
		   nullParams1.width = parentWidth/2-bigGridSize/2;
		   nullImg1.setLayoutParams(nullParams1);
		   mContentView.addView(leftViewNullBegin);
		   View leftViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   TextView beginTv = (TextView)leftViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(InitValue+"");
		   ImageView beginImg =  (ImageView)leftViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewb_begin);
		   LinearLayout.LayoutParams  BeginParams = ( LinearLayout.LayoutParams)beginImg.getLayoutParams();
		   BeginParams.width = bigGridSize;
		   beginImg.setLayoutParams(BeginParams);
		   mContentView.addView(leftViewBegin);			   
		   
		   /************************************************中间部分********************************************************/
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10) 
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(InitValue+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewb_content);
			   LinearLayout.LayoutParams  contentParams = ( LinearLayout.LayoutParams)contentImg.getLayoutParams();
			   contentParams.width = bigGridSize;
			   contentImg.setLayoutParams(contentParams);
			   mContentView.addView(viewContent);
		   }
				
		   /************************************************右边部分********************************************************/
		   View rightViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   TextView endTv = (TextView)rightViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(InitValue+"");
		   ImageView endImg =  (ImageView)rightViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewb_end);		  
		   LinearLayout.LayoutParams  endParams = (LinearLayout.LayoutParams)endImg.getLayoutParams();
		   endParams.width = bigGridSize;
		   endImg.setLayoutParams(endParams);
		   mContentView.addView(rightViewEnd);
		   View rightViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   ImageView nullImg2 =  (ImageView)rightViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewb_null);
		   LinearLayout.LayoutParams  nullParams2 = (LinearLayout.LayoutParams)nullImg2.getLayoutParams();
		   nullParams2.width = parentWidth/2-bigGridSize/2;
		   nullImg2.setLayoutParams(nullParams2);
		   mContentView.addView(rightViewNullEnd);
		}	
		
		/**初始化指针指向当前的刻度值*/
		new Handler()
		{
			public void handleMessage(Message msg)
			{
				mHorizontalScrollView.smoothScrollTo(mCurrentValue * smallGridSize,0);
				if(mListener != null)
					mListener.onScroll(mMinValue+ (int) Math.ceil(mHorizontalScrollView.getScrollX() / smallGridSize));
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