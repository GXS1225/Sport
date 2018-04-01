package edu.bzu.project.fragment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.adapter.Home_fragementAdapter;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.view.CircleProgressBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 主界面之主页的fragment
 *
 */
public class HomeFragment extends BaseFragment implements OnPageChangeListener{
	
	private static String TAG ="HomeFragment";
	/**view Page*/
	private ViewPager viewPager =null;
	private Context context =null;
	private FragmentManager fragmentManager =null;
	private Home_fragementAdapter fragment_adapter =null;
	private List<Fragment> fragments =new ArrayList<Fragment>();
	
	private TextView title_view =null;
	private ImageButton today_view =null;
	
	private String firstTime =null;//第一次登陆的时间
	private Intent intent =null;
	
	private String nowDate_str =null;//今天的时间
	
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return TAG;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//获取第一次登录的时间
		firstTime =(String)SPUtils.get(getActivity(), ConstantValues.First_Time, "");
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootv =inflater.inflate(R.layout.fragment_home, container, false);
		viewPager =(ViewPager)rootv.findViewById(R.id.home_viewpage);
		fragmentManager =getActivity().getSupportFragmentManager();
		title_view =(TextView) rootv.findViewById(R.id.circle_riqi);
		today_view =(ImageButton) rootv.findViewById(R.id.circle_today);
	
		//加载时 分一下几种情况
		/**
		 * 取得存入的时间与当前的时间判断
		 * 1，首次登陆的时候，只加载本页面，并隐藏滑动的图标
		 * 2，不是首次登陆 但是少于3天，则只显示两天，并选择性取消滑动图标
		 * 3，多于3天的时候，
		 */
		Home_circle_Fragment h1 =null;
		Home_circle_old_Fragment h2 =null;
		Home_circle_old_Fragment h3 =null;
		//今天的时间
		Date nowDate =Calendar.getInstance().getTime();
		nowDate_str =DateUtils.DateToString(nowDate, DateStyle.YYYY_MM_DD_CN);
		
		if(firstTime.equals(nowDate_str)){//是否今天
			 h1 =new Home_circle_Fragment();
			 fragments.add(h1);
		}else if(DateUtils.getIntervalDays(nowDate_str, firstTime,false)<2){//使用不超过3天
			 h1 =new Home_circle_Fragment();
			 
			 h2 =new Home_circle_old_Fragment();
			 Bundle b =new Bundle();
			 b.putString("time1", DateUtils.addDay(nowDate_str, -1));
			 b.putInt("tianshu", 1);
			 h2.setArguments(b);
			 fragments.add(h2);
			 fragments.add(h1);
		}else {
			
			 Bundle b =new Bundle();
			 b.putString("time1", DateUtils.addDay(nowDate_str, -1));
			 b.putInt("tianshu", 1);
			 h2 =new Home_circle_old_Fragment();
			 h2.setArguments(b);
			 
			 Bundle b2 =new Bundle();
			 b2.putString("time2", DateUtils.addDay(nowDate_str, -2));
			 b2.putInt("tianshu", 2);
			 h3 =new Home_circle_old_Fragment();
			 h3.setArguments(b2);
			 
			 h1 =new Home_circle_Fragment();
			 
			 fragments.add(h3);
			 fragments.add(h2);
			 fragments.add(h1);	
		}
		
		title_view.setText("今天");
		fragment_adapter =new Home_fragementAdapter(fragmentManager,fragments);
		
		viewPager.setOffscreenPageLimit(fragments.size()-1);
		viewPager.setAdapter(fragment_adapter);
		viewPager.setCurrentItem(fragments.size()-1);
		viewPager.setOnPageChangeListener(this);
		today_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(fragments.size()-1);//设置到最后
				title_view.setText("今天");
			}
		});
		return rootv;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		if(fragments.size()==1){
			showToday();
		}
		else if(fragments.size() == 2){
			switch (arg0) {
			case 0:
				showOneDay();
				break;
			case 1:
				showToday();
				break;
			}
		}else if(fragments.size() == 3){
			switch (arg0) {
			case 0:
				showTwoDay();
				break;
			case 1:
				showOneDay();
				break;
			case 2:
				showToday();
				break;
			}
		}
	}
	private void showToday(){
		today_view.setVisibility(View.GONE);
		title_view.setText("今天");
	}
	private void showOneDay(){
		today_view.setVisibility(View.VISIBLE);
		title_view.setText(DateUtils.StringToString(DateUtils.addDay(nowDate_str, -1), DateStyle.MM_DD_CN));
		}
	private void showTwoDay(){
		today_view.setVisibility(View.VISIBLE);
		title_view.setText(DateUtils.StringToString(DateUtils.addDay(nowDate_str, -2), DateStyle.MM_DD_CN));
	}
}