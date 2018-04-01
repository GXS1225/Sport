package edu.bzu.project.activity.map;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import edu.bzu.project.R;

import edu.bzu.project.activity.setting.AddExercise_Acivity;
import edu.bzu.project.domain.bmob.Map_domain;
import edu.bzu.project.service.StepService;
import edu.bzu.project.utils.CommonUtils;
import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.DateStyle;
import edu.bzu.project.utils.DateUtils;
import edu.bzu.project.utils.LogUtils;
import edu.bzu.project.utils.MyApplication;
import edu.bzu.project.utils.SPUtils;
import edu.bzu.project.utils.SportType;
import edu.bzu.project.utils.SportUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
/**
 * 地图运动页面
 * @author houenshuo
 *
 */
public class BMapActivity extends Activity {
	
	//定位
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode=LocationMode.FOLLOWING;
	private BitmapDescriptor mCurrentMarker;
	//地图
	private MapStatusUpdate msu;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	//标记
	private Marker marker_start, marker_end;
	//标题
	private String mode =null;
	private TextView map_title =null;
	private Button map_jiesuo =null;
	private Button map_btn_jixu =null;
	private Button map_btn_jieshu =null;
	private Chronometer chronometer =null;
	
	private TextView map_suduShow =null;
	private TextView map_juliShow =null;
	private TextView map_qiankaShow =null;
	private long timeCha ;
	private boolean isChronometer =true;
	private boolean isStop =false;
	//定义距离变量
	
	double distanceSum =0.0;
	// 轨迹相关
	private List<LatLng> latlngs = null;
	private List<BmobGeoPoint> points =null;
	private int count = 0, count_point = 0;// 定位点集合的大小
	
	private AlertDialog dialog =null;
	private TextView dialog_title =null;
	private Button bc =null;
	private Button bbc =null;
	private Button qx =null;
	
