package edu.bzu.project.activity.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import edu.bzu.project.R;
import edu.bzu.project.adapter.AddSport_fragmentAdapter;
import edu.bzu.project.domain.Sport_item;
import edu.bzu.project.fragment.Custom_fragment;
import edu.bzu.project.fragment.Hot_fragment;

import edu.bzu.project.fragment.Often_fragment;
import edu.bzu.project.other.OnSubmitClickListener;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.SPUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 添加运动的界面
 * @author houenshuo
 *
 */
public class AddExercise_Acivity extends FragmentActivity implements OnSubmitClickListener {
	private static String TAG ="AddExercise_Acivity";
	private  int items_value;
	private  double items_qianka_value;
	private ArrayList<Sport_item> lists =new ArrayList<Sport_item>();
	private ArrayList<Sport_item> sports =null;
	private ArrayList<Sport_item> oftens =null;
	private ArrayList<Sport_item> customs =null;
	//分段按钮
	private RadioGroup radioGroup =null;
	private RadioButton rButton1 =null;
	private RadioButton rButton2 =null;
	private RadioButton rButton3 =null;
	
	//viewpage
	private FragmentManager fm =null;
	private ViewPager viewPager =null;
	private AddSport_fragmentAdapter aSport_fragmentAdapter =null;
	private List<Fragment> list =null;//分段页面集合
	
	//fragment
	private Hot_fragment hot_fragment =null;
	private Often_fragment often_fragment =null;
	private Custom_fragment custom_fragment =null;
//	private FragmentTransaction transaction =null;
	private TextView items =null;
	private TextView items_qianka =null;
	private Button submit_ok =null;
	//对话框选择器
	private AlertDialog dialog =null;
	private Button bc =null;
	private Button bbc =null;
	private Button qx =null;

	private ImageButton back =null;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		LogUtils.i(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		lists =(ArrayList<Sport_item>) getIntent().getSerializableExtra("sportsyet");//获取以前选择过的数据
		if(lists == null){
			lists =new ArrayList<Sport_item>();
		}
		
		setContentView(R.layout.activity_addsport);
	//	EventBus.getDefault().register(this);//注册
		initView();
		initEvent();
	
		//以前选择的数据
		if(lists!=null && lists.size()>0){
			items_value =lists.size();
			for(Sport_item item:lists){
				items_qianka_value =item.getQianka()+items_qianka_value;
			}
			items.setText(String.valueOf(items_value));
			items_qianka.setText(String.valueOf(items_qianka_value));
		}
		
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.btn_remen_tag:
					viewPager.setCurrentItem(0);
					break;
				case R.id.btn_changyong_tag:
					viewPager.setCurrentItem(1);
					break;
				case R.id.btn_zidingyi_tag:
					viewPager.setCurrentItem(2);
					break;
				}
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
			
