package com.su.activity;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlantListActivity extends Activity implements IXListViewListener{
	private static final String DIS_DATE[] = { "植物类型", "常绿乔木", "落叶乔木",
		"常绿小乔木/灌木" ,"落叶小乔木","常绿灌木","半常绿灌木","落叶灌木","藤本","竹类","水生植物","一、二年生花卉","宿根花卉","球根花卉","草坪植物"};
	private static final String CLASS_DATE[] = { "颜色", "绿色叶", "黄色叶", "蓝色叶",
			"白色叶", "秋天呈红色叶" ,"秋天呈白色叶","紫色花","红色花","黄色花","蓝色花","白色花","多色花","其他"};
	private static final String AWAY_DATE[] = {  "观赏时期", "一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月" };
	
	private ArrayAdapter disAdapter, claAdapter, awayAdapter;
	
	public NearAdapter nAdapter;
	
	public Spinner disSpi, claSpi, awaySpi;
	
	public TextView merTital;
	
	private Handler mHandler;
	
	public XListView showList;
	
	public PlantListActivity _plant;
	
	private ArrayList<PlantListModel> items = new ArrayList<PlantListModel>();
	
	private int page = 1;

	
	HoldView hold = new HoldView();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_mer);
		_plant = this;
		initResourceRefs();
		initSetting();
		String name = getIntent().getStringExtra("MER_NAME");
		merTital.setText("植物手册");
		mHandler = new Handler();
		onRefresh();
 	}
	
	public void initResourceRefs() {
	
		showList = (XListView) findViewById(R.id.choose_mer_list);
		showList.setPullLoadEnable(true);
		showList.setPullLoadEnable(false);
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
	    showList.setXListViewListener(this);
	
		merTital = (TextView)findViewById(R.id.choose_mer_tital);
		
		//公用的。。。
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
		disAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		claAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		awayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	
		showList.setAdapter(nAdapter);
		showList.setDivider(null);
		showList.setDividerHeight(20);
	
		disSpi.setAdapter(disAdapter);
		claSpi.setAdapter(claAdapter);
		awaySpi.setAdapter(awayAdapter);
	
		disSpi.setSelection(0);
		claSpi.setSelection(0);
		awaySpi.setSelection(0);
		showList.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent picIntent = new Intent(PlantListActivity.this,
						PlantDetailActivity.class);
				PlantListActivity.this.startActivity(picIntent);
			}
		});
	
	}
	
	/**
	 * 复用 附近选项卡 中的 东西 
	 * 
	 * 尼玛 是的 他们太相似了
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
						.findViewById(R.id.textView1);
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
		showList.setRefreshTime("刚刚");
		showList.setPullLoadEnable(true);
	}
	private void geneItems(int size) {
		for (int i = 0; i != size; ++i) {
//			items.add("refresh cnt " + (i));
		}
	}
	@Override
	public void onRefresh() {
		Log.v("test","mHandler.postDelayed(new Runnable() {()");
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				items.clear();
				List<NameValuePair>  tt = new ArrayList<NameValuePair>();
				//returnStr =
				Log.v("suzhaohui","begin http");
				NetManager bb=		NetManager.getInstance() ; //.toString();
				Log.v("suzhaohui","getInstance");
				tt.add(new BasicNameValuePair("page", String.valueOf(page++))); 
				JSONObject get = bb.sendHttpRequest("plant/search", tt, 1);
				try {
					if(get != null)
					{
						JSONObject data = get.getJSONObject("data");
						JSONArray plantList = data.getJSONArray("plantsList");
						Log.v("plantList",String.valueOf(plantList.length()));
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
							Log.v("plantName",mode.getName());
							items.add(mode);
					}
					
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("test","onRefresh()");
				
				nAdapter.notifyDataSetChanged();
				nAdapter = new NearAdapter(_plant);
				showList.setAdapter(nAdapter);
				onLoad();
			}
		}, 2);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems(30);
				nAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
//	private void getPlantList(String page , String )
}
