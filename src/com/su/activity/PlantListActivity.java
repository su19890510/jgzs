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

import com.su.util.NetManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
public class PlantListActivity extends Activity implements IXListViewListener{
	private static final String DIS_DATE[] = { "全部植物类型", "常绿乔木", "落叶乔木",
		"常绿小乔木/灌木" ,"落叶小乔木","常绿灌木","半常绿灌木","落叶灌木","藤本","竹类","水生植物","一、二年生花卉","宿根花卉","球根花卉","草坪植物"};
	private static final String CLASS_DATE[] = { "全部颜色", "绿色叶", "黄色叶", "蓝色叶",
			"白色叶", "秋天呈红色叶" ,"秋天呈白色叶","紫色花","红色花","黄色花","蓝色花","白色花","多色花","其他"};
	private static final String AWAY_DATE[] = {  "全部观赏时期", "一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月" };
	
	private ArrayAdapter disAdapter, claAdapter, awayAdapter;
	
	public NearAdapter nAdapter;
	
	public Spinner disSpi, claSpi, awaySpi;
	
	public TextView merTital;
	
	private Handler mHandler;
	
	public XListView showList;
	
	public PlantListActivity _plant;
	
	private ArrayList<PlantListModel> items = new ArrayList<PlantListModel>();
	
	private int page = 1;

	private int catId = 0;
	private int colorId = 0;
	private int periodId = 0;
	HoldView hold = new HoldView();
	
	
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> itemsa = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plantlist);
		_plant = this;
		initResourceRefs();
		initSetting();
		String name = getIntent().getStringExtra("MER_NAME");
		mHandler = new Handler();
		onRefresh();
 	}
	
	public void initResourceRefs() {
	
		showList = (XListView) findViewById(R.id.choose_mer_list);
		showList.setPullLoadEnable(true);

		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
	    showList.setXListViewListener(this);
	
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
		showList.setDivider(null);
		showList.setDividerHeight(20);
	
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
						if(catId != arg2)
						{
							catId = arg2;
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
						if(colorId != arg2)
						{
							colorId = arg2;
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
						if(periodId != arg2)
						{
							 periodId = arg2;
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
				Intent picIntent = new Intent(PlantListActivity.this,
						PlantDetailActivity.class);
				
				PlantListModel model = items.get(arg2 -1);
				picIntent.putExtra("image", model.getCover());
				picIntent.putExtra("xixing", model.getXixing());
				picIntent.putExtra("yongtu", model.getYongTu());
				picIntent.putExtra("bieming",model.getNameAlias());
				picIntent.putExtra("name", model.getName());
				picIntent.putExtra("nameen", model.getNameEn());
				picIntent.putStringArrayListExtra("imagelist", (ArrayList<String>) model.getImageList());
				
				PlantListActivity.this.startActivity(picIntent);
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
						.from(PlantListActivity.this);
				convertView = inflater.inflate(R.layout.nearby_list_item, null);
				holdview = new HoldView();
				holdview.name = (TextView) convertView
						.findViewById(R.id.listItemName);
				holdview.plantImage = (ImageView)convertView
						.findViewById(R.id.imageView1);
				convertView.setTag(holdview);
				
			
			}
			else
			{
				holdview = (HoldView) convertView.getTag();
			}
			
			if(position < items.size())
			{
				Log.v("position",String.valueOf(position));
				PlantListModel model = items.get(position);
				holdview.name.setText(model.getName());
				Log.v("cover",model.getCover());
				mImageLoader.DisplayImage(model.getCover(), holdview.plantImage, false);
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
		TextView name, dis1, dis2, dis3;
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
		if(catId >= 0)
		{
			tt.add(new BasicNameValuePair("catId", String.valueOf(catId)));
		}
		if(colorId >= 0)
		{
			tt.add(new BasicNameValuePair("colorId", String.valueOf(colorId)));
		}
		if(periodId >= 0)
		{
			tt.add(new BasicNameValuePair("periodId", String.valueOf(periodId)));
		}
		JSONObject get = bb.sendHttpRequest("plant/search", tt, 0);
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
				JSONArray plantList = data.getJSONArray("plantsList");
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
					PlantListModel mode = new PlantListModel();
					mode.setId(item.getInt("id"));
					mode.setCatId(item.getInt("catId"));
					mode.setName(item.getString("nameCn"));
					mode.setNameEn(item.getString("nameEn"));
					mode.setNameAlias(item.getString("aliasName"));
					mode.setXixing(item.getString("habits"));
					mode.setCover(item.getString("cover_media"));
					mode.setTime(item.getString("gmt_created"));
					mode.setYongTu(item.getString("gardenUtilization"));
					Log.v("plantName",mode.getName());
					JSONArray imageList = item.getJSONArray("plantsMediaList");
					ArrayList<String> imgList = new ArrayList<String>();
					for(int n = 0 ; n < imageList.length();n++)
					{
						JSONObject imageitem = (JSONObject) imageList.get(n);
						imgList.add(imageitem.getString("url"));							
					}
					if(imgList.size() == 0)
					{
						imgList.add(item.getString("cover_media"));
					}
					mode.setImageList(imgList);
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