	private int miao =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(BMapActivity.this);
		setContentView(R.layout.activity_bmap);
		//关闭 计步
		if(StepService.FLAG){
			Intent intent =new Intent(BMapActivity.this, StepService.class);
			stopService(intent);
		}
		initView();
	//地图的初始化
		initMap();
	//定位的初始化
		location();
	
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		//结束
		map_btn_jieshu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map_title.setText(mode+"正结束");
				//保存数据,出现对话框
				View v1 =View.inflate(BMapActivity.this, R.layout.dialog_back, null);
				dialog =CommonUtils.getAlertDialog(BMapActivity.this, v1);
				dialog_title =(TextView)v1.findViewById(R.id.sport_title);
				bbc =(Button)v1.findViewById(R.id.dialog_bbc);
				bc =(Button)v1.findViewById(R.id.dialog_bc);
				qx =(Button)v1.findViewById(R.id.dialog_qx);
				//判断数据的多少
				if(distanceSum>=0){//
					dialog_title.setVisibility(View.VISIBLE);
					dialog_title.setText("你确定已经尽力了吗？保存将上传服务器");
				}else{//运行距离太少
					dialog_title.setVisibility(View.VISIBLE);
					bc.setVisibility(View.GONE);
				}
				bbc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						MyApplication.getInstance().removeActivity(BMapActivity.this);
						finish();
					}
				});
				//保存
				bc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(latlngs!=null && latlngs.size()>0 && distanceSum>=0){
							//生成 运动单
							Map_domain domain =new Map_domain();
							domain.setJuli(SportUtils.getKM(distanceSum));
							domain.setQianka(Double.parseDouble(map_qiankaShow.getText().toString()));
							domain.setMode(mode);
							double sudu =distanceSum / miao;//距离除以时间
							//domain.setSport_time(chronometer.getFormat());
							domain.setSudu(SportUtils.getFormatKM(sudu));
							domain.setSummiao(miao);
							domain.setPoints(points);
							//当前的时间
							String now_time =DateUtils.DateToString(Calendar.getInstance().getTime(), DateStyle.YYYY_MM_DD_HH_MM_CN);
							domain.setTime(now_time);
							Intent intent =new Intent(BMapActivity.this,ShowInfoActivity.class);
							intent.putExtra("sportInfo", domain);
							startActivity(intent);
							
							//关闭
							dialog.dismiss();
							MyApplication.getInstance().removeActivity(BMapActivity.this);
							finish();
						}
					}
				});
				qx.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}
		});
		//继续
		map_btn_jixu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map_jiesuo.setVisibility(View.VISIBLE);
				map_btn_jieshu.setVisibility(View.GONE);
				map_btn_jixu.setVisibility(View.GONE);
				
				isStop =false;
				map_title.setText(mode+"正在进行");
				chronometer.setBase(SystemClock.elapsedRealtime()-timeCha);
				chronometer.start();
			}
		});
		//解锁暂停
		map_jiesuo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map_jiesuo.setVisibility(View.GONE);
				map_btn_jieshu.setVisibility(View.VISIBLE);
				map_btn_jixu.setVisibility(View.VISIBLE);
				
				isStop =true;
				map_title.setText(mode+"已暂停");
				timeCha =SystemClock.elapsedRealtime() -chronometer.getBase();
				chronometer.stop();
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.mapview);
		map_title =(TextView)findViewById(R.id.map_title);
		map_jiesuo =(Button)findViewById(R.id.map_jiesuo);
		map_btn_jixu =(Button)findViewById(R.id.map_btn1);
		map_btn_jieshu =(Button)findViewById(R.id.map_btn2);
		chronometer =(Chronometer)findViewById(R.id.map_jishi);
		
		map_suduShow =(TextView)findViewById(R.id.map_suduShow);
		map_qiankaShow =(TextView)findViewById(R.id.map_qiankaShow);
		map_juliShow =(TextView)findViewById(R.id.map_juliShow);
		map_suduShow.setText("0.0");
		map_juliShow.setText("0.0");
		map_qiankaShow.setText("0.0");
		
		//获取模式
		mode =getIntent().getStringExtra("status");
		map_title.setText("正在"+mode+"中");
	}
	private void initMap() {
		// TODO Auto-generated method stub
		// 地图初始化
		mBaiduMap = mMapView.getMap();
		// 设置地图的缩放级别
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(18).build()));
		//设置是否允许楼块效果
		 mBaiduMap.setBuildingsEnabled(true);
		 //	配置定位图层显示方式-自定义图标
		 mCurrentMarker = BitmapDescriptorFactory
					.fromResource(R.drawable.marker);
		 mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					mCurrentMode, true, mCurrentMarker));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
	}
	/** 定位*/
	private void location() {
	// 定位初始化
		mLocClient = new LocationClient(MyApplication.getInstance().getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

//	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
	
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			LogUtils.i("地图监听器", "经行中");
			//在地图上显示定位
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.speed(location.getSpeed())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection()).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			
			//设置地图的新中心点
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			BmobGeoPoint point =new BmobGeoPoint();
			point.setLatitude(location.getLatitude());
			point.setLongitude(location.getLongitude());
			
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.animateMapStatus(msu);
			//计时器开始
			if(isChronometer){
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();//启动
				isChronometer =false;
				chronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
					
					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub
						miao+=1;
					}
				});
			}
			//刚开始
			if(latlngs == null){
				latlngs =new ArrayList<LatLng>();
				points =new ArrayList<BmobGeoPoint>();
				
				points.add(point);//添加到
				latlngs.add(latLng);
				DrewMarker(latLng, ConstantValues.MARKERT_START);
			}else{//计算
				if (!isStop) {
					count = latlngs.size();
					if (latlngs.get(count - 1) != latLng) {
						latlngs.add(latLng);
						points.add(point);//添加到
						
						setViewDistance(location); 
					}
				}
			}
		}
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	//计算距离
	private void setViewDistance(BDLocation location) {
		if(latlngs!=null && latlngs.size()>=1){
			double distance=DistanceUtil.getDistance(latlngs.get(latlngs.size()-2), latlngs.get(latlngs.size()-1));
			//判断距离 不能太过偏差（还可细分各种模式）
			if(distance<50){
				OverlayOptions ooPolyline = new PolylineOptions()
					.width(10).color(0xff00ffff).points(latlngs).zIndex(5);
					mBaiduMap.addOverlay(ooPolyline);
				mBaiduMap.addOverlay(ooPolyline);
				distanceSum+=distance;
				//设置距离
				map_juliShow.setText(String.valueOf(SportUtils.getKM(distanceSum)));
				map_suduShow.setText(String.valueOf(SportUtils.getFormatKM(location.getSpeed())));
				//设置千卡
				if(mode.equals("跑步")){
					map_qiankaShow.setText(String.valueOf(SportUtils.getKLL(SportUtils.getKM(distanceSum), SportType.PAO_BU)));
				}else if(mode.equals("竞走")){
					map_qiankaShow.setText(String.valueOf(SportUtils.getKLL(SportUtils.getKM(distanceSum), SportType.JING_ZOU)));
				}else{
					map_qiankaShow.setText(String.valueOf(SportUtils.getKLL(SportUtils.getKM(distanceSum), SportType.QI_CHE)));
				}
			}else{//移除
				latlngs.remove(latlngs.size()-1);
				points.remove(points.size()-1);
			}
		}
	}
	
	// 画Marker
	private void DrewMarker(LatLng latLng, int markertStart) {
		BitmapDescriptor start = BitmapDescriptorFactory
				.fromResource(R.drawable.v3_icon_qidian);
		BitmapDescriptor end = BitmapDescriptorFactory
				.fromResource(R.drawable.v3_icon_zhongdian);
		//起点
		if (markertStart == ConstantValues.MARKERT_START) {
			OverlayOptions startM = new MarkerOptions().position(latLng)
					.icon(start).zIndex(5);
			marker_start = (Marker) mBaiduMap.addOverlay(startM);
		} else {//终点
			OverlayOptions endM = new MarkerOptions().position(latLng)
					.icon(end).zIndex(5);
			marker_end = (Marker) mBaiduMap.addOverlay(endM);
		}
	}
	
	private void ClearLine() {
		mBaiduMap.clear();
	}
	
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		if (ConstantValues.isTrack)
			ConstantValues.isTrack = false;
		if(!StepService.FLAG){
			Intent intent =new Intent(BMapActivity.this, StepService.class);
			startService(intent);
		}
		super.onDestroy();
	}
}
