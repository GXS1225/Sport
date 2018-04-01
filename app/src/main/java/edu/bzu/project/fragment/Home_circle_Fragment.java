package edu.bzu.project.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.litepal.crud.DataSupport;


import edu.bzu.project.R;


import edu.bzu.project.domain.Pedometer;
import edu.bzu.project.domain.Userinfo;
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
import edu.bzu.project.view.CircleProgressBar;
import edu.bzu.project.view.ExpandedCircleProgressView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * ������֮��ҳ�ļƲ�����¼Fragment
 *
 */
public class Home_circle_Fragment extends BaseFragment {

	private static String TAG="Home_circle_Fragment";
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return TAG;
	}
	/**homecircle�Ĳ���*/
	private RelativeLayout circlePregress =null;
	private RelativeLayout circleTianqi =null;
	/**�Զ��廷�ν���*/
	private CircleProgressBar circleProgressBar =null;
	private TextView gongli =null;
	private TextView qianka =null;
	private TextView shijian =null;
	/**�Զ��廷�ν��ȵ�����*/
	private TextView currentbushu =null;
	private TextView mubiao =null;
	private ImageView mfull =null;
	/**���λ���ͼƬ*/
	private ExpandedCircleProgressView mbuffer1 =null;
	private ImageView mbuffer=null;
	private int mode;
	/**��ҳ��ť*/
	private ImageButton mleft =null;
	private ImageButton mright =null;
	/**���������ֵ����ǰ�Ĳ���*/
	private int max;
	
	//�̣߳�ֻҪ�������У����ϵ�ȥ��ȡ������������
	private Thread thread =null;
	private int current_bushu =0;//��ǰ����
	private int BUFFER_SERP =0;//���岽��
	private float SHIJIAN ;
	private boolean is_today =false;

	private Context context =null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootv =inflater.inflate(R.layout.home_circle, container, false);

		circlePregress =(RelativeLayout)rootv.findViewById(R.id.home_circle);
		//�Զ������
		circleProgressBar =(CircleProgressBar)rootv.findViewById(R.id.home_circle_Bar);
		//���ν��ȵ�����
		currentbushu =(TextView)rootv.findViewById(R.id.hone_circle_current);
		mubiao =(TextView)rootv.findViewById(R.id.home_circle_mubiao);
		mbuffer =(ImageView)rootv.findViewById(R.id.home_circle_full);
		mfull =(ImageView)rootv.findViewById(R.id.home_circle_top);
		gongli =(TextView)rootv.findViewById(R.id.home_tab_bushu_btm_value);
		qianka =(TextView)rootv.findViewById(R.id.home_tab_weather_value);
		shijian =(TextView)rootv.findViewById(R.id.home_tab_shijian_value);
		mleft =(ImageButton)rootv.findViewById(R.id.home_left_btn);
		mright =(ImageButton)rootv.findViewById(R.id.home_right_btn);
		//�ж��Ƿ��һ��Ʋ�
		Date nowDate =Calendar.getInstance().getTime();
		String nowDate_str =DateUtils.DateToString(nowDate, DateStyle.YYYY_MM_DD_CN);
		String isFirst =(String) SPUtils.get(getActivity(), ConstantValues.First_Time, "");
		String oldDate_str =DateUtils.StringToString(isFirst, DateStyle.YYYY_MM_DD_CN);
		if(DateUtils.getIntervalDays(nowDate_str, oldDate_str,false)>0){
			mleft.setVisibility(View.VISIBLE);
		}
		//��ʼ�����ȣ���ȡ��������֮
		initCircleBar();
		mThread();
		return rootv;
	}
	
	private void initCircleBar(){
		circleProgressBar.setProgress(current_bushu, 1000);//����
		circleProgressBar.setMax(max);
		mubiao.setText(max+"");//���ֵ
		currentbushu.setText(String.valueOf(current_bushu));//��ǰ����
		//���������
		gongli.setText(String.valueOf(SportUtils.getDistanceByPace(current_bushu)));
		//ǧ��������
		if(SHIJIAN>60){
			qianka.setText(SportUtils.getCalorie(SHIJIAN/60, SportType.BU_XING)+"");
		}else if(SHIJIAN ==0){
			qianka.setText("0");
		}else{
			qianka.setText("2");
		}
		//��Ծ�ȵ�����
		shijian.setText(SportUtils.getMinuteBySecond(SHIJIAN)+"����");
	}
	
	private void mThread(){
		if(thread ==null){
			thread =new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					while(true){
						try {
							Thread.sleep(500);//500��������㲽���㷨�Ǻ�
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(StepService.FLAG){//��������ʱ��
							/*
							 * ��ǰ�����뻺��Ĳ�����ֵ
							 * 	��ֵ��10���ڣ�
							 * 		�û��˶����Ŵ�����䶯��
							 * 		�û����˶�������ֵ���ӣ�����С��䶯��
							 *  ��ֵ����10
							 * 		�û��˶����ı�������ֵ�������������
							 * 		�û����˶�:����ֵ����Ϊ��ǰ����
							 * 
							 * ���벻���ı�־��
							 * */
						//	if(StepDetector.CURRENT_SETP - BUFFER_SERP>=0){
								Message message =new Message();
								message.what =0;
//								int cha =StepDetector.CURRENT_SETP - BUFFER_SERP;
//								if(cha == 0){
//									
//								}
//								else if(cha<=10&&cha>0){
//									message.what =1;
//									if(++StepDetector.BUFFER_SERP == StepDetector.CURRENT_SETP){//����
//										BUFFER_SERP++;
//										cha =StepDetector.CURRENT_SETP - BUFFER_SERP;//��С������
//										message.obj =new Integer(cha);
//							
//									}else {
//										message.obj=new Integer(cha);
//										
//									}
//									handler.sendMessage(message);
//									
//								}else {
//									message.what=0;
//									if(++StepDetector.BUFFER_SERP == StepDetector.CURRENT_SETP){
//										BUFFER_SERP =StepDetector.CURRENT_SETP;//����Ϊ��ǰ����
//										message.obj =new Integer(0);//��С
//									}else{
//										message.obj=new Integer(10);//���
//									}
									
									handler.sendMessage(message);
					//			}
							}
							
						}
					}
			//	}
			};
			thread.start();
		}
	}
	
	/**��Ӧ���񣬸ı�ui����*/
	Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			circleProgressBar.setMax(max);
			mubiao.setText(max+"");//���ֵ
			if(msg.what ==0){
				super.handleMessage(msg);
				current_bushu =StepDetector.CURRENT_SETP;//��ȡ����
				SHIJIAN =StepDetector.shijian;//��ȡʱ��
				
				circleProgressBar.setProgress(current_bushu, 0);
				currentbushu.setText(String.valueOf(current_bushu));
				
				//���������
				gongli.setText(String.valueOf(SportUtils.getDistanceByPace(current_bushu)));
				//ǧ��������
				if(SHIJIAN>60){
					qianka.setText(SportUtils.getCalorie(SHIJIAN/60, SportType.BU_XING)+"");
				}else if(SHIJIAN ==0){
					qianka.setText("0");
				}else{
					qianka.setText("2");
				}
				//��Ծ�ȵ�����
				shijian.setText(SportUtils.getMinuteBySecond(SHIJIAN)+"����");
//				Animation animation =AnimationUtils.loadAnimation(context, R.anim.home_circlealpha);
//				mfull.startAnimation(animation);
//				animation.setRepeatCount(1);
				
			}
			else if(msg.what ==1) {
				int cha =(Integer)msg.obj;
//				float fromTo[] =new float[]{0.1f,(float)cha/10*1.0f,
//						0.1f,(float)cha/10*1.0f};
				float fromTo[] =new float[]{0.1f,(float)cha*1.0f/10.0f,
						0.1f,(float)cha*1.0f/10.0f};
				//Animation_Utils.ScaleOut_HomeCircle(mbuffer, getActivity(), 500, fromTo,mode);

			}
		}
	};
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context =activity;
		LogUtils.i("------------------", current_bushu+"");
		initData();
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//���¼�������
		initData();
		
	}
	public void initData(){
		if(StepService.FLAG){
			/*ʱ�� ����*/
			current_bushu =(Integer)SPUtils.get(getActivity(),  ConstantValues.BU_SHU, 0);
			SHIJIAN =(Float)SPUtils.get(getActivity(), ConstantValues.HUO_YUE, 0f);
			StepDetector.CURRENT_SETP =current_bushu;	
			StepDetector.shijian =SHIJIAN;
		}
		if(DataSupport.find(Userinfo.class, 1) ==null){
			max =7000;//Ĭ����ǧ
		}else{
			max =(int) DataSupport.find(Userinfo.class, 1).getMax();
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