				//hideFragment(transaction);
				switch (position) {
				case 0:
					radioGroup.check(R.id.btn_remen_tag);
					break;
				case 1:

					radioGroup.check(R.id.btn_changyong_tag);
					break;
				case 2:
					radioGroup.check(R.id.btn_zidingyi_tag);
					break;
				}

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		back.setOnClickListener(onClickListener);
		submit_ok.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent =null;
			switch (v.getId()) {
			
			case R.id.addsport_back:
				//当有数据时，出现提示框
				if(items_value !=0 && items_qianka_value !=0 && lists.size()!=0){
					//加载布局
					View v1 =View.inflate(AddExercise_Acivity.this, R.layout.dialog_back, null);
					bbc =(Button)v1.findViewById(R.id.dialog_bbc);
					bc =(Button)v1.findViewById(R.id.dialog_bc);
					qx =(Button)v1.findViewById(R.id.dialog_qx);
					bbc.setOnClickListener(onClickListener);
					bc.setOnClickListener(onClickListener);
					qx.setOnClickListener(onClickListener);
					//对话框
					dialog =new AlertDialog.Builder(AddExercise_Acivity.this).setView(v1).show();
				}else {
					intent =new Intent();
					AddExercise_Acivity.this.setResult(AddSport_Activity.CODE_NO, intent);
					MyApplication.getInstance().removeActivity(AddExercise_Acivity.this);
					finish();
				}
				break;
			//对话框的保存
			case R.id.dialog_bc:
				if(items_value !=0 && items_qianka_value !=0 && lists.size()!=0){
					intent =new Intent();
					Bundle b =new Bundle();
					b.putSerializable("sports", lists);//效率低点
					b.putDouble("qianka", items_qianka_value);
					intent.putExtras(b);
					AddExercise_Acivity.this.setResult(AddSport_Activity.CODE, intent);
					
					dialog.dismiss();
					MyApplication.getInstance().removeActivity(AddExercise_Acivity.this);
					finish();
				}
				break;
			//对话框的不保存
			case R.id.dialog_bbc:
				//初始化话数据
				lists.clear();
				items_value =0;
				items_qianka_value =0.0;
				dialog.dismiss();
				
				intent =new Intent();
				AddExercise_Acivity.this.setResult(AddSport_Activity.CODE_NO, intent);
				MyApplication.getInstance().removeActivity(AddExercise_Acivity.this);
				finish();
				break;
			//对话框的取消
			case R.id.dialog_qx:
				//什么也不做，取消对话框
				dialog.dismiss();
				break;
			case R.id.sport_ok:
				if(items_value !=0 && items_qianka_value !=0 && lists.size()!=0){
					intent =new Intent();
					Bundle b =new Bundle();
					b.putSerializable("sports", lists);//效率低点
					b.putDouble("qianka", items_qianka_value);
					intent.putExtras(b);
					AddExercise_Acivity.this.setResult(AddSport_Activity.CODE, intent);
					
					MyApplication.getInstance().removeActivity(AddExercise_Acivity.this);
					finish();
				}
				break;
			default:
				break;
			}
		}
	};
	private void initView() {
		// TODO Auto-generated method stub
		radioGroup =(RadioGroup)findViewById(R.id.sport_radioGroup);
		rButton1 =(RadioButton)findViewById(R.id.btn_remen_tag);
		rButton2 =(RadioButton)findViewById(R.id.btn_changyong_tag);
		rButton3 =(RadioButton)findViewById(R.id.btn_zidingyi_tag);
		
		back =(ImageButton)findViewById(R.id.addsport_back);
		
		//记录的运动
		items =(TextView)findViewById(R.id.items);
		items_qianka =(TextView)findViewById(R.id.items_qianka);
		submit_ok =(Button)findViewById(R.id.sport_ok);
		items.setText(0+"");
		items_qianka.setText(0+"");
		viewPager =(ViewPager)findViewById(R.id.addsport_viewpager);
		//初始化viewpage的页面
		fm =getSupportFragmentManager();
		
		//根据类型来区分不同的数据
		list =new ArrayList<Fragment>();
		if(lists!=null && lists.size()>0){
			for(Sport_item item:lists){
				if(item.getType()==1){
					if(sports==null){
						sports =new ArrayList<Sport_item>();
					}
					sports.add(item);
				}else if(item.getType()==2){
					if(oftens ==null){
						oftens =new ArrayList<Sport_item>();
					}
					
					oftens.add(item);
				}else {
					if(customs ==null){
						customs =new ArrayList<Sport_item>();
					}
					customs.add(item);
				}
			}
		}
		//有以前选择数据时，根据实体类的类型，在分装到集合
		hot_fragment =new Hot_fragment();
		if(sports!=null && sports.size()>0){//以前选择，在此打开
			Bundle b1 =new Bundle();
			b1.putSerializable("sportsyet", sports);
			hot_fragment.setArguments(b1);
		}
		often_fragment =new Often_fragment();
		if(oftens!=null && oftens.size()>0){//以前选择，在此打开
			Bundle b2 =new Bundle();
			b2.putSerializable("oftensyet", oftens);
			often_fragment.setArguments(b2);
		}
		custom_fragment =new Custom_fragment();
		if(customs!=null && customs.size()>0){
			Bundle b3 =new Bundle();
			b3.putSerializable("customsyet", customs);
			custom_fragment.setArguments(b3);
		}
		list.add(hot_fragment);
		list.add(often_fragment);
		list.add(custom_fragment);	
		//自定义监听事件
		hot_fragment.setOnSubmitClickListener(this);
		custom_fragment.setOnSubmitClickListener(this);
		aSport_fragmentAdapter =new AddSport_fragmentAdapter(fm, list);
		viewPager.setOffscreenPageLimit(2);//预加载3个   会多点内存 但是能保存状态
		viewPager.setAdapter(aSport_fragmentAdapter);
	}
	//替代intent的传值
	public void onEventMainThread(Sport_item s){//订阅函数,可能有耗时的操作	
		onSubmitListener(s, true);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtils.i(TAG, "onResum");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	//	EventBus.getDefault().unregister(this);//反注册
	}
	//实现hotfragment的接口方法 获取数据
	@Override
	public void onSubmitListener(Sport_item item,boolean is) {
		// TODO Auto-generated method stub
		if(!is){
			if(items_value!=0 && lists.size()>0){
				//减去
				items_value--;
				items_qianka_value =items_qianka_value -item.getQianka();
				lists.remove(item);
			}
		}else {
			//添加
			items_value++;
			items_qianka_value +=item.getQianka();
			lists.add(item);//增加到集合
		}
		if(lists.size() == 0){
			submit_ok.setVisibility(View.GONE);
		}else{
			submit_ok.setVisibility(View.VISIBLE);
		}
		items.setText(String.valueOf(items_value));
		items_qianka.setText(String.valueOf(items_qianka_value));
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent =new Intent();
			AddExercise_Acivity.this.setResult(AddSport_Activity.CODE_NO, intent);
			MyApplication.getInstance().removeActivity(this);
			finish();
		}
		return true;
	}
}
