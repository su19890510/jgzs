package com.su.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jgzs.lsw.R;
import com.su.ImageLoad.ImageLoader;
import com.su.framgment.FragmentAdapter;
import com.su.model.ProjectListModel;
import com.su.util.NetManager;

public class ProjectDetailActivity extends FragmentActivity implements OnTouchListener {

    

	private ArrayList<String> imagelist = new ArrayList<String>();
	private com.su.ImageLoad.ImageLoader mImageLoader;
	private FragmentAdapter mAdapter;
	private ViewPager mPager; 
	private int id;
	private String tittle;
	private String dec;
	private TextView tittleView;
	private TextView decView;
	private ImageView bottomView;
	private ImageView showView;
	private ScrollView decScrollView;
	private LinearLayout showlayout;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				decView.setText(dec);
				mAdapter = new FragmentAdapter(getSupportFragmentManager(),imagelist,mImageLoader,2);
				mPager.setAdapter(mAdapter);
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
		setContentView(R.layout.projectdetail);
		mImageLoader = new ImageLoader(this);
		Intent intent1 = this.getIntent(); 
		id = intent1.getIntExtra("id",1);
		tittle = intent1.getStringExtra("tittle");
		initResourceRefs();
		getDetail();
	}
	
	
	private void initResourceRefs() {

		tittleView = (TextView) findViewById(R.id.project_detail_tittle);
		decView = (TextView) findViewById(R.id.project_detail_dec);
		
		tittleView.setText(tittle);

		mPager = (ViewPager) findViewById(R.id.project_detail_viewpager);
		
		bottomView = (ImageView)findViewById(R.id.project_detail_show_button);
		showView = (ImageView)findViewById(R.id.projectdetail_show_button);
	    decScrollView = (ScrollView)findViewById(R.id.project_detail_dec_scroll);
	    
	    showlayout = (LinearLayout)findViewById(R.id.project_detail_show_layout);
	    
	    showlayout.setVisibility(View.GONE);
		
	  
	   
	}
	public void hideDetail(View arg0)
	{
		Log.v("suzhaohui","hideDetail");
		showlayout.setVisibility(View.GONE);
		    bottomView.setVisibility(View.VISIBLE);
	}
	public void showDetail(View arg0)
	{
		Log.v("suzhaohui","showDetail");
		showlayout.setVisibility(View.VISIBLE);
		    bottomView.setVisibility(View.GONE);
	}

 private int getDetail()
 {
	 Log.v("suzhaohui","getDetail");
	 handler.postDelayed(
	 new Runnable() {
			@Override
			public void run() {
				Log.v("suzhaohui","getDetail");
				List<NameValuePair>  tt = new ArrayList<NameValuePair>();
				NetManager bb=		NetManager.getInstance() ; //.toString();				
				JSONObject get = bb.sendHttpRequest("item/" + String.valueOf(id), tt, 0);
				try {
					if(get != null)
					{  
						
						
						if(get.isNull("error") == false)
						{
							String error = get.getString("error");
							if(error == "timeOut")
							{
								handler.sendEmptyMessage(0);
								return ;
							}
							else
							{
								handler.sendEmptyMessage(1);
								return ;
							}
						}
						int code = get.getInt("code");
						if(code == -9000 )
						{
							handler.sendEmptyMessage(-9000);
							return ;
						}
						
						JSONObject data = get.getJSONObject("data");
						JSONObject item = data.getJSONObject("item");
					    dec =  item.getString("description");
					    JSONArray imagearray = item.getJSONArray("itemMediaList");
					    for(int i = 0; i < imagearray.length(); i++)
					    {
					    	JSONObject imageitem = (JSONObject) imagearray.get(i);
					    	imagelist.add(imageitem.getString("url"));
					    }
					    handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				return ;
			}
		},1);
	return 0;
 }


@Override
public boolean onTouch(View arg0, MotionEvent arg1) {
	// TODO Auto-generated method stub
	return false;
}
	
}
