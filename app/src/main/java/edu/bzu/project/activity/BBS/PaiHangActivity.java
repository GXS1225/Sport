package edu.bzu.project.activity.BBS;

import java.util.ArrayList;
import java.util.List;

import edu.bzu.project.R;
import edu.bzu.project.activity.BBS.PKActivity.myPagerAdapter;
import edu.bzu.project.fragment.PK_fujin_fragment;
import edu.bzu.project.fragment.PaiHang_fragment;
import edu.bzu.project.fragment.Paihang_pk_fragment;
import edu.bzu.project.fragment.Pk_guanzhu_fragment;
import edu.bzu.project.utils.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * 排行的界面
 */
public class PaiHangActivity extends FragmentActivity {
private Context context =null;
	
	private List<Fragment> fragments =new ArrayList<Fragment>();
	private List<String> strs =new ArrayList<String>();
	
	//fragment
	private FragmentManager manager =null;
	private ViewPager viewPager =null;
	private Button back =null;
	private TextView title =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pk);
		context =this;
		MyApplication.getInstance().addActivity(PaiHangActivity.this);
		
		initView();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().removeActivity(PaiHangActivity.this);
				finish();
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		manager =getSupportFragmentManager();
		viewPager =(ViewPager)findViewById(R.id.viewPager);
		back =(Button)findViewById(R.id.pk_back);
		title =(TextView)findViewById(R.id.pk_title);
		title.setText("排行榜");
		fragments.add(new PaiHang_fragment());
		fragments.add(new Paihang_pk_fragment());
		strs.add("运动排行");
		strs.add("pk排行");
		
		viewPager.setAdapter(new myPagerAdapter(manager, fragments, strs));
	}
	
	
	class myPagerAdapter extends FragmentPagerAdapter {  
		  
        private List<Fragment> fragmentList;  
        private List<String>   titleList;  
  
        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList){  
            super(fm);  
            this.fragmentList = fragmentList;  
            this.titleList = titleList;  
        }  
  
        /** 
         * 得到每个页面 
         */  
        @Override  
        public Fragment getItem(int arg0) {  
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);  
        }  
  
        /** 
         * 每个页面的title 
         */  
        @Override  
        public CharSequence getPageTitle(int position) {  
            return (titleList.size() > position) ? titleList.get(position) : "";  
        }  
  
        /** 
         * 页面的总个数 
         */  
        @Override  
        public int getCount() {  
            return fragmentList == null ? 0 : fragmentList.size();  
        }  
    }  
}
