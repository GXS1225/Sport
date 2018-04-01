package edu.bzu.project.activity;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import java.util.Date;

import org.litepal.crud.DataSupport;

import edu.bzu.project.R;
import edu.bzu.project.R.layout;
import edu.bzu.project.R.menu;
import edu.bzu.project.activity.map.BMapActivity;

import edu.bzu.project.activity.setting.AddExercise_Acivity;
import edu.bzu.project.domain.Pedometer;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.fragment.FindFragment;
import edu.bzu.project.fragment.GroupFragment;
import edu.bzu.project.fragment.HomeFragment;
import edu.bzu.project.fragment.SettingFragment;
import edu.bzu.project.receiver.PedometerReceiver;
import edu.bzu.project.service.StepDetector;
import edu.bzu.project.service.StepService;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.utils.SportType;
import edu.bzu.project.utils.SportUtils;
import edu.bzu.project.view.MyTabView;
import edu.bzu.project.view.MyTabView.OnTabSelectedListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import edu.bzu.project.view.SelectPicPopupWindow;
/**
 * ������
 */
public class MainActivity extends FragmentActivity {
	
	private static String TAG="MainActivity";
	private Bundle savedInstanceState =null;
	//�ײ�����tabָʾ��������
	private MyTabView myTabView =null;
	//tab��Ӧfragment
	private HomeFragment homeFragment =null;
	private GroupFragment groupFragment =null;
	private FindFragment findFragment =null;
	private SettingFragment settingFragment =null;
	//�Զ���ĵ�������
	SelectPicPopupWindow menuWindow;
	private ImageView btn_text;
	//fragment����
	private int Indext_fragment =ConstantValues.HOME_FRAGMENT_INDEX;
	//fragment�������
	private FragmentManager fragmentManager =null;

	private Intent intent =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState =savedInstanceState;
		LogUtils.i(TAG, "onCreate()");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//��һ��ʹ�� ��¼һ��ʱ��
		if(SPUtils.get(MainActivity.this,ConstantValues.First_Time, "").equals("")){
			String firstTime =DateUtils.DateToString(Calendar.getInstance().getTime(), DateStyle.YYYY_MM_DD_CN);
			SPUtils.put(this, ConstantValues.First_Time, firstTime);
		}
		initView();
		initEvents();
		
