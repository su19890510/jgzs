package com.su.framgment;



import com.jgzs.lsw.R;
import com.su.ImageLoad.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentOne extends Fragment{

	TextView text;
	ImageView image;
	String _url;
	String _index;
	String _allLength;
	int    _layoutType;
	private com.su.ImageLoad.ImageLoader mImageLoader;
	
	public FragmentOne(String url , String index , String allLength,com.su.ImageLoad.ImageLoader ImageLoader,int layouttype)
	{
		_url = url;
		_index = index;
		_allLength = allLength;
		mImageLoader = ImageLoader;
		_layoutType = layouttype;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// ��һ�����������Fragment��Ҫ��ʾ�Ľ��沼��,�ڶ������������Fragment������Activity,�����������Ǿ�����fragment�Ƿ�����Activity
    	View view = null ;
    	if (_layoutType == 1 )
    	{
        	view =inflater.inflate(R.layout.framegment, container, false);

    	}
    	else
    	{
        	view =inflater.inflate(R.layout.fragmenttwo, container, false);

    	}
    	image = (ImageView) view.findViewById(R.id.fragementImage);
    	text = (TextView) view.findViewById(R.id.fragementText);
    	text.setText(_index+"/"+_allLength);
    	mImageLoader.DisplayImage(_url, image, false);
		
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        System.out.println("FragmentOne onCreate");
    }
    
	public void onResume(){
        super.onResume();
        System.out.println("FragmentOne onResume");
    }
    
    @Override
    public void onPause(){
        super.onPause();
        System.out.println("FragmentOne onPause");
    }
    
    @Override
    public void onStop(){
        super.onStop();
        System.out.println("FragmentOne onStop");
    }
}
