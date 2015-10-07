package com.su.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.com.cctest.view.XListView;
import org.com.cctest.view.XListView.IXListViewListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jgzs.lsw.R;
import com.su.ImageLoad.ImageLoader;
import com.su.model.PlantListModel;
import com.su.model.ProjectListModel;

import com.su.util.NetManager;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
// 156 50 -44
public class ProjectActivity extends Activity implements IXListViewListener{
	private static final String DIS_DATE[] = { "全部分类", "居住景观", "商业办公",
		"城市绿地" ,"校园规划","休闲度假","滨水生态"};
	private static final String CLASS_DATE[] = { "全部区域", "入口景观", "场地节点", "园路栈桥",
			"水景泳池", "宅间入户" ,"庭院露台","架空层","景观建筑"};
	private static final String AWAY_DATE[] = {  "全部风格", "欧式","东南亚","中式","地中海","现代简约"};
	
	private ArrayAdapter disAdapter, claAdapter, awayAdapter;
	
	public NearAdapter nAdapter;
	
	public Spinner disSpi, claSpi, awaySpi;
	
	public TextView merTital;
	
	private Handler mHandler;
	
	public XListView showList;
	
	public ProjectActivity _plant;
	public float viewWidth;
	
	private ArrayList<ProjectListModel> items = new ArrayList<ProjectListModel>();
	
	private int page = 1;

	private int cid = 0;
	private int acid = 0;
	private int sid = 0;
	HoldView hold = new HoldView();
	
	
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> itemsa = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.projectlist);
		_plant = this;
		initResourceRefs();
		initSetting();
		String name = getIntent().getStringExtra("MER_NAME");
		mHandler = new Handler();
		
 	}
	
	public void initResourceRefs() {
	
		showList = (XListView) findViewById(R.id.project_listView);
		showList.setPullLoadEnable(true);
        viewWidth = showList.getWidth();
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
	    showList.setXListViewListener(this);
	    showList.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						viewWidth = showList.getWidth();
						showList.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						onRefresh();
						Log.v("suzhaohui","showlist widht"+String.valueOf(viewWidth));
					}
				});
		merTital = (TextView)findViewById(R.id.choose_mer_tital);
		
		
		
		//���õġ�����
		disSpi = (Spinner) findViewById(R.id.nearby_distance_spinner);
		claSpi = (Spinner) findViewById(R.id.nearby_class_spinner);
		awaySpi = (Spinner) findViewById(R.id.nearby_away_spinner);
	
	
		nAdapter = new NearAdapter(_plant);
		disAdapter = new ArrayAdapter<String>(this,
				R.layout.nearby_spinner_text, DIS_DATE);
		claAdapter = new ArrayAdapter<String>(this,
				R.layout.nearby_spinner_text, CLASS_DATE);
		awayAdapter = new ArrayAdapter<String>(this,
				R.layout.nearby_spinner_text, AWAY_DATE);
	
	}
	public void initSetting() {
		disAdapter.setDropDownViewResource(R.layout.spinnerdownitem);//android.R.layout.simple_spinner_dropdown_item
		claAdapter.setDropDownViewResource(R.layout.spinnerdownitem);
		awayAdapter.setDropDownViewResource(R.layout.spinnerdownitem);
	
		showList.setAdapter(nAdapter);
//		showList.setDivider(null);
//		showList.setDividerHeight(20);
	
		disSpi.setAdapter(disAdapter);
		claSpi.setAdapter(claAdapter);
		awaySpi.setAdapter(awayAdapter);
	
		disSpi.setSelection(0);
		claSpi.setSelection(0);
		awaySpi.setSelection(0);
		
		disSpi.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if(cid != arg2)
						{
							cid = arg2;
						    onRefresh();
						}
						    
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				}
				) ;
		
		claSpi.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if(acid != arg2)
						{
							acid = arg2;
						    onRefresh();
						}
						

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				}
				) ;
		
		awaySpi.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if(sid != arg2)
						{
							 sid = arg2;
							    onRefresh();
						}
						   

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				}
				) ;
		
		showList.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent picIntent = new Intent(ProjectActivity.this,
						ProjectDetailActivity.class);
				
				ProjectListModel model = items.get(arg2 -1);
				picIntent.putExtra("id", model.getId());
				picIntent.putExtra("tittle", model.getTittle());
				
				ProjectActivity.this.startActivity(picIntent);
			}
		});
	
	}
	
	/**
	 * ���� ����ѡ� �е� ���� 
	 * 
	 * ���� �ǵ� ����̫������
	 * 
	 * @author yuhaiyang
	 *
	 */
	public class NearAdapter extends BaseAdapter {
	
		private com.su.ImageLoad.ImageLoader mImageLoader;
		public NearAdapter(Context context)
		{
			mImageLoader = new ImageLoader(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}
	
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HoldView holdview = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater
						.from(ProjectActivity.this);
				convertView = inflater.inflate(R.layout.project_item, null);
				holdview = new HoldView();
				holdview.name = (TextView) convertView
						.findViewById(R.id.listItemName);
				holdview.plantImage = (ImageView)convertView
						.findViewById(R.id.imageView1);
				holdview.people = (TextView)convertView.findViewById(R.id.project_provider);
				holdview.time = (TextView)convertView.findViewById(R.id.project_time);

				
				convertView.setTag(holdview);
				
			
			}
			else
			{
				holdview = (HoldView) convertView.getTag();
			}
			
			if(position < items.size())
			{
				Log.v("position",String.valueOf(position));
				ProjectListModel model = items.get(position);
				holdview.name.setText(model.getTittle());
				holdview.people.setText("设计师:"+model.getShejishi());
				String time = model.getShangchuanshijian();
				long p = time.indexOf(" ");
				time = time.substring(0, (int) p);
				holdview.time.setText("上传时间:"+time);
				int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
				int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
				holdview.plantImage.measure(w, h);  
				int height =holdview.plantImage.getMeasuredHeight();  
				int width =holdview.plantImage.getMeasuredWidth();
				Log.v("suzhaohui","viewwidth"+width);
				ViewGroup.LayoutParams lp = holdview.plantImage.getLayoutParams();
				lp.width = (int) viewWidth;
				lp.height = LayoutParams.WRAP_CONTENT;
				holdview.plantImage.setLayoutParams(lp);

				holdview.plantImage.setMaxWidth((int) viewWidth);
				holdview.plantImage.setMaxHeight((int) viewWidth * 5);
				mImageLoader.DisplayImage(model.getUrl(), holdview.plantImage, false);
			}
	
			return convertView;
		}
	
		public void init(View convertView) {
			
	//		hold.local = (TextView) convertView
	//				.findViewById(R.id.nearby_item_local);
	//		hold.dis1 = (TextView) convertView
	//				.findViewById(R.id.nearby_item_dis1);
	//		hold.dis2 = (TextView) convertView
	//				.findViewById(R.id.nearby_item_dis2);
	//		hold.dis3 = (TextView) convertView
	//				.findViewById(R.id.nearby_item_dis3);
		}
	}
	
	class HoldView {
		TextView name, people, time;
		ImageView plantImage;
	}

	private void onLoad() {
		showList.stopRefresh();
		showList.stopLoadMore();
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");       
		String    date    =    sDateFormat.format(new    java.util.Date());  
		showList.setRefreshTime(date);
	}
	private void geneItems(int size) {
		for (int i = 0; i != size; ++i) {
//			items.add("refresh cnt " + (i));
		}
	}
	@Override
	public void onRefresh() {
		items.clear();
	
		page = 1;
		getPlantList();
		nAdapter = new NearAdapter(_plant);
		showList.setAdapter(nAdapter);
		onLoad();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
			}
		}, 2);
	}

	@Override
	public void onLoadMore() {
		page++;
		int ret = getPlantList();
		if(ret == 2)
		{
			dandler.sendEmptyMessage(2);
			return;
		}
		nAdapter.notifyDataSetChanged();
		onLoad();
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				
//			}
//		}, 2000);
	}
	