		if(!StepService.FLAG){
			/**��������*/
			intent = new Intent(MainActivity.this, StepService.class);
			MainActivity.this.startService(intent);
		}
		boolean is_alaem =(Boolean) SPUtils.get(MainActivity.this, ConstantValues.is_Alarm, false);
		//��������
//				int bushu =(Integer) SPUtils.get(MainActivity.this, ConstantValues.BU_SHU, 0);
//				float shijian =(Float)SPUtils.get(MainActivity.this, ConstantValues.HUO_YUE, 0f);
//				String linchen =(String)SPUtils.get(MainActivity.this, ConstantValues.LIN_CHEN, "");
//			
//				Pedometer p =new Pedometer();
//				p.setBushul(bushu);
//				p.setMeter(SportUtils.getDistanceByPace(bushu));
//				p.setQianka(SportUtils.getCalorie(shijian/60, SportType.BU_XING));
//				p.setShijian(SportUtils.getMinuteBySecond(shijian));
//				
//				p.setRiqi(linchen);
//				if(DataSupport.find(Userinfo.class, 1) == null){
//					p.setMax(7000);
//				}else{
//					p.setMax((int)DataSupport.find(Userinfo.class, 1).getMax());
//				}
//				p.setTime(DateUtils.addDay(Calendar.getInstance().getTime(), -1));
//				p.save();
//				Toast.makeText(MainActivity.this, ((List<Pedometer>)DataSupport.findAll(Pedometer.class)).size()+"", 0).show();
		if(!is_alaem){
			/**��������*/
			startAlarm(MainActivity.this);
			SPUtils.put(MainActivity.this, ConstantValues.is_Alarm, true);
		}
	}

	//��Ӽ�����
	private void initEvents() {
		// TODO Auto-generated method stub
		myTabView.setOnTabSelectedListener(onTabSelectedListener);
	}
	/**�ص��ӿ�*/
	OnTabSelectedListener onTabSelectedListener =new OnTabSelectedListener() {
		
		@Override
		public void OnTabSelecteed(int index) {
			// TODO Auto-generated method stub
			FragmentTransaction transaction =fragmentManager.beginTransaction();
			if(index != ConstantValues.START_FRAGMENT_INDEX){
				hideFragment(transaction);
			}
			//�л�fragment
			switch (index) {
			case ConstantValues.HOME_FRAGMENT_INDEX:
				if(null ==homeFragment){
					homeFragment =new HomeFragment();
					transaction.add(R.id.activity_fragment, homeFragment);
				}else {
					transaction.show(homeFragment);
				}
				break;
			case ConstantValues.GROUP_FRAGMENT_INDEX:
				if(null ==groupFragment){
					groupFragment =new GroupFragment();
					transaction.add(R.id.activity_fragment, groupFragment);
				}else {
					transaction.show(groupFragment);
				}
				break;
			// ��ʼ����
			case ConstantValues.START_FRAGMENT_INDEX:
				// ����
				menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
				//��ʾ����
				menuWindow.showAtLocation(myTabView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case ConstantValues.FIND_FRAGMENT_INDEX:
				if(null ==findFragment){
					findFragment =new FindFragment();
					transaction.add(R.id.activity_fragment, findFragment);
				}else{
					transaction.show(findFragment);
				}
				
				break;
			case ConstantValues.SETTING_FRAGMENT_INDEX:
				if(null ==settingFragment){
					settingFragment =new SettingFragment();
					transaction.add(R.id.activity_fragment, settingFragment);
				}else {
					transaction.show(settingFragment);
				}
				break;
			}
			if(index != ConstantValues.START_FRAGMENT_INDEX){
				Indext_fragment =index;
			}
			transaction.commitAllowingStateLoss();
		}
	};

	//�ر����в�Ϊ�յ�fragment
	private void hideFragment(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if(null !=homeFragment){
			transaction.hide(homeFragment);
		}
		if(null !=groupFragment){
			transaction.hide(groupFragment);
		}
		if(null !=findFragment){
			transaction.hide(findFragment);
		}
		if(null !=settingFragment){
			transaction.hide(settingFragment);
		}
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		fragmentManager =getSupportFragmentManager();
		myTabView =(MyTabView)findViewById(R.id.myTabView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtils.i(TAG, "onResume()");
		onTabSelectedListener.OnTabSelecteed(Indext_fragment);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//����ʱ������ 
		//��������
		if(StepService.FLAG){
			SPUtils.put(MainActivity.this, ConstantValues.BU_SHU, StepDetector.CURRENT_SETP);
			SPUtils.put(MainActivity.this, ConstantValues.HUO_YUE, StepDetector.shijian);
		}
		LogUtils.i(TAG, "onPause()");
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtils.i(TAG, "onStop()");
	}

	//�Զ�����fragment����
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//super.onSaveInstanceState(outState);
		outState.putInt("indext", Indext_fragment);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//super.onRestoreInstanceState(savedInstanceState);
		Indext_fragment =savedInstanceState.getInt("index");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.i(TAG, "onDestroy()");
		//��������
		if(StepService.FLAG){
			SPUtils.put(MainActivity.this, ConstantValues.BU_SHU, StepDetector.CURRENT_SETP);
			SPUtils.put(MainActivity.this, ConstantValues.HUO_YUE, StepDetector.shijian);
			stopService(intent);//ֹͣ����
		}
	}
    //Ϊ��������ʵ�ּ�����
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			menuWindow.dismiss();
			Intent i =null;
			switch (v.getId()) {
			case R.id.btn_work:
				i = new Intent(MainActivity.this,BMapActivity.class);
				i.putExtra("status", "����");
				startActivity(i);
				break;
			case R.id.btn_cycle:	
				i = new Intent(MainActivity.this,BMapActivity.class);
				i.putExtra("status", "�ﳵ");
				startActivity(i);
				break;
			case R.id.btn_run:
				i = new Intent(MainActivity.this,BMapActivity.class);
				i.putExtra("status", "�ܲ�");
				startActivity(i);
				break;
			}	
			menuWindow.dismiss();
		}
    };
    
    private void startAlarm(Context context) {
		// TODO Auto-generated method stub
		 AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		 Intent intent =new Intent();
		 intent.setAction("edu.bzu.project.PedometerReceiver.SERVICE");
		 PendingIntent pendingIntent =PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
		 long firstTime = SystemClock.elapsedRealtime();	// ����֮�����ڵ�����ʱ��(����˯��ʱ��)

         Calendar c = Calendar.getInstance();
         c.setTimeInMillis(System.currentTimeMillis());
		 c.setTimeZone(TimeZone.getTimeZone("GMT+8")); // ����ʱ����Ҫ����һ�£���Ȼ����8��Сʱ��ʱ���
         Date date_next =DateUtils.addDay(c.getTime(), 1);
      
         long cha =date_next.getTime() -c.getTimeInMillis();//�賿��ʱ���
         
         firstTime+=cha;
         am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        		 1000*5, AlarmManager.INTERVAL_DAY, pendingIntent);
         
         LogUtils.i(TAG, "������");
         
	}
}
