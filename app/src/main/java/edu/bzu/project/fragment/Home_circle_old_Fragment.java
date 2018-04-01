package edu.bzu.project.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import edu.bzu.project.R;
import edu.bzu.project.adapter.SportsData_adapter;
import edu.bzu.project.domain.Pedometer;
import edu.bzu.project.domain.Sport_item;
import edu.bzu.project.domain.Sport_w;
import edu.bzu.project.domain.Userinfo;
import edu.bzu.project.receiver.PedometerReceiver;
import edu.bzu.project.service.StepDetector;
import edu.bzu.project.service.StepService;
import edu.bzu.project.sql.WithOut_sqlDao;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.utils.SportType;
import edu.bzu.project.utils.SportUtils;
import edu.bzu.project.view.CircleProgressBar;
import edu.bzu.project.view.ExpandedCircleProgressView;
import edu.bzu.project.view.LoadMoreListView;
/**
 * 历史记录的计步器fragment
 * 可以和Home_circle_Fragment合用一个
 * 没修改
 */
public class Home_circle_old_Fragment extends BaseFragment {

	private static  String TAG="Home_circle_old_Fragment";
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return TAG;
	}
	/**homecircle的布局*/
	private RelativeLayout circlePregress =null;
	private RelativeLayout circleTianqi =null;
	/**自定义环形进度*/
	private CircleProgressBar circleProgressBar =null;
	
	private TextView gongli =null;
	private TextView qianka =null;
	private TextView shijian =null;
	/**自定义环形进度的中心*/
	private TextView currentbushu =null;
	private TextView mubiao =null;
	private ImageView mfull =null;
	/**环形缓存图片*/
	private ExpandedCircleProgressView mbuffer1 =null;
	private ImageView mbuffer=null;
	private int mode;
	/**翻页按钮*/
	private ImageButton mleft =null;
	private ImageButton mright =null;
	private boolean is_today =false;
	
	/**步数的最大值及当前的步数*/
	private int max;
	
	private Context context =null;
	
	private Pedometer pedometer =null;

public List<Pedometer> selectdate(String date){
	
	List<Pedometer> p_list =DataSupport.where("riqi = ? ",date).find(Pedometer.class);
	return p_list;
	
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	View rootv =inflater.inflate(R.layout.home_circle, container, false);
	//自定义进度
	circleProgressBar =(CircleProgressBar)rootv.findViewById(R.id.home_circle_Bar);
	//环形进度的中心
	currentbushu =(TextView)rootv.findViewById(R.id.hone_circle_current);
	mubiao =(TextView)rootv.findViewById(R.id.home_circle_mubiao);
	mbuffer =(ImageView)rootv.findViewById(R.id.home_circle_full);
	gongli =(TextView)rootv.findViewById(R.id.home_tab_bushu_btm_value);
	qianka =(TextView)rootv.findViewById(R.id.home_tab_weather_value);
	shijian =(TextView)rootv.findViewById(R.id.home_tab_shijian_value);
	mleft =(ImageButton)rootv.findViewById(R.id.home_left_btn);
	mright =(ImageButton)rootv.findViewById(R.id.home_right_btn);
	//显示 ui
	//第二天
	 if(getArguments().getInt("tianshu")==1){
			//查数据	
			List<Pedometer> p =selectdate(getArguments().getString("time1"));
			if(p.size()<=0){
				Pedometer tianbu =new Pedometer();
				tianbu.setRiqi(getArguments().getString("time1"));
				tianbu.setBushul(0);
				tianbu.setMeter(0);
				tianbu.setQianka(0);
				tianbu.setShijian(0);
				if(DataSupport.find(Userinfo.class, 1) == null){
					tianbu.setMax(7000);
				}else{
					tianbu.setMax((int)DataSupport.find(Userinfo.class, 1).getMax());
				}
				tianbu.saveThrows();
				pedometer =tianbu;
			}else{
				pedometer =p.get(0);
			}
			mright.setVisibility(View.VISIBLE);
			Date nowDate =Calendar.getInstance().getTime();
			String nowDate_str =DateUtils.DateToString(nowDate, DateStyle.YYYY_MM_DD_CN);
			String isFirst =(String) SPUtils.get(getActivity(), ConstantValues.First_Time, "");
			String oldDate_str =DateUtils.StringToString(isFirst, DateStyle.YYYY_MM_DD_CN);
			if(DateUtils.getIntervalDays(nowDate_str, oldDate_str,false)>1){
				mleft.setVisibility(View.VISIBLE);
			}
		}
	 	//第三天
	 	else if(getArguments().getInt("tianshu")==2){
			//查数据
			List<Pedometer> p =selectdate(getArguments().getString("time2"));
			if(p.size()<=0 ){
				Pedometer tianbu =new Pedometer();
				Calendar c =Calendar.getInstance();
				tianbu.setRiqi(getArguments().getString("time2"));
				tianbu.setBushul(0);
				tianbu.setMeter(0);
				tianbu.setQianka(0);
				tianbu.setShijian(0);
				if(DataSupport.find(Userinfo.class, 1) == null){
					tianbu.setMax(7000);
				}else{
					tianbu.setMax((int)DataSupport.find(Userinfo.class, 1).getMax());
				}
				tianbu.saveThrows();
				pedometer =tianbu;
			}else{
				pedometer =p.get(0);
			}
			mright.setVisibility(View.VISIBLE);
		}
	//获取是否第一天计步
	//初始化进度，获取数据设置之
	initCircleBar();
	
	return rootv;
}

private void initCircleBar(){
	 circleProgressBar.setProgress(pedometer.getBushul(), 0);
	 currentbushu.setText(pedometer.getBushul()+"");
	 mubiao.setText(pedometer.getMax()+"");
	 gongli.setText(pedometer.getMeter()+"");
	 qianka.setText(pedometer.getQianka()+"");
	 shijian.setText(pedometer.getShijian()+"");
}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context =activity;
	   
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
