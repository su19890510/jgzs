package com.su.framgment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> fragments;

	/**
	 * Fragmentʵ��ֻ��Zero��One��Two������
	 */
	public FragmentAdapter(FragmentManager fm,ArrayList<String> itemlist,com.su.ImageLoad.ImageLoader mImageLoader,int itemType) {
		super(fm);
		fragments = new ArrayList<Fragment>();
		for(int i = 0 ; i < itemlist.size(); i++)
		{
			String url = itemlist.get(i);
			fragments.add(new FragmentOne(url,String.valueOf(i + 1),String.valueOf(itemlist.size()),mImageLoader,itemType));
		}
//		fragments.add(new FragmentTwo());
//		fragments.add(new FragmentZero());
//		fragments.add(new FragmentOne());
//		fragments.add(new FragmentTwo());
//		fragments.add(new FragmentZero());
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}