//	private void getPlantList(String page , String )
	private int getPlantList()
	{
		List<NameValuePair>  tt = new ArrayList<NameValuePair>();
		NetManager bb=		NetManager.getInstance() ; //.toString();
		tt.add(new BasicNameValuePair("page", String.valueOf(page))); 
		if(cid >= 0)
		{
			tt.add(new BasicNameValuePair("cid", String.valueOf(cid)));
		}
		if(acid >= 0)
		{
			tt.add(new BasicNameValuePair("acid", String.valueOf(acid)));
		}
		if(sid >= 0)
		{
			tt.add(new BasicNameValuePair("sid", String.valueOf(sid)));
		}
		JSONObject get = bb.sendHttpRequest("item/search", tt, 0);
		try {
			if(get != null)
			{  
				
				
				if(get.isNull("error") == false)
				{
					String error = get.getString("error");
					if(error == "timeOut")
					{
						dandler.sendEmptyMessage(0);
						return 1;
					}
					else
					{
						dandler.sendEmptyMessage(1);
						return 1;
					}
				}
				int code = get.getInt("code");
				if(code == -9000 )
				{
					dandler.sendEmptyMessage(-9000);
					return -9000;
				}
				
				JSONObject data = get.getJSONObject("data");
				JSONArray plantList = data.getJSONArray("itemList");
				Log.v("suzhaohui",String.valueOf(plantList.length()));
				if(plantList.length() == 0)
				{
					return 2;
				}
				else if(plantList.length() < 10)
				{
					dandler.sendEmptyMessage(9);
				}
				else if (plantList.length() >= 10)
				{
					dandler.sendEmptyMessage(10);
				}
				for(int i = 0; i < plantList.length();i++)
				{
					JSONObject item = (JSONObject)plantList.opt(i);
					Log.v("getJSON",String.valueOf(item.getInt("id")) );
					ProjectListModel mode = new ProjectListModel();
					mode.setId(item.getInt("id"));
					mode.setTittle(item.getString("title"));
					mode.setUrl(item.getString("cover_media"));
					mode.setShejishi(item.getString("designer"));
					mode.setShangchuanshijian(item.getString("gmt_created"));				
					items.add(mode);
			}
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return 0;
	}
	
	// 处理EmptyMessage(0)
		@SuppressLint("HandlerLeak")
		Handler dandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				 switch (msg.what) {
		            case 0:
						Toast.makeText(_plant, "网络连接超时",
								Toast.LENGTH_SHORT).show();
		                break;
		            case 1:
						Toast.makeText(_plant, "网络错误",
								Toast.LENGTH_SHORT).show();
		                break;
		            case -9000:
						Toast.makeText(_plant, "没有查到数据",
								Toast.LENGTH_SHORT).show();
						showList.setPullLoadEnable(false);
		                break;
		            case 9:
						
						showList.setPullLoadEnable(false);
		                break;
                    case 10:
						
						showList.setPullLoadEnable(true);
		                break;
		            }
		            super.handleMessage(msg);
		        }

//			}
		};
}
