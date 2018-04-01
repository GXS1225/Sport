package edu.bzu.project.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Home_fragementAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;
	private FragmentManager fm;

	public Home_fragementAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.fm =fm;
		this.list =list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
