package edu.bzu.project.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class AddSport_fragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> list =null;
	private FragmentTransaction transaction =null;
	public AddSport_fragmentAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.list =list;
		transaction =fm.beginTransaction();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
