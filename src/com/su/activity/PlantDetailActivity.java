package com.su.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.jgzs.lsw.R;
import com.su.ImageLoad.ImageLoader;
import com.su.framgment.FragmentAdapter;
import com.su.util.log;

public class PlantDetailActivity extends FragmentActivity implements OnTouchListener {

	public static final String TAG = "PlantDetailActivity";
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int EACH =4 ;
	boolean isShow; 
	
	View view;
	WindowManager windowManager;
	WindowManager.LayoutParams lp;
	ScrollView sv;
	TextView text;
	ImageButton img;
	GestureDetector mGestureDetector;
	int viewH;
	private String cover = "";
	private ImageView coverImage = null;
	private TextView xixing = null;
	private TextView yongtu = null;
	private TextView secTittle = null;
	private TextView tittle = null;
	private TextView biemingTextView = null;
	private String  xixingStr = "";
	private String  yongtuStr = "";
	private String  name = "";
	private String  bieming = "";
	private String  nameen = "";
	private ArrayList<String> imagelist = new ArrayList<String>();
	private com.su.ImageLoad.ImageLoader mImageLoader;
	Timer mTimer;
	TimerTask mTask;
	int pageIndex = 0;
	boolean isTaskRun;
	private FragmentAdapter mAdapter;
	private ViewPager mPager;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UP:
				windowManager.updateViewLayout(view, lp);
				break;
            case DOWN:
				windowManager.updateViewLayout(view, lp);
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
         
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.businesse_information);
		String flag = "   ";
		mImageLoader = new ImageLoader(this);

		Intent intent1 = this.getIntent(); 

		cover = intent1.getStringExtra("image");
		name = intent1.getStringExtra("name");
		nameen = intent1.getStringExtra("nameen");
		bieming = intent1.getStringExtra("bieming");
		xixingStr = intent1.getStringExtra("xixing");
		yongtuStr = intent1.getStringExtra("yongtu");
		imagelist = intent1.getStringArrayListExtra("imagelist");
		initResourceRefs();

		windowManager.addView(view, lp);
		isShow = text.getVisibility() == View.VISIBLE;
		
		// �Ż�ȯ��ʾ
		img.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShow) {
					text.setVisibility(View.GONE);
					img.setImageResource(R.drawable.arrow_down);
					isShow = false;
				} else {
					text.setVisibility(View.VISIBLE);
					img.setImageResource(R.drawable.arrow_up);
					isShow = true;
				}

			}
		});

	}
	
       /**
        *  ��ʼ��
        */
	private void initResourceRefs() {
		img = (ImageButton) findViewById(R.id.bus_info_detail_but);
		text = (TextView) findViewById(R.id.bus_info_explor);
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.bus_info_windows, null);
//		coverImage = (ImageView) findViewById(R.id.plantDetailCover);
//		mImageLoader.DisplayImage(cover, coverImage, false);
		
		xixing = (TextView) findViewById(R.id.plantDetailXiXing);
		yongtu = (TextView) findViewById(R.id.plantDetailYongTu);
		tittle = (TextView) findViewById(R.id.plantDetailTittle);
		secTittle = (TextView) findViewById(R.id.plantDetailSecondTittle);
		biemingTextView = (TextView) findViewById(R.id.bus_info_explor);
		
		
		xixing.setText(xixingStr);
		yongtu.setText(yongtuStr);
		tittle.setText(name);
		secTittle.setText(name + "(" + nameen + ")");
		biemingTextView.setText("别名:"+bieming);

		mPager = (ViewPager) findViewById(R.id.plantDetailViewPager);
		mAdapter = new FragmentAdapter(getSupportFragmentManager(),imagelist,mImageLoader,1);
		mPager.setAdapter(mAdapter);
		mPager.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                 if (event.getAction() == MotionEvent.ACTION_UP)  
                     sv.requestDisallowInterceptTouchEvent(false);  
                  else    
                     sv.requestDisallowInterceptTouchEvent(true);  
                return false;   
            }  
        });  
		
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			/* 更新手动滑动时的位置 */
			@Override
			public void onPageSelected(int position) {
				pageIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			/* state: 0空闲，1是滑行中，2加载完毕 */
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				System.out.println("state:" + state);
				if (state == 0 && !isTaskRun) {
					setCurrentItem();
					startTask();
				} else if (state == 1 && isTaskRun)
					stopTask();
			}
		});
		
		lp = new WindowManager.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				// ����Ϊ�޽���״̬
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, // û�б߽�
				// ��͸��Ч��
				PixelFormat.TRANSLUCENT);
		lp.gravity = Gravity.BOTTOM;
        
        lp.windowAnimations = R.style.bus_view;
		sv = (ScrollView) findViewById(R.id.scrollView);
		sv.setOnTouchListener(this);
		
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		view.setVisibility(View.GONE);
	   
	}


	@Override
	protected void onDestroy() {
		windowManager.removeView(view);
		super.onDestroy();
	}

	// scroll view ����onTouch
	@Override
	public boolean onTouch(View v, MotionEvent event) {
//		if(event.getAction()==MotionEvent.ACTION_MOVE){
//			view.setVisibility(View.GONE);
//		}else{
//			view.setVisibility(View.VISIBLE);
//		} 
		return false;
	}
	/**
	 * 开启定时任务
	 */
	private void startTask() {
		// TODO Auto-generated method stub
		isTaskRun = true;
		mTimer = new Timer();
		mTask = new TimerTask() {
			@Override
			public void run() {
				pageIndex++;
				mHandler.sendEmptyMessage(0);
			}
		};
		mTimer.schedule(mTask, 5 * 1000, 5 * 1000);// 这里设置自动切换的时间，单位是毫秒，2*1000表示2秒
	}

	// 处理EmptyMessage(0)
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			setCurrentItem();
		}
	};

	/**
	 * 处理Page的切换逻辑
	 */
	private void setCurrentItem() {
		if (pageIndex == 0) {
			pageIndex = 0;
		} else if (pageIndex >= imagelist.size() + 1) {
			pageIndex = 0;
		}
		mPager.setCurrentItem(pageIndex, true);// 取消动画
	}

	/**
	 * 停止定时任务
	 */
	private void stopTask() {
		// TODO Auto-generated method stub
		isTaskRun = false;
		mTimer.cancel();
	}

	public void onResume() {
		super.onResume();
		setCurrentItem();
		startTask();
	}
    
	@Override
	public void onPause() {
		super.onPause();
		stopTask();
	}
}
